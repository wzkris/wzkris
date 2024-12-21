package com.wzkris.equipment.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.annotation.group.ValidationGroups;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.security.oauth2.annotation.CheckPerms;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.equipment.domain.ThingsModel;
import com.wzkris.equipment.domain.req.ThingsModelQueryReq;
import com.wzkris.equipment.mapper.ThingsModelMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * (ThingsModel)表控制层
 *
 * @author wzkris
 * @since 2024-12-21
 */
@Tag(name = "物模型管理")
@RestController
@RequestMapping("/things_model")
@RequiredArgsConstructor
public class ThingsModelController extends BaseController {
    private final ThingsModelMapper thingsModelMapper;

    @Operation(summary = "分页查询")
    @GetMapping("/list")
    @CheckPerms("things_model:list")
    public Result<Page<ThingsModel>> listPage(ThingsModelQueryReq queryReq) {
        startPage();
        List<ThingsModel> list = thingsModelMapper.selectList(this.buildQueryWrapper(queryReq));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<ThingsModel> buildQueryWrapper(ThingsModelQueryReq queryReq) {
        return new LambdaQueryWrapper<ThingsModel>()
                .like(StringUtil.isNotBlank(queryReq.getModelName()), ThingsModel::getModelName, queryReq.getModelName())
                .eq(StringUtil.isNotNull(queryReq.getPdtId()), ThingsModel::getPdtId, queryReq.getPdtId())
                .eq(StringUtil.isNotBlank(queryReq.getModelType()), ThingsModel::getModelType, queryReq.getModelType())
                .orderByDesc(ThingsModel::getModelSort, ThingsModel::getModelId);
    }

    @Operation(summary = "id查询物模型信息")
    @GetMapping("/{modelId}")
    @CheckPerms("things_model:query")
    public Result<ThingsModel> queryById(@PathVariable Long modelId) {
        return ok(thingsModelMapper.selectById(modelId));
    }

    @Operation(summary = "添加物模型")
    @OperateLog(title = "物模型管理", subTitle = "添加物模型", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckPerms("things_model:add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroups.Insert.class) ThingsModel thingsModel) {
        return toRes(thingsModelMapper.insert(thingsModel));
    }

    @Operation(summary = "修改物模型")
    @OperateLog(title = "物模型管理", subTitle = "修改物模型", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckPerms("things_model:edit")
    public Result<Void> edit(@RequestBody ThingsModel thingsModel) {
        return toRes(thingsModelMapper.updateById(thingsModel));
    }

    @Operation(summary = "删除物模型")
    @OperateLog(title = "物模型管理", subTitle = "删除物模型", operateType = OperateType.UPDATE)
    @PostMapping("/remove")
    @CheckPerms("things_model:remove")
    public Result<Void> deleteById(@RequestBody Long modelId) {
        return toRes(thingsModelMapper.deleteById(modelId));
    }

}
