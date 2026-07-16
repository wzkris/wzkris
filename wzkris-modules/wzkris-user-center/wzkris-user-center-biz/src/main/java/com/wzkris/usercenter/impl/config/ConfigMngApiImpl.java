package com.wzkris.usercenter.impl.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.api.config.ConfigMngApi;
import com.wzkris.usercenter.api.config.request.ConfigMngPageRequest;
import com.wzkris.usercenter.api.config.request.ConfigMngSaveRequest;
import com.wzkris.usercenter.api.config.request.ConfigMngUpdateRequest;
import com.wzkris.usercenter.api.config.response.ConfigInfoResponse;
import com.wzkris.usercenter.domain.ConfigInfoDO;
import com.wzkris.usercenter.service.ConfigInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfigMngApiImpl extends AbstractApi implements ConfigMngApi {

    private final ConfigInfoService configInfoService;

    @Override
    public Result<Page<ConfigInfoResponse>> queryPage(ConfigMngPageRequest request) {
        IPage<ConfigInfoDO> page = configInfoService.page(request.buildPage(), this.buildQueryWrapper(request));
        return ok(Page.of(page, BeanUtil.convert(page.getRecords(), ConfigInfoResponse.class)));
    }

    private LambdaQueryWrapper<ConfigInfoDO> buildQueryWrapper(ConfigMngPageRequest request) {
        return new LambdaQueryWrapper<ConfigInfoDO>()
                .like(StringUtil.isNotEmpty(request.getConfigKey()), ConfigInfoDO::getConfigKey, request.getConfigKey())
                .like(StringUtil.isNotEmpty(request.getConfigName()),
                        ConfigInfoDO::getConfigName,
                        request.getConfigName())
                .like(StringUtil.isNotEmpty(request.getConfigType()),
                        ConfigInfoDO::getConfigType,
                        request.getConfigType())
                .orderByDesc(ConfigInfoDO::getConfigId);
    }

    @Override
    public Result<ConfigInfoResponse> queryInfo(IdRequest request) {
        Long configId = request.getId();
        return ok(BeanUtil.convert(configInfoService.getById(configId), ConfigInfoResponse.class));
    }

    @Override
    public Result<Void> save(ConfigMngSaveRequest request) {
        if (configInfoService.checkUsedByConfigKey(null, request.getConfigKey())) {
            return requestFail("新增参数'" + request.getConfigName() + "'失败，参数键名已存在");
        }
        return toRes(configInfoService.insertConfig(BeanUtil.convert(request, ConfigInfoDO.class)));
    }

    @Override
    public Result<Void> update(ConfigMngUpdateRequest request) {
        if (configInfoService.checkUsedByConfigKey(request.getConfigId(), request.getConfigKey())) {
            return requestFail("修改参数'" + request.getConfigName() + "'失败，参数键名已存在");
        }
        return toRes(configInfoService.updateConfig(BeanUtil.convert(request, ConfigInfoDO.class)));
    }

    @Override
    public Result<Void> remove(IdRequest request) {
        Long configId = request.getId();
        ConfigInfoDO config = configInfoService.getById(configId);
        if (config.getBuiltIn()) {
            return requestFail(String.format("内置参数'%s'不能删除", config.getConfigKey()));
        }
        return toRes(configInfoService.deleteById(configId));
    }

    @Override
    public Result<Void> refreshCache() {
        configInfoService.loadingConfigCache();
        return ok();
    }

}
