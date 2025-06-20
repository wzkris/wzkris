package com.wzkris.common.security.oauth2.repository;

import com.wzkris.auth.rmi.RmiTokenFeign;
import com.wzkris.auth.rmi.domain.req.TokenReq;
import com.wzkris.auth.rmi.domain.resp.TokenResponse;
import com.wzkris.common.core.constant.HeaderConstants;
import com.wzkris.common.core.domain.CorePrincipal;
import com.wzkris.common.core.utils.StringUtil;
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
public class RmiSecurityContextRepository implements SecurityContextRepository {

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

    private final RmiTokenFeign rmiTokenFeign;

    public RmiSecurityContextRepository(RmiTokenFeign rmiTokenFeign) {
        this.rmiTokenFeign = rmiTokenFeign;
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

        String userToken = request.getHeader(HeaderConstants.X_USER_TOKEN);
        if (StringUtil.isBlank(userToken)) {
            return ctx;
        }

        TokenResponse tokenResponse = rmiTokenFeign.checkUserToken(new TokenReq(userToken));
        if (tokenResponse.isSuccess()) {
            ctx.setAuthentication(createAuthentication(tokenResponse.getPrincipal(), request));
        }
        return ctx;
    }

    protected Authentication createAuthentication(Object principal, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, "",
                AuthorityUtils.createAuthorityList(((CorePrincipal) principal).getPermissions()));
        token.setDetails(this.authenticationDetailsSource.buildDetails(request));
        return token;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        // Empty 中心化认证架构不允许其他服务持久化安全上下文
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        return securityContextHolderStrategy.getContext().getAuthentication() != null;
    }

    /**
     * Copy from @org.springframework.security.web.context.SupplierDeferredSecurityContext
     * Because it is final class
     */
    @Slf4j
    static class SupplierDeferredSecurityContext implements DeferredSecurityContext {

        private final Supplier<SecurityContext> supplier;

        private final SecurityContextHolderStrategy strategy;

        private SecurityContext securityContext;

        private boolean missingContext;

        SupplierDeferredSecurityContext(Supplier<SecurityContext> supplier, SecurityContextHolderStrategy strategy) {
            this.supplier = supplier;
            this.strategy = strategy;
        }

        @Override
        public SecurityContext get() {
            init();
            return this.securityContext;
        }

        @Override
        public boolean isGenerated() {
            init();
            return this.missingContext;
        }

        private void init() {
            if (this.securityContext != null) {
                return;
            }

            this.securityContext = this.supplier.get();
            this.missingContext = (this.securityContext == null);
            if (this.missingContext) {
                this.securityContext = this.strategy.createEmptyContext();
                if (log.isTraceEnabled()) {
                    log.trace("Created {}", this.securityContext);
                }
            }
        }

    }

}
