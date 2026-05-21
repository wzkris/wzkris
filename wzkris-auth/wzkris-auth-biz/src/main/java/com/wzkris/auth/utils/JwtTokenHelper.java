package com.wzkris.auth.utils;

import com.wzkris.auth.domain.TokenClaims;
import com.wzkris.common.core.constant.JwtClaimConstants;
import com.wzkris.common.core.enums.AuthTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtTokenHelper {

    private static final JwsHeader JWS_HEADER = JwsHeader.with(SignatureAlgorithm.RS256).build();

    private static final Set<String> RESERVED_CLAIM_NAMES = Set.of(
            JwtClaimNames.ISS, JwtClaimNames.SUB, JwtClaimNames.IAT,
            JwtClaimNames.EXP, JwtClaimNames.NBF, JwtClaimNames.JTI);

    private final JwtEncoder jwtEncoder;

    private final JwtDecoder jwtDecoder;

    private final AuthorizationServerSettings authorizationServerSettings;

    public TokenClaims parse(String token) {
        return TokenClaims.from(jwtDecoder.decode(token));
    }

    public String encodeLoginToken(int ttlSeconds, Long uid, String sid, AuthTypeEnum authType) {
        Instant issuedAt = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(authorizationServerSettings.getIssuer())
                .subject(uid.toString())
                .claim(JwtClaimConstants.SID, sid)
                .claim(JwtClaimConstants.AUTH_TYPE, authType.getValue())
                .issuedAt(issuedAt)
                .expiresAt(issuedAt.plusSeconds(ttlSeconds))
                .notBefore(issuedAt)
                .id(UUID.randomUUID().toString())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(JWS_HEADER, claims)).getTokenValue();
    }

    public String issueServiceToken(String subject, Map<String, Object> customClaims, long ttlSeconds) {
        Instant issuedAt = Instant.now();
        JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
                .issuer(authorizationServerSettings.getIssuer())
                .subject(subject)
                .issuedAt(issuedAt)
                .expiresAt(issuedAt.plusSeconds(ttlSeconds))
                .notBefore(issuedAt)
                .id(UUID.randomUUID().toString());
        if (customClaims != null) {
            customClaims.forEach((name, value) -> {
                if (!RESERVED_CLAIM_NAMES.contains(name)) {
                    builder.claim(name, value);
                }
            });
        }
        return jwtEncoder.encode(JwtEncoderParameters.from(JWS_HEADER, builder.build())).getTokenValue();
    }

}
