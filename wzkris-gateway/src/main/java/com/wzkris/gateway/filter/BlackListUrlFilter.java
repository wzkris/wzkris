package com.wzkris.gateway.filter;

import com.wzkris.common.core.enums.BizBaseCode;
import com.wzkris.gateway.utils.WebFluxUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 黑名单过滤器
 *
 * @author wzkris
 */
@Component
public class BlackListUrlFilter extends AbstractGatewayFilterFactory<BlackListUrlFilter.Config> {

    public BlackListUrlFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String url = exchange.getRequest().getURI().getPath();
            if (config.matchBlacklist(url)) {
                return WebFluxUtil.writeResponse(exchange.getResponse(), BizBaseCode.FORBID);
            }

            return chain.filter(exchange);
        };
    }

    public static class Config {

        private final List<Pattern> blacklistUrlPattern = new ArrayList<>();

        public boolean matchBlacklist(String url) {
            return !blacklistUrlPattern.isEmpty()
                    && blacklistUrlPattern.stream().anyMatch(p -> p.matcher(url).find());
        }

        public void setBlacklistUrl(List<String> blacklistUrl) {
            this.blacklistUrlPattern.clear();
            blacklistUrl.forEach(url -> {
                this.blacklistUrlPattern.add(
                        Pattern.compile(url.replaceAll("\\*\\*", "(.*?)"), Pattern.CASE_INSENSITIVE));
            });
        }

    }

}
