package com.wzkris.system.impl.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.system.api.config.ConfigMngApi;
import com.wzkris.system.domain.ConfigInfoDO;
import com.wzkris.system.mapper.ConfigInfoMapper;
import com.wzkris.system.request.config.ConfigMngQueryRequest;
import com.wzkris.system.request.config.ConfigMngSaveRequest;
import com.wzkris.system.request.config.ConfigMngUpdateRequest;
import com.wzkris.system.response.config.ConfigInfoResponse;
import com.wzkris.system.service.ConfigInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfigMngApiImpl extends BaseController implements ConfigMngApi {

    private final ConfigInfoMapper configInfoMapper;

    private final ConfigInfoService configInfoService;

    @Override
    public Result<Page<ConfigInfoResponse>> queryPage(ConfigMngQueryRequest request) {
        startPage();
        List<ConfigInfoDO> list = configInfoMapper.selectList(this.buildQueryWrapper(request));
        return getDataTable(BeanUtil.convert(list, ConfigInfoResponse.class));
    }

    private LambdaQueryWrapper<ConfigInfoDO> buildQueryWrapper(ConfigMngQueryRequest request) {
        return new LambdaQueryWrapper<ConfigInfoDO>()
                .like(StringUtil.isNotEmpty(request.getConfigKey()), ConfigInfoDO::getConfigKey, request.getConfigKey())
                .like(
                        StringUtil.isNotEmpty(request.getConfigName()),
                        ConfigInfoDO::getConfigName,
                        request.getConfigName())
                .like(
                        StringUtil.isNotEmpty(request.getConfigType()),
                        ConfigInfoDO::getConfigType,
                        request.getConfigType())
                .orderByDesc(ConfigInfoDO::getConfigId);
    }

    @Override
    public Result<ConfigInfoResponse> queryInfo(Long configId) {
        return ok(BeanUtil.convert(configInfoMapper.selectById(configId), ConfigInfoResponse.class));
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
    public Result<Void> remove(Long configId) {
        ConfigInfoDO config = configInfoMapper.selectById(configId);
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
