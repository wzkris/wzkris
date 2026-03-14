package com.wzkris.risk.service.decision.rule;

import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.risk.domain.RiskDecision;
import com.wzkris.risk.properties.RiskProperties;
import com.wzkris.risk.service.decision.RiskDecisionContext;
import com.wzkris.risk.service.decision.RiskDecisionResponseFactory;
import com.wzkris.risk.service.decision.RiskRule;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Optional;

@Component
@Order(20)
@RequiredArgsConstructor
public class CrawlerRule implements RiskRule {

    private final RiskProperties riskProperties;

    @Override
    public Optional<RiskDecision> evaluate(RiskDecisionContext context) {
        CrawlerCheckResult result = checkCrawler(context.getUserAgent());
        if (result.crawler()) {
            if (result.highRisk()) {
                return Optional.of(RiskDecisionResponseFactory.block("检测到疑似爬虫行为", 90));
            }
            return Optional.of(RiskDecisionResponseFactory.captcha("检测到异常访问，请完成安全验证", 65));
        }
        return Optional.empty();
    }

    private CrawlerCheckResult checkCrawler(String userAgent) {
        if (StringUtil.isBlank(userAgent)) {
            return new CrawlerCheckResult(riskProperties.isBlockEmptyUserAgent(), false);
        }
        String lower = userAgent.toLowerCase(Locale.ROOT);

        // 高风险爬虫特征 - 直接拦截
        boolean isHighRisk = lower.contains("headless")
                || lower.contains("selenium")
                || lower.contains("puppeteer")
                || lower.contains("playwright")
                || lower.contains("phantomjs");

        // 中等风险特征 - 要求验证码
        boolean isMediumRisk = lower.contains("python-requests")
                || lower.contains("curl/");

        if (isHighRisk) {
            return new CrawlerCheckResult(true, true);
        }
        if (isMediumRisk) {
            return new CrawlerCheckResult(true, false);
        }
        return new CrawlerCheckResult(false, false);
    }

    private record CrawlerCheckResult(boolean crawler, boolean highRisk) {

    }

}
