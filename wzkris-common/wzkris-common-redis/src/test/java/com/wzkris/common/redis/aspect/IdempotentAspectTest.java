package com.wzkris.common.redis.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wzkris.common.core.exception.request.TooManyRequestException;
import com.wzkris.common.redis.annotation.Idempotent;
import com.wzkris.common.redis.model.IdempotentRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class IdempotentAspectTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private MethodSignature signature;

    private IdempotentAspect idempotentAspect;

    private Map<String, Object> redisStore;

    @BeforeEach
    void setUp() {
        idempotentAspect = new IdempotentAspect(redisTemplate, new ObjectMapper());
        redisStore = new ConcurrentHashMap<>();

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getDeclaringTypeName()).thenReturn("com.wzkris.demo.TestService");
        when(signature.getName()).thenReturn("submit");
        when(signature.getReturnType()).thenReturn(String.class);

        when(valueOperations.setIfAbsent(anyString(), any(), any(Duration.class)))
                .thenAnswer(invocation -> {
                    String key = invocation.getArgument(0);
                    Object value = invocation.getArgument(1);
                    return redisStore.putIfAbsent(key, value) == null;
                });
        doAnswer(invocation -> {
            String key = invocation.getArgument(0);
            Object value = invocation.getArgument(1);
            redisStore.put(key, value);
            return null;
        }).when(valueOperations).set(anyString(), any(), any(Duration.class));
        when(valueOperations.get(anyString())).thenAnswer(invocation -> redisStore.get(invocation.getArgument(0)));
        when(redisTemplate.delete(anyString())).thenAnswer(invocation -> redisStore.remove(invocation.getArgument(0)) != null);
    }

    @Test
    void shouldReuseCachedResultWhenJsonSemanticIsSame() throws Throwable {
        when(joinPoint.getArgs()).thenReturn(new Object[]{"{\"a\":1,\"b\":2}"});
        when(joinPoint.proceed()).thenReturn("ok");

        Object firstResult = idempotentAspect.around(joinPoint, idempotent());

        ProceedingJoinPoint secondJoinPoint = mock(ProceedingJoinPoint.class);
        when(secondJoinPoint.getSignature()).thenReturn(signature);
        when(secondJoinPoint.getArgs()).thenReturn(new Object[]{"{ \n\"b\": 2,\n\"a\":1 }"});

        Object secondResult = idempotentAspect.around(secondJoinPoint, idempotent());

        assertEquals("ok", firstResult);
        assertEquals("ok", secondResult);
        verify(secondJoinPoint, never()).proceed();
    }

    @Test
    void shouldUseSpelKeyWhenSpecified() throws Throwable {
        // SpEL 求值需要真实 Method（-parameters 保留参数名，供 #p0 解析）
        when(signature.getMethod()).thenReturn(spelTargetMethod());
        when(joinPoint.getArgs()).thenReturn(new Object[]{"A"});
        when(joinPoint.proceed()).thenReturn("ok-A");

        Object firstResult = idempotentAspect.around(joinPoint, idempotent(60, "#p0"));

        ProceedingJoinPoint secondJoinPoint = mock(ProceedingJoinPoint.class);
        when(secondJoinPoint.getSignature()).thenReturn(signature);
        when(secondJoinPoint.getArgs()).thenReturn(new Object[]{"B"});
        when(secondJoinPoint.proceed()).thenReturn("ok-B");

        Object secondResult = idempotentAspect.around(secondJoinPoint, idempotent(60, "#p0"));

        assertEquals("ok-A", firstResult);
        assertEquals("ok-B", secondResult);
        verify(secondJoinPoint).proceed();
    }

    @Test
    void shouldUseArgsHashWithDefaultMethodKey() throws Throwable {
        when(joinPoint.getArgs()).thenReturn(new Object[]{"X"});
        when(joinPoint.proceed()).thenReturn("ok-X");

        Object firstResult = idempotentAspect.around(joinPoint, idempotent());

        ProceedingJoinPoint secondJoinPoint = mock(ProceedingJoinPoint.class);
        when(secondJoinPoint.getSignature()).thenReturn(signature);
        when(secondJoinPoint.getArgs()).thenReturn(new Object[]{"Y"});
        when(secondJoinPoint.proceed()).thenReturn("ok-Y");

        Object secondResult = idempotentAspect.around(secondJoinPoint, idempotent());

        assertEquals("ok-X", firstResult);
        assertEquals("ok-Y", secondResult);
        verify(secondJoinPoint).proceed();
    }

    @Test
    void shouldDeleteProcessingRecordWhenBusinessThrows() throws Throwable {
        when(joinPoint.getArgs()).thenReturn(new Object[]{"{\"a\":1}"});
        when(joinPoint.proceed()).thenThrow(new IllegalStateException("boom"));

        assertThrows(IllegalStateException.class, () -> idempotentAspect.around(joinPoint, idempotent()));
        assertTrue(redisStore.isEmpty());
    }

    @Test
    void shouldThrowTooManyRequestWhenProcessingNotDoneWithinTimeout() {
        when(valueOperations.setIfAbsent(anyString(), any(), any(Duration.class))).thenReturn(false);
        when(valueOperations.get(anyString())).thenReturn(IdempotentRecord.processing());
        when(joinPoint.getArgs()).thenReturn(new Object[]{"{\"a\":1}"});

        assertThrows(TooManyRequestException.class,
                () -> idempotentAspect.around(joinPoint, idempotent(60)));
    }

    @Test
    void shouldExecuteWhenRecordExpiresInRaceWindow() throws Throwable {
        // setIfAbsent 首次返回 false（旧记录刚存在），get 返回 null（记录已过期），
        // 视为新请求重新抢占成功后执行业务，而非误报请求过频。
        when(valueOperations.setIfAbsent(anyString(), any(), any(Duration.class)))
                .thenReturn(false)
                .thenReturn(true);
        when(valueOperations.get(anyString())).thenReturn(null);
        when(joinPoint.getArgs()).thenReturn(new Object[]{"{\"a\":1}"});
        when(joinPoint.proceed()).thenReturn("fresh");

        Object result = idempotentAspect.around(joinPoint, idempotent());

        assertEquals("fresh", result);
        verify(joinPoint).proceed();
    }

    /**
     * 提供真实 Method 供 SpEL #p0 参数名解析（依赖 -parameters 编译参数）。
     */
    private static Method spelTargetMethod() throws NoSuchMethodException {
        return IdempotentAspectTest.class.getDeclaredMethod("spelTarget", String.class);
    }

    private static String spelTarget(String p0) {
        return p0;
    }

    private Idempotent idempotent() {
        return idempotent(60, "");
    }

    private Idempotent idempotent(long ttlSeconds) {
        return idempotent(ttlSeconds, "");
    }

    private Idempotent idempotent(long ttlSeconds, String key) {
        return new Idempotent() {
            @Override
            public String key() {
                return key;
            }

            @Override
            public long ttlSeconds() {
                return ttlSeconds;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Idempotent.class;
            }
        };
    }
}
