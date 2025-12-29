package com.wzkris.auth.service;

import com.wzkris.auth.config.TokenProperties;
import com.wzkris.auth.domain.OnlineSession;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.UserPrincipal;
import com.wzkris.common.core.utils.ServletUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.redis.util.RedisUtil;
import com.wzkris.common.web.utils.UserAgentUtil;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import nl.basjes.parse.useragent.UserAgent;
import org.redisson.api.RMapCache;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * token操作
 *
 * @author wzkris
 */
@Component
public class TokenService {

    /**
     * 在线会话Key前缀
     */
    private final String SESSION_PREFIX = "auth-token:%s:session:{%s}";

    /**
     * Access Token Key前缀
     */
    private final String ACCESS_TOKEN_PREFIX = "auth-token:%s:access:%s";

    /**
     * Refresh Token Key前缀
     */
    private final String REFRESH_TOKEN_PREFIX = "auth-token:%s:info:%s";

    private final StringKeyGenerator tokenGenerator = new Base64StringKeyGenerator(Base64.getUrlEncoder().withoutPadding(), 96);

    @Autowired
    private TokenProperties tokenProperties;

    @Autowired
    private JwtEncoder jwtEncoder;

    public String generateToken() {
        return tokenGenerator.generateKey();
    }

    @Nullable
    public String generateAccessToken(UserPrincipal principal) {
        if (StringUtil.equalsAny(principal.getType(), AuthTypeEnum.ADMIN.getValue(), AuthTypeEnum.TENANT.getValue())) {
            return this.generateToken();
        } else if (StringUtil.equals(principal.getType(), AuthTypeEnum.CUSTOMER.getValue())) {
            JwsAlgorithm jwsAlgorithm = SignatureAlgorithm.RS256;
            JwsHeader jwsHeader = JwsHeader.with(jwsAlgorithm)
                    .build();
            Instant issuedAt = Instant.now();
            Instant expiresAt = issuedAt.plus(Duration.ofSeconds(tokenProperties.getAccessTokenTimeOut()));
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .subject(principal.getId().toString())
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
     * 构建Refresh Token Key
     *
     * @param refreshToken Refresh Token
     * @return Key
     */
    private String buildRefreshTokenKey(String type, String refreshToken) {
        return REFRESH_TOKEN_PREFIX.formatted(type, refreshToken);
    }

    /**
     * 保存token及用户信息
     * 存储结构：
     * - accessToken -> refreshToken (字符串)
     * - refreshToken -> principal (用户信息)
     * - session:{userId} -> Map<refreshToken, OnlineSession> (在线会话)
     *
     * @param principal    用户信息
     * @param accessToken  access token
     * @param refreshToken refresh token
     */
    public final void save(UserPrincipal principal, String accessToken, String refreshToken) {
        Serializable id = principal.getId();
        String type = principal.getType();
        long refreshTTL = tokenProperties.getRefreshTokenTimeOut();
        long accessTTL = tokenProperties.getAccessTokenTimeOut();

        // ========== 1️⃣ 写入在线会话 ==========

        RMapCache<String, OnlineSession> onlineCache = loadSessionCache(type, id);
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

            if (onlineCache.containsKey(refreshToken)) {
                // 已存在则仅刷新过期时间
                onlineCache.expireEntry(refreshToken, Duration.ofSeconds(refreshTTL), Duration.ZERO);
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
                onlineCache.put(refreshToken, onlineSession, refreshTTL, TimeUnit.SECONDS);
            }
        } catch (Exception e) {// 不存在请求
            onlineCache.put(refreshToken, new OnlineSession(), refreshTTL, TimeUnit.SECONDS);
        }

        // ========== 2️⃣ 处理Token映射（不同分片，单独操作） ==========
        // Access Token -> Refresh Token (存储字符串)
        RedisUtil.setObj(buildAccessTokenKey(type, accessToken), refreshToken, accessTTL);

        // Refresh Token -> principal (直接存储用户信息)
        RedisUtil.setObj(buildRefreshTokenKey(type, refreshToken), principal, refreshTTL);
    }

    /**
     * 根据accessToken获取用户信息
     * 查询路径：accessToken -> refreshToken -> principal
     *
     * @param accessToken access token
     * @return 用户信息，如果不存在则返回null
     */
    @Nullable
    public final UserPrincipal loadByAccessToken(String type, String accessToken) {
        // 1. 通过accessToken获取refreshToken
        String refreshToken = loadRefreshTokenByAccessToken(type, accessToken);
        if (refreshToken == null) {
            return null;
        }

        // 2. 通过refreshToken获取用户信息
        return loadByRefreshToken(type, refreshToken);
    }

    /**
     * 根据refreshToken获取用户信息
     * 查询路径：refreshToken -> principal（直接获取）
     *
     * @param refreshToken refresh token
     * @return 用户信息，如果不存在则返回null
     */
    @Nullable
    public final UserPrincipal loadByRefreshToken(String type, String refreshToken) {
        return RedisUtil.getObj(buildRefreshTokenKey(type, refreshToken), UserPrincipal.class);
    }

    /**
     * 通过accessToken获取refreshToken
     *
     * @param accessToken access token
     * @return refreshToken，如果不存在则返回null
     */
    @Nullable
    public final String loadRefreshTokenByAccessToken(String type, String accessToken) {
        return RedisUtil.getObj(buildAccessTokenKey(type, accessToken), String.class);
    }

    /**
     * 根据accessToken移除信息
     *
     * @param accessToken access token
     * @return 用户ID，如果不存在则返回null
     */
    @Nullable
    public final Serializable logoutByAccessToken(String type, String accessToken) {
        // 1. 通过accessToken获取refreshToken
        String refreshToken = loadRefreshTokenByAccessToken(type, accessToken);

        if (refreshToken == null) {
            RedisUtil.delObj(buildAccessTokenKey(type, accessToken));
            return null;
        }

        return logoutByRefreshToken(type, refreshToken);
    }

    /**
     * 根据refreshToken移除信息
     *
     * @param refreshToken refresh token
     */
    public final Serializable logoutByRefreshToken(String type, String refreshToken) {
        // 通过refreshToken获取用户信息
        UserPrincipal principal = loadByRefreshToken(type, refreshToken);
        if (principal == null) {
            return null;
        }

        Serializable id = principal.getId();

        // 删除refreshToken映射
        RedisUtil.delObj(buildRefreshTokenKey(type, refreshToken));

        // 删除在线会话
        RMapCache<String, OnlineSession> onlineCache = loadSessionCache(type, id);
        onlineCache.remove(refreshToken);

        if (onlineCache.isEmpty()) {
            onlineCache.delete();
        }

        return id;
    }

    /**
     * 根据用户ID获取在线会话列表
     *
     * @param id 用户ID
     */
    public final RMapCache<String, OnlineSession> loadSessionCache(String type, Serializable id) {
        return RedisUtil.getRMapCache(buildSessionKey(type, id), String.class, OnlineSession.class);
    }

}
