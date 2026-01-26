package com.wzkris.auth.service;

import com.wzkris.auth.domain.OnlineSession;
import com.wzkris.auth.properties.TokenProperties;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.LoginUser;
import com.wzkris.common.core.utils.ServletUtil;
import com.wzkris.common.web.utils.UserAgentUtil;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import nl.basjes.parse.useragent.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
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

    /**
     * 在线会话Key前缀
     */
    private final String SESSION_PREFIX = "auth-token:%s:session:{%s}";

    /**
     * Access Token Key前缀
     */
    private final String ACCESS_TOKEN_PREFIX = "auth-token:%s:access:%s";

    /**
     * Refresh Token Key前缀（用于 refreshToken -> uid 映射）
     */
    private final String REFRESH_TOKEN_PREFIX = "auth-token:%s:refresh:%s";

    /**
     * 用户信息 Hash Key前缀（用于 uid -> Hash(user, permissions)）
     */
    private final String USER_INFO_PREFIX = "auth-token:%s:info:%s";

    private final StringKeyGenerator tokenGenerator = new Base64StringKeyGenerator(Base64.getUrlEncoder().withoutPadding(), 96);

    @Autowired
    private TokenProperties tokenProperties;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public String generateToken() {
        return tokenGenerator.generateKey();
    }

    @Nullable
    public String generateAccessToken(LoginUser loginUser) {
        AuthTypeEnum authType = loginUser.getAuthType();
        if (authType == AuthTypeEnum.ADMIN || authType == AuthTypeEnum.TENANT) {
            return this.generateToken();
        } else if (authType == AuthTypeEnum.CUSTOMER) {
            JwsAlgorithm jwsAlgorithm = SignatureAlgorithm.RS256;
            JwsHeader jwsHeader = JwsHeader.with(jwsAlgorithm)
                    .build();
            Instant issuedAt = Instant.now();
            Instant expiresAt = issuedAt.plus(Duration.ofSeconds(tokenProperties.getAccessTokenTimeOut()));
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .subject(loginUser.getUid().toString())
                    .issuedAt(issuedAt)
                    .expiresAt(expiresAt)
                    .id(UUID.randomUUID().toString())
                    .notBefore(issuedAt)
                    .build();
            Jwt jwt = this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims));
            return jwt.getTokenValue();
        }
        return null;
    }

    /**
     * 构建在线会话Key
     *
     * @param id 用户ID
     * @return Key
     */
    private String buildSessionKey(String type, Serializable id) {
        return SESSION_PREFIX.formatted(type, id);
    }

    /**
     * 构建Access Token Key
     *
     * @param accessToken Access Token
     * @return Key
     */
    private String buildAccessTokenKey(String type, String accessToken) {
        return ACCESS_TOKEN_PREFIX.formatted(type, accessToken);
    }

    /**
     * 构建Refresh Token到UID的映射Key
     *
     * @param refreshToken Refresh Token
     * @return Key
     */
    private String buildRefreshTokenToUidKey(String type, String refreshToken) {
        return REFRESH_TOKEN_PREFIX.formatted(type, refreshToken);
    }

    /**
     * 构建用户信息Hash Key（以uid为key）
     *
     * @param uid 用户ID
     * @return Key
     */
    private String buildUserInfoKey(String type, Serializable uid) {
        return USER_INFO_PREFIX.formatted(type, uid);
    }

    /**
     * 保存token及用户信息
     * 存储结构：
     * - accessToken -> refreshToken (字符串)
     * - refreshToken -> uid (映射)
     * - uid -> Hash(user, permissions) (用户信息和权限)
     * - session:{userId} -> Map<refreshToken, OnlineSession> (在线会话)
     *
     * @param loginUser    用户信息
     * @param accessToken  access token
     * @param refreshToken refresh token
     * @param permissions  权限信息
     */
    public final void save(LoginUser loginUser, String accessToken, String refreshToken, Set<String> permissions) {
        Serializable id = loginUser.getUid();
        String type = loginUser.getAuthType().getValue();
        long refreshTTL = tokenProperties.getRefreshTokenTimeOut();
        long accessTTL = tokenProperties.getAccessTokenTimeOut();

        // ========== 1️⃣ 写入在线会话 ==========
        saveOrUpdateOnlineSession(type, id, refreshToken, refreshTTL);

        // ========== 2️⃣ 处理Token映射 ==========
        redisTemplate.opsForValue().set(buildAccessTokenKey(type, accessToken), refreshToken, Duration.ofSeconds(accessTTL));
        redisTemplate.opsForValue().set(buildRefreshTokenToUidKey(type, refreshToken), id, Duration.ofSeconds(refreshTTL));

        String userInfoKey = buildUserInfoKey(type, id);
        redisTemplate.opsForHash().putAll(userInfoKey, Map.of(HASH_FIELD_USER, loginUser, HASH_FIELD_PERMISSIONS, permissions));

        redisTemplate.expire(userInfoKey, Duration.ofSeconds(refreshTTL));
    }

    /**
     * 根据accessToken获取用户信息
     *
     * @param accessToken access token
     * @return 用户信息，如果不存在则返回null
     */
    @Nullable
    public final LoginUser loadByAccessToken(String type, String accessToken) {
        String refreshToken = loadRefreshTokenByAccessToken(type, accessToken);
        if (refreshToken == null) {
            return null;
        }

        return loadByRefreshToken(type, refreshToken);
    }

    /**
     * 根据refreshToken获取用户ID
     *
     * @param type         认证类型
     * @param refreshToken refresh token
     * @return 用户ID，如果不存在则返回null
     */
    @Nullable
    private Serializable getUidByRefreshToken(String type, String refreshToken) {
        Object uidObj = redisTemplate.opsForValue().get(buildRefreshTokenToUidKey(type, refreshToken));
        return uidObj instanceof Serializable ? (Serializable) uidObj : null;
    }

    /**
     * 刷新会话过期时间
     *
     * @param sessionKey 会话Key
     * @param refreshTTL 过期时间（秒）
     */
    private void refreshSessionExpire(String sessionKey, long refreshTTL) {
        redisTemplate.expire(sessionKey, Duration.ofSeconds(refreshTTL));
    }

    /**
     * 根据refreshToken获取用户信息
     * 查询路径：refreshToken -> uid -> Hash(user)
     *
     * @param refreshToken refresh token
     * @return 用户信息，如果不存在则返回null
     */
    @Nullable
    public final LoginUser loadByRefreshToken(String type, String refreshToken) {
        Serializable uid = getUidByRefreshToken(type, refreshToken);
        if (uid == null) {
            return null;
        }

        return (LoginUser) redisTemplate.opsForHash().get(buildUserInfoKey(type, uid), HASH_FIELD_USER);
    }

    /**
     * 根据refreshToken获取权限信息
     * 查询路径：refreshToken -> uid -> Hash(permissions)
     *
     * @param refreshToken refresh token
     * @return 权限信息，如果不存在则返回null
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public final Set<String> loadPermissionsByRefreshToken(String type, String refreshToken) {
        Serializable uid = getUidByRefreshToken(type, refreshToken);
        if (uid == null) {
            return null;
        }

        Object permissionsObj = redisTemplate.opsForHash().get(buildUserInfoKey(type, uid), HASH_FIELD_PERMISSIONS);
        return permissionsObj instanceof Set ? (Set<String>) permissionsObj : null;
    }

    /**
     * 根据accessToken获取权限信息
     * 查询路径：accessToken -> refreshToken -> uid -> Hash(permissions)
     *
     * @param type        认证类型
     * @param accessToken access token
     * @return 权限信息，如果不存在则返回null
     */
    @Nullable
    public final Set<String> loadPermissionsByAccessToken(String type, String accessToken) {
        String refreshToken = loadRefreshTokenByAccessToken(type, accessToken);
        if (refreshToken == null) {
            return null;
        }
        return loadPermissionsByRefreshToken(type, refreshToken);
    }

    /**
     * 通过accessToken获取refreshToken
     *
     * @param accessToken access token
     * @return refreshToken，如果不存在则返回null
     */
    @Nullable
    public final String loadRefreshTokenByAccessToken(String type, String accessToken) {
        Object value = redisTemplate.opsForValue().get(buildAccessTokenKey(type, accessToken));
        return value instanceof String ? (String) value : null;
    }

    /**
     * 根据accessToken移除信息
     *
     * @param accessToken access token
     * @return 用户ID，如果不存在则返回null
     */
    @Nullable
    public final Serializable logoutByAccessToken(String type, String accessToken) {
        String refreshToken = loadRefreshTokenByAccessToken(type, accessToken);

        redisTemplate.delete(buildAccessTokenKey(type, accessToken));

        if (refreshToken == null) return null;

        return logoutByRefreshToken(type, refreshToken);
    }

    /**
     * 根据refreshToken移除信息
     *
     * @param refreshToken refresh token
     */
    public final Serializable logoutByRefreshToken(String type, String refreshToken) {
        LoginUser loginUser = loadByRefreshToken(type, refreshToken);
        if (loginUser == null) {
            return null;
        }

        Serializable id = loginUser.getUid();

        redisTemplate.delete(buildRefreshTokenToUidKey(type, refreshToken));

        String sessionKey = buildSessionKey(type, id);
        redisTemplate.opsForHash().delete(sessionKey, refreshToken);

        Long size = redisTemplate.opsForHash().size(sessionKey);
        if (size == null || size == 0) {
            redisTemplate.delete(buildUserInfoKey(type, id));
            redisTemplate.delete(sessionKey);
        }

        return id;
    }

    /**
     * 保存或更新在线会话
     *
     * @param type         认证类型
     * @param id           用户ID
     * @param refreshToken refresh token
     * @param refreshTTL   过期时间（秒）
     */
    private void saveOrUpdateOnlineSession(String type, Serializable id, String refreshToken, long refreshTTL) {
        String sessionKey = buildSessionKey(type, id);
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

            if (redisTemplate.opsForHash().hasKey(sessionKey, refreshToken)) {
                // 已存在则仅刷新过期时间
                refreshSessionExpire(sessionKey, refreshTTL);
            } else {
                String clientIP = ServletUtil.getClientIP(request);
                UserAgent.ImmutableUserAgent userAgent = UserAgentUtil.INSTANCE.parse(request.getHeader(HttpHeaders.USER_AGENT));
                OnlineSession onlineSession = new OnlineSession();
                onlineSession.setDevice(userAgent.getValue(UserAgent.DEVICE_NAME));
                onlineSession.setDeviceBrand(userAgent.getValue(UserAgent.DEVICE_BRAND));
                onlineSession.setLoginIp(clientIP);
                onlineSession.setBrowser(userAgent.getValue(UserAgent.AGENT_NAME));
                onlineSession.setOs(userAgent.getValue(UserAgent.OPERATING_SYSTEM_NAME));
                onlineSession.setLoginTime(new Date());

                // 新建会话
                redisTemplate.opsForHash().putIfAbsent(sessionKey, refreshToken, onlineSession);
                refreshSessionExpire(sessionKey, refreshTTL);
            }
        } catch (Exception e) {
            // 不存在请求上下文时，创建空的会话
            redisTemplate.opsForHash().put(sessionKey, refreshToken, new OnlineSession());
            refreshSessionExpire(sessionKey, refreshTTL);
        }
    }

    /**
     * 根据用户ID获取在线会话列表
     *
     * @param id 用户ID
     * @return 在线会话Map
     */
    public final Map<String, OnlineSession> loadSessionCache(String type, Serializable id) {
        String sessionKey = buildSessionKey(type, id);
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(sessionKey);
        Map<String, OnlineSession> result = new HashMap<>();
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            result.put((String) entry.getKey(), (OnlineSession) entry.getValue());
        }
        return result;
    }

}
