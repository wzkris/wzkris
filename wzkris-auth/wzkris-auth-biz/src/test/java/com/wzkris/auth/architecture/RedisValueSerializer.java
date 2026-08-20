package com.wzkris.auth.architecture;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wzkris.auth.domain.UserContext;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.*;
import com.wzkris.common.redis.util.RedisJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class RedisValueSerializer {

    /**
     * 值序列化器：UserContext 转成 JSON 字符串后，由 StringRedisSerializer 完成字节级转换。
     */
    private final StringRedisSerializer serializer = new StringRedisSerializer();

    /**
     * 值序列化用 String：UserContext 先被 ObjectMapper 转成 JSON 字符串，
     * 再经 StringRedisSerializer 完成字节级 round-trip。
     * <p>
     * 反序列化时显式指定 UserContext.class，嵌套类型由对象结构决定，无需
     * GenericJackson2JsonRedisSerializer 那种 DefaultTyping 类型标识。
     */
    @Test
    void userContextRoundTrip() throws Exception {
        UserContext original = mockUserContext();

        // 对象 -> JSON 字符串（值序列化入口）
        String json = createObjectMapper().writeValueAsString(original);
        log.info("json:{}", json);
        // String 序列化器：字符串 -> 字节 -> 字符串
        byte[] bytes = serializer.serialize(json);
        String roundTrippedJson = serializer.deserialize(bytes);
        assertEquals(json, roundTrippedJson);

        // JSON 字符串 -> 对象
        UserContext deserialized = createObjectMapper().readValue(roundTrippedJson, UserContext.class);

        DefaultLoginUser loginUser = deserialized.loginUser();
        assertEquals(10001L, loginUser.getUid());
        assertEquals(AuthTypeEnum.ADMIN, loginUser.getAuthType());
        assertEquals("admin", loginUser.getName());
        assertEquals("admin-hint", loginUser.getHint());
        assertEquals(original.loginUser().getUserExpiredTime(), loginUser.getUserExpiredTime());
        assertEquals(20002L, loginUser.getActor().getUid());
        assertEquals(AuthTypeEnum.CUSTOMER, loginUser.getActor().getAuthType());

        RoleContext roleContext = deserialized.roleContext();
        assertTrue(roleContext.isSuperUser());
        assertEquals(1, roleContext.getRoles().size());

        UserRole role = roleContext.getRoles().get(0);
        assertEquals(1L, role.getId());
        assertEquals("admin", role.getName());
        assertEquals("ALL", role.getDataScope());
        assertEquals(List.of("user:page", "user:save"), role.getPermissions());
        assertEquals(1, role.getDataIdentityList().size());
        assertEquals("dept", role.getDataIdentityList().get(0).getName());
    }

    /**
     * 泛型集合（List&lt;X&gt;）因类型擦除无法用 Class 表达元素类型，需 TypeReference 还原。
     * 覆盖 RedisJsonUtil.parse 的两条路径：JSON 字符串 + 自然类型（ArrayList&lt;LinkedHashMap&gt;，
     * 即 GenericJackson2Json 反序列化后实际交给 RedisJsonUtil 的形态）。
     */
    @Test
    void genericListRoundTrip() throws Exception {
        List<UserRole> original = List.of(
                new UserRole(1L, "admin", "ALL", List.of(new DataIdentity(101L, "dept")), List.of("user:page")),
                new UserRole(2L, "staff", "SELF", List.of(), List.of("order:page"))
        );

        String json = createObjectMapper().writeValueAsString(original);
        byte[] bytes = serializer.serialize(json);
        String roundTrippedJson = serializer.deserialize(bytes);
        // 模拟真实 RedisTemplate 读回的自然类型：ArrayList<LinkedHashMap>
        Object naturalValue = createObjectMapper().readValue(roundTrippedJson, Object.class);

        List<UserRole> fromString = RedisJsonUtil.parse(roundTrippedJson, new TypeReference<List<UserRole>>() {});
        List<UserRole> fromNatural = RedisJsonUtil.parse(naturalValue, new TypeReference<List<UserRole>>() {});

        assertEquals(original, fromString);
        assertEquals(original, fromNatural);
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    private static UserContext mockUserContext() {
        DefaultLoginUser loginUser = new DefaultLoginUser();
        loginUser.setUid(10001L);
        loginUser.setAuthType(AuthTypeEnum.ADMIN);
        loginUser.setName("admin");
        loginUser.setHint("admin-hint");
        loginUser.setUserExpiredTime(Instant.parse("2026-12-31T23:59:59Z"));
        loginUser.setActor(ActorInfo.of(20002L, AuthTypeEnum.CUSTOMER, "sid-1"));

        UserRole role = new UserRole(
                1L,
                "admin",
                "ALL",
                List.of(new DataIdentity(101L, "dept")),
                List.of("user:page", "user:save")
        );

        RoleContext roleContext = new RoleContext(List.of(role));
        roleContext.setSuperUser(true);

        return new UserContext(loginUser, roleContext);
    }

}
