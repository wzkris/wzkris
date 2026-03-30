package com.wzkris.system.impl.config;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.system.api.config.ConfigInfoApi;
import com.wzkris.system.service.ConfigInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfigInfoApiImpl extends AbstractApi implements ConfigInfoApi {

    private final ConfigInfoService configInfoService;

    @Override
    public Result<String> queryValue(String configKey) {
        return ok(configInfoService.getValueByKey(configKey));
    }

}
