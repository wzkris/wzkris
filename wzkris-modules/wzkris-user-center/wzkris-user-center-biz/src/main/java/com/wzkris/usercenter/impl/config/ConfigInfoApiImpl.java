package com.wzkris.usercenter.impl.config;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.usercenter.api.config.ConfigInfoApi;
import com.wzkris.usercenter.api.config.request.ConfigInfoQueryRequest;
import com.wzkris.usercenter.service.ConfigInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfigInfoApiImpl extends AbstractApi implements ConfigInfoApi {

    private final ConfigInfoService configInfoService;

    @Override
    public Result<String> queryValue(ConfigInfoQueryRequest request) {
        return ok(configInfoService.getValueByKey(request.getConfigKey()));
    }

}
