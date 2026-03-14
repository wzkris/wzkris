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
@Order(10)
@RequiredArgsConstructor
public class AbnormalSourceRule implements RiskRule {

    private final RiskProperties riskProperties;

    @Override
    public Optional<RiskDecision> evaluate(RiskDecisionContext context) {
        if (isAbnormalSource(context.getOrigin(), context.getReferer())) {
            return Optional.of(RiskDecisionResponseFactory.captcha("检测到异常来源，请完成安全验证", 60));
        }
        return Optional.empty();
    }

    private boolean isAbnormalSource(String origin, String referer) {
        if (StringUtil.isBlank(origin) && StringUtil.isBlank(referer)) {
            return false;
        }
        String raw = (StringUtil.defaultString(origin) + " " + StringUtil.defaultString(referer)).toLowerCase(Locale.ROOT);

        // 如果配置了白名单模式，不拦截本地来源
        if (riskProperties.isAllowLocalSource()) {
            return false;
        }

        return raw.contains("127.0.0.1")
                || raw.contains("localhost")
                || raw.contains("null");
    }

}
