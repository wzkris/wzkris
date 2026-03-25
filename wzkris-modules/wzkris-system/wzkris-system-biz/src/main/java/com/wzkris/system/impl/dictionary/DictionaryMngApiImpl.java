package com.wzkris.system.impl.dictionary;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.system.api.dictionary.DictionaryMngApi;
import com.wzkris.system.domain.DictionaryInfoDO;
import com.wzkris.system.mapper.DictionaryInfoMapper;
import com.wzkris.system.request.dictionary.DictionaryMngQueryRequest;
import com.wzkris.system.request.dictionary.DictionaryMngSaveRequest;
import com.wzkris.system.request.dictionary.DictionaryMngUpdateRequest;
import com.wzkris.system.response.dictionary.DictionaryInfoResponse;
import com.wzkris.system.service.DictionaryInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DictionaryMngApiImpl extends BaseController implements DictionaryMngApi {

    private final DictionaryInfoMapper dictionaryInfoMapper;

    private final DictionaryInfoService dictionaryInfoService;

    @Override
    public Result<Page<DictionaryInfoResponse>> queryPage(DictionaryMngQueryRequest request) {
        startPage();
        LambdaQueryWrapper<DictionaryInfoDO> lqw = this.buildQueryWrapper(request);
        return getDataTable(BeanUtil.convert(dictionaryInfoMapper.selectList(lqw), DictionaryInfoResponse.class));
    }

    private LambdaQueryWrapper<DictionaryInfoDO> buildQueryWrapper(DictionaryMngQueryRequest request) {
        return new LambdaQueryWrapper<DictionaryInfoDO>()
                .like(StringUtil.isNotBlank(request.getDictName()), DictionaryInfoDO::getDictName, request.getDictName())
                .like(StringUtil.isNotBlank(request.getDictKey()), DictionaryInfoDO::getDictKey, request.getDictKey())
                .orderByDesc(DictionaryInfoDO::getDictId);
    }

    @Override
    public Result<DictionaryInfoResponse> queryInfo(Long dictId) {
        return ok(BeanUtil.convert(dictionaryInfoMapper.selectById(dictId), DictionaryInfoResponse.class));
    }

    @Override
    public Result<Void> save(DictionaryMngSaveRequest addReq) {
        if (dictionaryInfoService.checkUsedByDictKey(addReq.getDictId(), addReq.getDictKey())) {
            return requestFail("新增字典'" + addReq.getDictName() + "'失败，字典类型已存在");
        }
        return toRes(dictionaryInfoService.insertDict(BeanUtil.convert(addReq, DictionaryInfoDO.class)));
    }

    @Override
    public Result<Void> update(DictionaryMngUpdateRequest request) {
        if (dictionaryInfoService.checkUsedByDictKey(request.getDictId(), request.getDictKey())) {
            return requestFail("修改字典'" + request.getDictName() + "'失败，字典类型已存在");
        }
        return toRes(dictionaryInfoService.updateDict(BeanUtil.convert(request, DictionaryInfoDO.class)));
    }

    @Override
    public Result<Void> remove(Long dictId) {
        return toRes(dictionaryInfoService.deleteById(dictId));
    }

    @Override
    public Result<?> refreshCache() {
        dictionaryInfoService.loadingDictCache();
        return ok();
    }

}
