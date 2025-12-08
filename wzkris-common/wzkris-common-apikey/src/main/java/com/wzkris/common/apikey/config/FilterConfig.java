package com.wzkris.common.apikey.config;

import com.wzkris.common.apikey.filter.TraceIdFilter;
import com.wzkris.common.apikey.filter.RequestSignatureFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * 自定义的过滤器链
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<TraceIdFilter> commonRequestAndResponseFilter() {
        FilterRegistrationBean<TraceIdFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new TraceIdFilter());
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

    @Bean
    @ConditionalOnProperty(prefix = "request-signature", name = "enable", havingValue = "true", matchIfMissing = true)
    public FilterRegistrationBean<RequestSignatureFilter> requestSignatureFilter(
            SignkeyProperties signkeyProperties) {
        FilterRegistrationBean<RequestSignatureFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RequestSignatureFilter(signkeyProperties));
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        return registration;
    }

}
