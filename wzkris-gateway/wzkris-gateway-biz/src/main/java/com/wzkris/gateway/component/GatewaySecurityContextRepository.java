package com.wzkris.gateway.component;

import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.component.SupplierDeferredSecurityContext;
import com.wzkris.common.security.utils.BearerTokenUtil;
import com.wzkris.gateway.properties.PermitUrlProperties;
import com.wzkris.gateway.service.TokenValidateService;
import com.wzkris.gateway.service.impl.TokenValidateServiceImpl;
import com.wzkris.gateway.utils.PathMatchUtil;
import com.wzkris.gateway.utils.ScanAnnotationUrlUtil;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.DeferredSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Gateway 专用 SecurityContextRepository。
 * <p>
 * - 不再根据自定义请求头还原用户信息，避免外部伪造请求头绕过认证；
 * - 直接复用网关的 {@link TokenValidateServiceImpl} 做 Token 校验，并构建 SecurityContext；
 * - 复用网关的白名单/黑名单配置，白名单直接返回空上下文，黑名单交给下游 Filter 处理。
 *
 * @author wzkris
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GatewaySecurityContextRepository implements SecurityContextRepository, ApplicationRunner {

    private static final Authentication ANONYMOUS_AUTHENTICATION = new AnonymousAuthenticationToken("anonymous",
            "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));

    private final SecurityContextHolderStrategy contextHolderStrategy =
            SecurityContextHolder.getContextHolderStrategy();

    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource =
            new WebAuthenticationDetailsSource();

    private final TokenValidateService tokenValidateService;

    private final PermitUrlProperties permitUrlProperties;

    /**
     * 带 {@link PermitAll} 注解的 URL 集合（启动时扫描）。
     */
    private final Set<String> permitAllAnnotations = new HashSet<>();

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
        Authentication authentication;

        if (isPathPermitted(request.getRequestURI())) { //白名单放行
            authentication = ANONYMOUS_AUTHENTICATION;
        } else {
            authentication = this.tokenValidateService.loadAuthenticationByRequest(request);
        }

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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.permitAllAnnotations.addAll(ScanAnnotationUrlUtil.scanUrls(PermitAll.class));
    }

    private boolean isPathPermitted(String path) {
        return PathMatchUtil.matchAny(permitUrlProperties.getIgnores(), path)
                || PathMatchUtil.matchAny(permitAllAnnotations, path);
    }

}
