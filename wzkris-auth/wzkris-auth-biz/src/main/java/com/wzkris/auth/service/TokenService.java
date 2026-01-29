package com.wzkris.auth.service;

import com.wzkris.auth.domain.OnlineSession;
import com.wzkris.auth.enums.TokenLuaScriptsEnums;
import com.wzkris.auth.properties.TokenProperties;
import com.wzkris.common.core.model.LoginUser;
import com.wzkris.common.core.utils.ServletUtil;
import com.wzkris.common.web.utils.UserAgentUtil;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.useragent.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * token操作
 *
 * @author wzkris
 */
@Slf4j
@Component
public class TokenService {

    /**
     * 用户信息Hash中的用户字段名
     */
    private static final String HASH_FIELD_USER = "loginUser";

    /**
     * 用户信息Hash中的权限字段名
     */
    private static final String HASH_FIELD_PERMISSIONS = "permissions";

    private final StringKeyGenerator tokenGenerator = new Base64StringKeyGenerator(Base64.getUrlEncoder().withoutPadding(), 96);

    @Autowired
    private TokenProperties tokenProperties;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private AuthorizationServerSettings authorizationServerSettings;

    public String generateRefreshToken() {
        return tokenGenerator.generateKey();
    }

    /**
     * 生成 Access Token
     * 统一使用 JWT 格式，仅封装 uid 和 iss
     * <p>
     * 注意：kid（Key ID）需要手动设置。NimbusJwtEncoder 不会自动从 JWK 中提取 kid，
     * 因此通过 CurrentKeyIdProvider 从缓存的 JWK 中获取 kid 并显式设置到 JWT header 中。
     * </p>
     *
     * @param loginUser 用户信息
     * @return Access Token（JWT格式）
     */
    @Nullable
    public String generateAccessToken(LoginUser loginUser) {
        Serializable uid = loginUser.getUid();
        JwsAlgorithm jwsAlgorithm = SignatureAlgorithm.RS256;

        JwsHeader jwsHeader = JwsHeader.with(jwsAlgorithm)
                .build();

        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(Duration.ofSeconds(tokenProperties.getAccessTokenTimeOut()));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(uid.toString())  // 仅包含 uid
                .issuer(authorizationServerSettings.getIssuer())  // 添加 issuer，网关会校验
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .id(UUID.randomUUID().toString())
                .notBefore(issuedAt)
                .build();
        Jwt jwt = this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims));
        return jwt.getTokenValue();
    }

    /**
     * 保存token及用户信息
     * 存储结构：
     * - refreshToken -> uid (映射)
     * - uid -> Hash(user, permissions) (用户信息和权限)
     * - session:{type:uid} -> Map<refreshToken, OnlineSession> (在线会话)
     * 注意：refreshTokenToUidKey 单独保存，不在 Lua 脚本中，避免 CROSSSLOT 错误
     *
     * @param loginUser    用户信息
     * @param refreshToken refresh token
     * @param permissions  权限信息
     */
    public final void save(LoginUser loginUser, String refreshToken, Set<String> permissions) {
        Serializable uid = loginUser.getUid();
        String type = loginUser.getAuthType().getValue();
        long refreshTTL = tokenProperties.getRefreshTokenTimeOut();

        OnlineSession onlineSession = buildOnlineSession();
        String refreshTokenToUidKey = TokenKeyBuilder.buildRefreshTokenToUidKey(type, refreshToken);
        String userInfoKey = TokenKeyBuilder.buildUserInfoKey(type, uid);
        String sessionKey = TokenKeyBuilder.buildSessionKey(type, uid);

        // 1. 先单独保存 refreshTokenToUidKey（
        redisTemplate.opsForValue().set(refreshTokenToUidKey, uid.toString(), Duration.ofSeconds(refreshTTL));

        // 2. 使用 Lua 脚本原子化保存 userInfo 和 session（两者都使用 {type:uid} hash tag）
        DefaultRedisScript<Long> script = new DefaultRedisScript<>(TokenLuaScriptsEnums.SAVE_TOKEN_AND_USER_INFO.getScript(), Long.class);
        List<String> keys = Arrays.asList(userInfoKey, sessionKey);
        List<Object> args = Arrays.asList(refreshToken, loginUser, permissions, onlineSession, refreshTTL);

        Long result = redisTemplate.execute(script, keys, args.toArray());
        if (result == null || result == 0) {
            log.error("Failed to save token and user info for user: {}", uid);
        }
    }

    /**
     * 根据 uid 获取用户信息
     * 网关验证 JWT 后，解析出 uid，然后调用此方法获取用户信息
     *
     * @param type 认证类型
     * @param uid  用户ID
     * @return 用户信息，如果不存在则返回null
     */
    @Nullable
    public final LoginUser loadByUid(String type, Serializable uid) {
        String userInfoKey = TokenKeyBuilder.buildUserInfoKey(type, uid);
        return (LoginUser) redisTemplate.opsForHash().get(userInfoKey, HASH_FIELD_USER);
    }

    /**
     * 根据 uid 获取权限信息
     *
     * @param type 认证类型
     * @param uid  用户ID
     * @return 权限信息，如果不存在则返回null
     */
    @Nullable
    public final Set<String> loadPermissionsByUid(String type, Serializable uid) {
        String userInfoKey = TokenKeyBuilder.buildUserInfoKey(type, uid);
        Object permissionsObj = redisTemplate.opsForHash().get(userInfoKey, HASH_FIELD_PERMISSIONS);
        return permissionsObj instanceof Set ? (Set<String>) permissionsObj : null;
    }

    @Nullable
    private Serializable getUidByRefreshToken(String type, String refreshToken) {
        String refreshTokenToUidKey = TokenKeyBuilder.buildRefreshTokenToUidKey(type, refreshToken);
        Object uidObj = redisTemplate.opsForValue().get(refreshTokenToUidKey);
        return uidObj instanceof Serializable ? (Serializable) uidObj : null;
    }

    /**
     * 根据refreshToken获取用户信息
     * 查询路径：refreshToken -> uid -> Hash(user)
     *
     * @param type         认证类型
     * @param refreshToken refresh token
     * @return 用户信息，如果不存在则返回null
     */
    @Nullable
    public final LoginUser loadByRefreshToken(String type, String refreshToken) {
        Serializable uid = getUidByRefreshToken(type, refreshToken);
        if (uid == null) {
            return null;
        }

        return loadByUid(type, uid);
    }

    /**
     * 根据refreshToken获取权限信息
     * 查询路径：refreshToken -> uid -> Hash(permissions)
     *
     * @param type         认证类型
     * @param refreshToken refresh token
     * @return 权限信息，如果不存在则返回null
     */
    @Nullable
    public final Set<String> loadPermissionsByRefreshToken(String type, String refreshToken) {
        Serializable uid = getUidByRefreshToken(type, refreshToken);
        if (uid == null) {
            return null;
        }

        return loadPermissionsByUid(type, uid);
    }

    /**
     * 根据refreshToken移除信息
     * 注意：refreshTokenToUidKey 单独删除，不在 Lua 脚本中，避免 CROSSSLOT 错误
     *
     * @param type         认证类型
     * @param refreshToken refresh token
     * @return 用户ID，如果不存在则返回null
     */
    public final Serializable logoutByRefreshToken(String type, String refreshToken) {
        Serializable uid = getUidByRefreshToken(type, refreshToken);
        if (uid == null) {
            return null;
        }

        String refreshTokenToUidKey = TokenKeyBuilder.buildRefreshTokenToUidKey(type, refreshToken);
        String userInfoKey = TokenKeyBuilder.buildUserInfoKey(type, uid);
        String sessionKey = TokenKeyBuilder.buildSessionKey(type, uid);

        // 1. 先单独删除 refreshTokenToUidKey（
        redisTemplate.delete(refreshTokenToUidKey);

        // 2. 使用 Lua 脚本原子化删除 userInfo 和 session（两者都使用 {type:uid} hash tag）
        DefaultRedisScript<Long> script = new DefaultRedisScript<>(TokenLuaScriptsEnums.LOGOUT_BY_REFRESH_TOKEN.getScript(), Long.class);
        List<String> keys = Arrays.asList(userInfoKey, sessionKey);
        Long result = redisTemplate.execute(script, keys, refreshToken);

        if (result == null || result == 0) {
            log.warn("Failed to logout for refreshToken: {}", refreshToken);
        }

        return uid;
    }

    private OnlineSession buildOnlineSession() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String clientIP = ServletUtil.getClientIP(request);
            UserAgent.ImmutableUserAgent userAgent = UserAgentUtil.INSTANCE.parse(request.getHeader(HttpHeaders.USER_AGENT));

            OnlineSession onlineSession = new OnlineSession();
            onlineSession.setDevice(userAgent.getValue(UserAgent.DEVICE_NAME));
            onlineSession.setDeviceBrand(userAgent.getValue(UserAgent.DEVICE_BRAND));
            onlineSession.setLoginIp(clientIP);
            onlineSession.setBrowser(userAgent.getValue(UserAgent.AGENT_NAME));
            onlineSession.setOs(userAgent.getValue(UserAgent.OPERATING_SYSTEM_NAME));
            onlineSession.setLoginTime(new Date());
            return onlineSession;
        } catch (Exception e) {
            return new OnlineSession();
        }
    }

    /**
     * 根据用户ID获取在线会话列表
     *
     * @param type 认证类型
     * @param uid  用户ID
     * @return 在线会话Map
     */
    public final Map<String, OnlineSession> loadSessionCache(String type, Serializable uid) {
        String sessionKey = TokenKeyBuilder.buildSessionKey(type, uid);
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(sessionKey);
        Map<String, OnlineSession> result = new HashMap<>();
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            result.put((String) entry.getKey(), (OnlineSession) entry.getValue());
        }
        return result;
    }

}
