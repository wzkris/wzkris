package com.wzkris.risk.captcha;

import com.wzkris.common.core.utils.StringUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CaptchaAbilityRegistry {

    private final Map<String, CaptchaAbility> abilityMap;

    public CaptchaAbilityRegistry(List<CaptchaAbility> abilities) {
        this.abilityMap = abilities.stream()
                .collect(Collectors.toUnmodifiableMap(CaptchaAbility::type, Function.identity(), (a, b) -> a));
    }

    public CaptchaAbility get(String type) {
        if (StringUtil.isBlank(type)) {
            throw new IllegalArgumentException("invalidParameter.captcha.type.error");
        }
        CaptchaAbility ability = abilityMap.get(type);
        if (ability == null) {
            throw new IllegalArgumentException("invalidParameter.captcha.type.error");
        }
        return ability;
    }

}
