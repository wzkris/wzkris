package com.wzkris.auth.security.oauth2.customize;

import com.wzkris.common.core.constant.JwtClaimConstants;
import org.springframework.security.oauth2.core.ClaimAccessor;
import org.springframework.security.oauth2.jwt.JwtClaimAccessor;

import java.util.Map;

/**
 * JWT Claims 统一访问模型，收敛外部直接 getClaim 调用。
 */
public final class TokenClaims implements JwtClaimAccessor {

    private final Map<String, Object> claims;

    private TokenClaims(Map<String, Object> claims) {
        this.claims = claims;
    }

    public static TokenClaims from(ClaimAccessor claimAccessor) {
        return new TokenClaims(claimAccessor.getClaims());
    }

    public Long getUid() {
        return Long.valueOf(this.getSubject());
    }

    public String getSid() {
        return this.getClaimAsString(JwtClaimConstants.SID);
    }

    public String getAuthType() {
        return this.getClaimAsString(JwtClaimConstants.AUTH_TYPE);
    }

    @Override
    public Map<String, Object> getClaims() {
        return this.claims;
    }

}
