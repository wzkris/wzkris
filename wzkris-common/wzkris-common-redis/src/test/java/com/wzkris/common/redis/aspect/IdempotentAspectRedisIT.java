package com.wzkris.common.redis.aspect;

import com.wzkris.common.core.exception.request.TooManyRequestException;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.redis.annotation.Idempotent;
import com.wzkris.common.redis.enums.IdempotentStatusEnum;
import com.wzkris.common.redis.model.IdempotentRecord;
import com.wzkris.common.redis.util.RedisJsonUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 幂等切面真实 Redis Cluster 集成测试。
 * <p>
 * 使用本地 Redis Cluster（127.0.0.1:6379/6380/6381）+ Redisson 连接工厂构建
 * 与生产一致的 RedisTemplate（clean JSON、无 @class），并通过 AspectJProxyFactory
 * 走真实 AOP 代理调用 @Idempotent 方法，验证序列化与幂等语义的全链路。
 * <p>
 * 前置条件：本地已启动 Redis Cluster（三节点 6379/6380/6381）。
 */
class IdempotentAspectRedisIT {

    private RedissonClient redisson;

    private RedisTemplate<String, Object> redisTemplate;

    private StringRedisTemplate stringRedisTemplate;

    private DemoService proxy;

    @BeforeEach
    void setUp() {
        Config config = new Config();
        config.useClusterServers().addNodeAddress(
                "redis://127.0.0.1:6379", "redis://127.0.0.1:6380", "redis://127.0.0.1:6381");
        redisson = Redisson.create(config);

        RedisConnectionFactory factory = new RedissonConnectionFactory(redisson);
        redisTemplate = buildRedisTemplate(factory);
        stringRedisTemplate = new StringRedisTemplate(factory);
        stringRedisTemplate.afterPropertiesSet();

        DemoService.counter.set(0);
        AspectJProxyFactory proxyFactory = new AspectJProxyFactory();
        proxyFactory.setTarget(new DemoService());
        proxyFactory.addAspect(new IdempotentAspect(redisTemplate, JsonUtil.getObjectMapper()));
        proxy = proxyFactory.getProxy();
    }

    @AfterEach
    void tearDown() {
        redisTemplate.delete(redisTemplate.keys("idem:*"));
        redisson.shutdown();
    }

    private RedisTemplate<String, Object> buildRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer jsonSerializer =
                new GenericJackson2JsonRedisSerializer(JsonUtil.getObjectMapper().copy());
        template.setDefaultSerializer(stringRedisSerializer);
        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(jsonSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(jsonSerializer);
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 按业务参数在 idem:* 中定位幂等键（结果值中应包含该参数）。
     */
    private String findKey(String bizNo) {
        return redisTemplate.keys("idem:*").stream()
                .filter(k -> String.valueOf(redisTemplate.opsForValue().get(k)).contains(bizNo))
                .findFirst()
                .orElseThrow(() -> new AssertionError("未找到业务参数 " + bizNo + " 对应的幂等记录"));
    }

    @Test
    void firstCallExecutesAndDuplicateReturnsCached() {
        OrderResult first = proxy.createOrder("biz-1");
        OrderResult second = proxy.createOrder("biz-1");

        assertEquals(1, DemoService.counter.get(), "业务方法应只执行一次");
        assertEquals(first, second, "重复请求应命中缓存返回首次结果");
        assertFalse(redisTemplate.keys("idem:*").isEmpty(), "切面应写入幂等键（只有切面写 idem: 前缀的键）");
    }

    @Test
    void differentArgsUseDifferentKeys() {
        OrderResult a = proxy.createOrder("biz-A");
        OrderResult b = proxy.createOrder("biz-B");

        assertEquals(2, DemoService.counter.get(), "不同业务号应各自执行");
        assertEquals("R1", a.orderNo());
        assertEquals("R2", b.orderNo());
        assertEquals(2, redisTemplate.keys("idem:*").size(), "不同业务参数应产生不同的幂等键");
    }

    @Test
    void cachedValueIsCleanJsonWithoutTypeInfo() {
        proxy.createOrder("biz-json");

        String key = findKey("biz-json");
        String json = stringRedisTemplate.opsForValue().get(key);

        assertFalse(json.contains("@class"), "clean JSON 不应包含类型标识 @class，实际: " + json);
        assertTrue(json.contains("\"status\":\"done\""), "应序列化为 done 状态，实际: " + json);
    }

    @Test
    void resultRoundTripsThroughTypedParse() {
        proxy.createOrder("biz-typed");

        String key = findKey("biz-typed");
        IdempotentRecord record = RedisJsonUtil.parse(redisTemplate.opsForValue().get(key), IdempotentRecord.class);

        assertEquals(IdempotentStatusEnum.DONE, record.getStatus());
        // result 经 clean JSON 反序列化为 LinkedHashMap，RedisJsonUtil.parse 按目标类型还原
        OrderResult result = RedisJsonUtil.parse(record.getResult(), OrderResult.class);
        assertEquals("biz-typed", result.bizNo());
        assertEquals("R1", result.orderNo());
    }

    @Test
    void processingRecordRejectsDuplicate() {
        proxy.createOrder("biz-processing"); // 先执行一次，生成幂等键

        // 把记录改为 PROCESSING，模拟“仍在处理中”被并发重复请求命中
        String key = findKey("biz-processing");
        redisTemplate.opsForValue().set(key, IdempotentRecord.processing());

        assertThrows(TooManyRequestException.class, () -> proxy.createOrder("biz-processing"));
    }

    @Test
    void plainMethodWithoutAnnotationDoesNotCreateKey() {
        // 阴性对照：未标注 @Idempotent 的方法不应被切面拦截，也不应产生幂等键，
        // 证明幂等键只由切面按注解触发写入，而非其他逻辑碰巧写入。
        proxy.plainMethod("biz-plain");

        assertTrue(redisTemplate.keys("idem:*").isEmpty(), "未标注 @Idempotent 的方法不应产生幂等键");
    }

    /**
     * 被代理的演示服务：方法执行次数由静态计数器统计，便于断言只执行一次。
     */
    public static class DemoService {

        private static final AtomicInteger counter = new AtomicInteger();

        @Idempotent(ttlSeconds = 120)
        public OrderResult createOrder(String bizNo) {
            return new OrderResult("R" + counter.incrementAndGet(), bizNo);
        }

        /** 无注解方法：作为阴性对照，证明切面只拦截 @Idempotent 方法。 */
        public String plainMethod(String bizNo) {
            return "plain-" + bizNo;
        }

    }

    /**
     * 订单结果 POJO：验证 Object 类型 result 的序列化/反序列化往返。
     */
    public record OrderResult(String orderNo, String bizNo) {

    }

}
