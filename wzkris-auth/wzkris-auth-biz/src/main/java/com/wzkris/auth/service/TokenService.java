package com.wzkris.auth.service;

import com.wzkris.auth.domain.OnlineSession;
import com.wzkris.auth.properties.TokenProperties;
import com.wzkris.common.core.constant.JwtClaimConstants;
import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.core.utils.ServletUtil;
import com.wzkris.common.web.utils.UserAgentUtil;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.useragent.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.http.HttpHeaders;
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
import java.util.concurrent.TimeUnit;

/**
 * Token 操作服务，负责生成/解析/存储 JWT 及 Redis 会话。
 *
 * @author wzkris
 */
@Slf4j
@Component
public class TokenService {

    /**
     * 用户信息 Hash 字段：用户对象
     */
    private static final String HASH_FIELD_USER = "loginUser";

    /**
     * 用户信息 Hash 字段：权限集合
     */
    private static final String HASH_FIELD_PERMISSIONS = "permissions";

    @Autowired
    private TokenProperties tokenProperties;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private AuthorizationServerSettings authorizationServerSettings;

    /**
     * 生成 Access Token（JWT，含 uid、sid）。
     *
     * @param uid      用户ID
     * @param sid      会话ID
     * @param authType 认证类型（用于下游鉴别用户类型）
     * @return Access Token（JWT），失败返回 null
     */
    @Nullable
    public String generateAccessToken(Long uid, String sid, String authType) {
        Jwt jwt = getJwt(tokenProperties.getAccessTokenTimeOut(), uid, sid, authType);
        return jwt.getTokenValue();
    }

    @Nullable
    public String generateAccessToken(BaseLoginUser baseLoginUser, String sid) {
        return generateAccessToken(baseLoginUser.getUid(), sid, baseLoginUser.getAuthType().getValue());
    }

    /**
     * 生成 Refresh Token（JWT，与 accessToken 同 sid）。
     *
     * @param uid      用户ID
     * @param sid      会话ID
     * @param authType 认证类型（用于下游鉴别用户类型）
     * @return Refresh Token（JWT），失败返回 null
     */
    @Nullable
    public String generateRefreshToken(Long uid, String sid, String authType) {
        Jwt jwt = getJwt(tokenProperties.getRefreshTokenTimeOut(), uid, sid, authType);
        return jwt.getTokenValue();
    }

    @Nullable
    public String generateRefreshToken(BaseLoginUser baseLoginUser, String sid) {
        return generateRefreshToken(baseLoginUser.getUid(), sid, baseLoginUser.getAuthType().getValue());
    }

    private Jwt getJwt(int timeout, Long uid, String sid, String authType) {
        JwsAlgorithm jwsAlgorithm = SignatureAlgorithm.RS256;

        JwsHeader jwsHeader = JwsHeader.with(jwsAlgorithm)
                .build();

        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(Duration.ofSeconds(timeout));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(uid.toString())  // uid
                .claim(JwtClaimConstants.SID, sid)  // 会话ID，与 accessToken 的 sid 相同
                .claim(JwtClaimConstants.AUTH_TYPE, authType)
                .issuer(authorizationServerSettings.getIssuer())  // 添加 issuer
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .id(UUID.randomUUID().toString())
                .notBefore(issuedAt)
                .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims));
    }

    /**
     * 从 JWT 解析 uid、sid。
     *
     * @param token JWT（access 或 refresh）
     * @return TokenInfo（uid、sid），解析失败返回 null
     */
    public TokenInfo parseJwt(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        String uidStr = jwt.getSubject();
        String sid = jwt.getClaimAsString(JwtClaimConstants.SID);
        Instant exp = jwt.getExpiresAt();
        return new TokenInfo(Long.valueOf(uidStr), sid, exp);
    }

    /**
     * 保存用户信息与会话。
     *
     * @param baseLoginUser 用户信息
     * @param sid           会话ID
     * @param permissions   权限集合
     */
    public final void save(BaseLoginUser baseLoginUser, String sid, Set<String> permissions) {
        Serializable uid = baseLoginUser.getUid();
        String type = baseLoginUser.getAuthType().getValue();
        long refreshTTL = tokenProperties.getRefreshTokenTimeOut();

        OnlineSession onlineSession = buildOnlineSession();
        String userInfoKey = TokenKeyBuilder.buildUserInfoKey(type, uid);
        String sessionKey = TokenKeyBuilder.buildSessionKey(type, uid);
        redisTemplate.execute(new SessionCallback<Void>() {
            @Override
            @SuppressWarnings({"rawtypes", "unchecked"})
            public Void execute(RedisOperations operations) {
                operations.multi();
                operations.opsForHash().put(userInfoKey, HASH_FIELD_USER, baseLoginUser);
                operations.opsForHash().put(userInfoKey, HASH_FIELD_PERMISSIONS, permissions);
                operations.expire(userInfoKey, refreshTTL, TimeUnit.SECONDS);
                operations.opsForHash().put(sessionKey, sid, onlineSession);
                operations.expire(sessionKey, refreshTTL, TimeUnit.SECONDS);
                operations.exec();
                return null;
            }
        });
    }

    /**
     * 按 uid 获取用户信息。
     *
     * @param type 认证类型
     * @param uid  用户ID
     * @return 用户信息，不存在返回 null
     */
    @Nullable
    public final BaseLoginUser loadLoginUserByUid(String type, Serializable uid) {
        String userInfoKey = TokenKeyBuilder.buildUserInfoKey(type, uid);
        return (BaseLoginUser) redisTemplate.opsForHash().get(userInfoKey, HASH_FIELD_USER);
    }

    /**
     * 按 uid 获取权限。
     *
     * @param type 认证类型
     * @param uid  用户ID
     * @return 权限集合，不存在返回 null
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public final Set<String> loadPermissionsByUid(String type, Serializable uid) {
        String userInfoKey = TokenKeyBuilder.buildUserInfoKey(type, uid);
        return (Set<String>) redisTemplate.opsForHash().get(userInfoKey, HASH_FIELD_PERMISSIONS);
    }

    /**
     * 按 sid 移除会话；若无其它会话则删除 userInfo/session key。
     *
     * @param type 认证类型
     * @param uid  用户ID
     * @param sid  会话ID
     */
    public final void revoke(String type, Serializable uid, String sid) {
        String userInfoKey = TokenKeyBuilder.buildUserInfoKey(type, uid);
        String sessionKey = TokenKeyBuilder.buildSessionKey(type, uid);

        Long size = redisTemplate.opsForHash().size(sessionKey);
        if (size > 1) {
            redisTemplate.opsForHash().delete(sessionKey, sid);
        } else {
            redisTemplate.delete(List.of(userInfoKey, sessionKey));
        }
    }

    /**
     * 检查 sid 是否已拉黑（不在会话中则视为已拉黑）。
     *
     * @param type 认证类型
     * @param uid  用户ID
     * @param sid  会话ID
     * @return 已拉黑为 true，否则 false
     */
    public boolean isRevoked(String type, Long uid, String sid) {
        String sessionKey = TokenKeyBuilder.buildSessionKey(type, uid);
        return !redisTemplate.opsForHash().hasKey(sessionKey, sid);
    }

    /**
     * 从当前请求构建 OnlineSession。
     */
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
            log.warn("Failed to build online session, returning empty session: {}", e.getMessage());
            return new OnlineSession();
        }
    }

    /**
     * 按 uid 拉取在线会话 Map（sid -> OnlineSession）。
     *
     * @param type 认证类型
     * @param uid  用户ID
     * @return sid -> OnlineSession，无会话返回空 Map
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

    /**
         * JWT 解析结果（uid、sid）。
         */
        public record TokenInfo(Long uid, String sid, Instant exp) {

    }

}
