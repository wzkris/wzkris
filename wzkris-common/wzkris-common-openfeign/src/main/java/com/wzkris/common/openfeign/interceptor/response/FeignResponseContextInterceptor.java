package com.wzkris.common.openfeign.interceptor.response;

import com.wzkris.common.openfeign.constants.FeignHeaderConstant;
import com.wzkris.common.openfeign.interceptor.AuthenticationTokenUtil;
import feign.InvocationContext;
import feign.Response;
import feign.ResponseInterceptor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
@ConditionalOnClass(UsernamePasswordAuthenticationToken.class)
public class FeignResponseContextInterceptor implements ResponseInterceptor {

    @Override
    public Object intercept(InvocationContext invocationContext, Chain chain) throws Exception {
        try (Response response = invocationContext.response()) {
            Collection<String> contexts = response.headers().get(FeignHeaderConstant.X_SECURITY_CONTEXT);
            if (CollectionUtils.isNotEmpty(contexts)) {
                Optional<String> context = contexts.stream().findFirst();
                SecurityContext securityContext = AuthenticationTokenUtil
                        .deserialize(context.get(), SecurityContext.class);
                SecurityContextHolder.setContext(securityContext);
            }
            return chain.next(invocationContext);
        }
    }

}
