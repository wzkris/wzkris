package com.wzkris.gateway.component;

import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.component.SupplierDeferredSecurityContext;
import com.wzkris.gateway.properties.PermitAllProperties;
import com.wzkris.gateway.service.TokenExtractService;
import com.wzkris.gateway.utils.ScanAnnotationUrlUtil;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.DeferredSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Gateway 专用 SecurityContextRepository。
 * <p>
 * - 不再根据自定义请求头还原用户信息，避免外部伪造请求头绕过认证；
 * - 直接复用网关的 {@link TokenExtractService} 做 Token 校验，并构建 SecurityContext；
 * - 复用网关的白名单/黑名单配置，白名单直接返回空上下文，黑名单交给下游 Filter 处理。
 *
 * @author wzkris
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GatewaySecurityContextRepository implements SecurityContextRepository, ApplicationRunner {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private final SecurityContextHolderStrategy contextHolderStrategy =
            SecurityContextHolder.getContextHolderStrategy();

    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource =
            new WebAuthenticationDetailsSource();

    private final TokenExtractService tokenExtractService;

    private final PermitAllProperties permitAllProperties;

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
        SecurityContext context = this.contextHolderStrategy.createEmptyContext();

        String path = request.getRequestURI();
        // 白名单：不加载任何认证信息
        if (isPathPermitted(path)) {
            return context;
        }

        try {
            Authentication authentication = this.tokenExtractService.getAuthentication(request);
            if (authentication instanceof UsernamePasswordAuthenticationToken authenticationToken) {
                authenticationToken.setDetails(this.authenticationDetailsSource.buildDetails(request));
            }
            context.setAuthentication(authentication);
        } catch (Exception ex) {
            log.error("GatewaySecurityContextRepository error", ex);
        }

        return context;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        // 网关不负责持久化 SecurityContext
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        return this.contextHolderStrategy.getContext().getAuthentication() != null;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.permitAllAnnotations.addAll(ScanAnnotationUrlUtil.scanUrls(PermitAll.class));
    }

    private boolean isPathPermitted(String path) {
        return isPathMatched(permitAllProperties.getIgnores(), path)
                || isPathMatched(permitAllAnnotations, path);
    }

    private static boolean isPathMatched(Iterable<String> patterns, String path) {
        if (patterns == null) {
            return false;
        }
        for (String pattern : patterns) {
            if (StringUtil.isNotBlank(pattern) && PATH_MATCHER.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

}
