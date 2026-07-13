package com.wzkris.usercenter.impl.dictionary;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.api.dictionary.DictionaryMngApi;
import com.wzkris.usercenter.api.dictionary.request.DictionaryDataInfo;
import com.wzkris.usercenter.api.dictionary.request.DictionaryMngPageRequest;
import com.wzkris.usercenter.api.dictionary.request.DictionaryMngSaveRequest;
import com.wzkris.usercenter.api.dictionary.request.DictionaryMngUpdateRequest;
import com.wzkris.usercenter.api.dictionary.response.DictionaryDataResponse;
import com.wzkris.usercenter.api.dictionary.response.DictionaryMngResponse;
import com.wzkris.usercenter.domain.DictionaryInfoDO;
import com.wzkris.usercenter.mapper.DictionaryInfoMapper;
import com.wzkris.usercenter.service.DictionaryInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DictionaryMngApiImpl extends AbstractApi implements DictionaryMngApi {

    private final DictionaryInfoMapper dictionaryInfoMapper;

    private final DictionaryInfoService dictionaryInfoService;

    @Override
    public Result<Page<DictionaryMngResponse>> queryPage(DictionaryMngPageRequest request) {
        startPage(request);
        LambdaQueryWrapper<DictionaryInfoDO> lqw = this.buildQueryWrapper(request);
        List<DictionaryInfoDO> source = dictionaryInfoMapper.selectList(lqw);
        List<DictionaryMngResponse> result = source.stream().map(this::toMngResponse).toList();
        return getPageResult(result);
    }

    private LambdaQueryWrapper<DictionaryInfoDO> buildQueryWrapper(DictionaryMngPageRequest request) {
        return new LambdaQueryWrapper<DictionaryInfoDO>()
                .like(StringUtil.isNotBlank(request.getDictName()), DictionaryInfoDO::getDictName, request.getDictName())
                .like(StringUtil.isNotBlank(request.getDictKey()), DictionaryInfoDO::getDictKey, request.getDictKey())
                .orderByDesc(DictionaryInfoDO::getDictId);
    }

    @Override
    public Result<DictionaryMngResponse> queryInfo(IdRequest request) {
        Long dictId = request.getId();
        DictionaryInfoDO source = dictionaryInfoMapper.selectById(dictId);
        return ok(toMngResponse(source));
    }

    @Override
    public Result<Void> save(DictionaryMngSaveRequest request) {
        if (dictionaryInfoService.checkUsedByDictKey(request.getDictId(), request.getDictKey())) {
            return requestFail("新增字典'" + request.getDictName() + "'失败，字典类型已存在");
        }
        return toRes(dictionaryInfoService.insertDict(toDictInfoDO(request)));
    }

    @Override
    public Result<Void> update(DictionaryMngUpdateRequest request) {
        if (dictionaryInfoService.checkUsedByDictKey(request.getDictId(), request.getDictKey())) {
            return requestFail("修改字典'" + request.getDictName() + "'失败，字典类型已存在");
        }
        return toRes(dictionaryInfoService.updateDict(toDictInfoDO(request)));
    }

    @Override
    public Result<Void> remove(IdRequest request) {
        Long dictId = request.getId();
        return toRes(dictionaryInfoService.deleteById(dictId));
    }

    @Override
    public Result<?> refreshCache() {
        dictionaryInfoService.loadingDictCache();
        return ok();
    }

    private DictionaryMngResponse toMngResponse(DictionaryInfoDO source) {
        if (source == null) {
            return null;
        }
        DictionaryMngResponse target = BeanUtil.convert(source, DictionaryMngResponse.class);
        DictionaryInfoDO.DictData[] sourceArray = source.getDictValue();
        if (sourceArray == null) {
            target.setDictValue(null);
            return target;
        }
        DictionaryDataResponse[] targetArray = new DictionaryDataResponse[sourceArray.length];
        for (int i = 0; i < sourceArray.length; i++) {
            targetArray[i] = BeanUtil.convert(sourceArray[i], DictionaryDataResponse.class);
        }
        target.setDictValue(targetArray);
        return target;
    }

    private DictionaryInfoDO toDictInfoDO(DictionaryMngSaveRequest source) {
        DictionaryInfoDO target = BeanUtil.convert(source, DictionaryInfoDO.class);
        target.setDictValue(toDictData(source.getDictValue()));
        return target;
    }

    private DictionaryInfoDO toDictInfoDO(DictionaryMngUpdateRequest source) {
        DictionaryInfoDO target = BeanUtil.convert(source, DictionaryInfoDO.class);
        target.setDictValue(toDictData(source.getDictValue()));
        return target;
    }

    private DictionaryInfoDO.DictData[] toDictData(DictionaryDataInfo[] sourceArray) {
        if (sourceArray == null) {
            return null;
        }
        DictionaryInfoDO.DictData[] targetArray = new DictionaryInfoDO.DictData[sourceArray.length];
        for (int i = 0; i < sourceArray.length; i++) {
            targetArray[i] = BeanUtil.convert(sourceArray[i], DictionaryInfoDO.DictData.class);
        }
        return targetArray;
    }

}
