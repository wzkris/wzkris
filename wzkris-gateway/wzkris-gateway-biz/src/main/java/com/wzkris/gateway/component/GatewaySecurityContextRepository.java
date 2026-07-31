package com.wzkris.gateway.component;

import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.component.SupplierDeferredSecurityContext;
import com.wzkris.common.security.utils.BearerTokenUtil;
import com.wzkris.gateway.service.TokenValidateService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.DeferredSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * Gateway 专用 SecurityContextRepository。
 *
 * @author wzkris
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GatewaySecurityContextRepository implements SecurityContextRepository {

    private final SecurityContextHolderStrategy contextHolderStrategy =
            SecurityContextHolder.getContextHolderStrategy();

    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource =
            new WebAuthenticationDetailsSource();

    private final TokenValidateService tokenValidateService;

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        return loadDeferredContext(requestResponseHolder.getRequest()).get();
    }

    @Override
    public DeferredSecurityContext loadDeferredContext(HttpServletRequest request) {
        Supplier<SecurityContext> supplier = () -> loadContextInternal(request);
        return new SupplierDeferredSecurityContext(supplier, this.contextHolderStrategy);
    }

    private SecurityContext loadContextInternal(HttpServletRequest request) {
        Authentication authentication = this.tokenValidateService.loadAuthenticationByRequest(request);

        if (authentication instanceof AbstractAuthenticationToken authenticationToken) {
            authenticationToken.setDetails(this.authenticationDetailsSource.buildDetails(request));
        }
        SecurityContext context = this.contextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        // 网关不负责持久化 SecurityContext
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        return StringUtil.isNotBlank(BearerTokenUtil.extractHeaderToken(request));
    }

}
