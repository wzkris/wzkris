package com.wzkris.common.security.oauth2.repository;

import com.wzkris.auth.rmi.RmiTokenFeign;
import com.wzkris.auth.rmi.domain.ClientUser;
import com.wzkris.auth.rmi.domain.req.TokenReq;
import com.wzkris.auth.rmi.domain.resp.TokenResponse;
import com.wzkris.common.core.constant.HeaderConstants;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.domain.CorePrincipal;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.model.DeferredClientUser;
import com.wzkris.common.security.model.SupplierDeferredSecurityContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.DeferredSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;

import java.util.function.Supplier;

/**
 * 原SecurityContextRepository支持http session，
 * 作为纯API服务，禁止http session管理，重写此类以支持中心化的认证架构
 *
 * @author wzkris
 * @date 2025/06/19 15:40
 */
@Slf4j
public final class RmiSecurityContextRepository implements SecurityContextRepository {

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

    private final RmiTokenFeign rmiTokenFeign;

    private final JwtDecoder jwtDecoder;

    public RmiSecurityContextRepository(RmiTokenFeign rmiTokenFeign, JwtDecoder jwtDecoder) {
        this.rmiTokenFeign = rmiTokenFeign;
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        return loadDeferredContext(requestResponseHolder.getRequest()).get();
    }

    @Override
    public DeferredSecurityContext loadDeferredContext(HttpServletRequest request) {
        Supplier<SecurityContext> supplier = () -> readSecurityContextFromRequest(request);
        return new SupplierDeferredSecurityContext(supplier, this.securityContextHolderStrategy);
    }

    private SecurityContext readSecurityContextFromRequest(HttpServletRequest request) {
        SecurityContext ctx = SecurityContextHolder.createEmptyContext();

        final String tenantToken = request.getHeader(HeaderConstants.X_TENANT_TOKEN);
        if (StringUtil.isNotBlank(tenantToken)) {
            generateTenantToken(request, tenantToken, ctx);
            return ctx;
        }

        final String userToken = request.getHeader(HeaderConstants.X_USER_TOKEN);
        if (StringUtil.isNotBlank(userToken)) {
            generateUserToken(request, userToken, ctx);
            return ctx;
        }

        return ctx;
    }

    private void generateTenantToken(HttpServletRequest request, String token, SecurityContext ctx) {
        TokenResponse tokenResponse = rmiTokenFeign.checkUserToken(new TokenReq(token));
        if (tokenResponse.isSuccess()) {
            ctx.setAuthentication(createAuthentication(tokenResponse.getPrincipal(), request, token));
        }
    }

    private void generateUserToken(HttpServletRequest request, String token, SecurityContext ctx) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            String sub = jwt.getClaimAsString(JwtClaimNames.SUB);
            Long userId = sub == null ? SecurityConstants.DEFAULT_USER_ID : Long.valueOf(sub);
            Supplier<ClientUser> supplier = () -> {
                TokenResponse tokenResponse = rmiTokenFeign.checkUserToken(new TokenReq(token));
                if (tokenResponse.isSuccess()) {
                    return (ClientUser) tokenResponse.getPrincipal();
                }
                return null;
            };
            ctx.setAuthentication(createAuthentication(new DeferredClientUser(userId, supplier), request, token));
        } catch (JwtException ignored) {
        }
    }

    private Authentication createAuthentication(CorePrincipal principal, HttpServletRequest request, String token) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principal, token,
                AuthorityUtils.createAuthorityList((principal).getPermissions()));
        authenticationToken.setDetails(this.authenticationDetailsSource.buildDetails(request));
        return authenticationToken;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        // Empty 中心化认证架构不允许其他服务持久化安全上下文
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        return securityContextHolderStrategy.getContext().getAuthentication() != null;
    }

}

