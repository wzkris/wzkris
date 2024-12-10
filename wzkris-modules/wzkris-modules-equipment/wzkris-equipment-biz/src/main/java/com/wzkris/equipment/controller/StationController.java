package com.wzkris.equipment.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.equipment.domain.Station;
import com.wzkris.equipment.domain.req.BindingReq;
import com.wzkris.equipment.domain.req.EditStatusReq;
import com.wzkris.equipment.domain.req.StationQueryReq;
import com.wzkris.equipment.mapper.StationMapper;
import com.wzkris.equipment.service.StationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * (Station)表控制层
 *
 * @author wzkris
 * @since 2024-12-09 12:56:40
 */
@Tag(name = "站点管理")
@RestController
@RequestMapping("/station")
@RequiredArgsConstructor
public class StationController extends BaseController {
    private final StationMapper stationMapper;
    private final StationService stationService;

    @Operation(summary = "分页查询")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('station:list')")
    public Result<Page<Station>> listPage(StationQueryReq req) {
        startPage();
        List<Station> list = stationMapper.selectList(this.buildQueryWrapper(req));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<Station> buildQueryWrapper(StationQueryReq req) {
        return new LambdaQueryWrapper<Station>()
                .like(StringUtil.isNotBlank(req.getStationName()), Station::getStationName, req.getStationName())
                .eq(StringUtil.isNotBlank(req.getStatus()), Station::getStatus, req.getStatus())
                .orderByDesc(Station::getStationId);
    }

    @Operation(summary = "id查询站点信息")
    @GetMapping("/{stationId}")
    @PreAuthorize("@ps.hasPerms('station:query')")
    public Result<Station> queryById(@PathVariable Long stationId) {
        return ok(stationMapper.selectById(stationId));
    }

    @Operation(summary = "添加站点")
    @OperateLog(title = "站点管理", subTitle = "添加站点", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('station:add')")
    public Result<Void> add(@RequestBody @Valid Station station) {
        return toRes(stationMapper.insert(station));
    }

    @Operation(summary = "修改站点")
    @OperateLog(title = "站点管理", subTitle = "修改站点", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('station:edit')")
    public Result<Void> edit(@RequestBody @Valid Station station) {
        return toRes(stationMapper.updateById(station));
    }

    @Operation(summary = "修改站点状态")
    @OperateLog(title = "站点管理", subTitle = "修改站点状态", operateType = OperateType.UPDATE)
    @PostMapping("/edit_status")
    @PreAuthorize("@ps.hasPerms('station:edit')")
    public Result<Void> editStatus(@RequestBody @Valid EditStatusReq statusReq) {
        Station update = new Station(statusReq.getId());
        update.setStatus(statusReq.getStatus());
        return toRes(stationMapper.updateById(update));
    }

    @Operation(summary = "绑定设备")
    @OperateLog(title = "站点管理", subTitle = "绑定设备", operateType = OperateType.UPDATE)
    @PostMapping("/binding_device")
    @PreAuthorize("@ps.hasPerms('station:bind')")
    public Result<Void> bindingDevice(@RequestBody @Valid BindingReq bindingReq) {
        Station station = stationMapper.selectById(bindingReq.getId());
        if (station == null) {
            return fail("站点不存在");
        }
        else if (StringUtil.equals(CommonConstants.STATUS_DISABLE, station.getStatus())) {
            return fail("该站点已被禁用, 禁止绑定设备");
        }
        stationService.bindDevice(bindingReq.getId(), bindingReq.getBindingIds());
        return ok();
    }

    @Operation(summary = "解绑设备")
    @OperateLog(title = "站点管理", subTitle = "解绑设备", operateType = OperateType.UPDATE)
    @PostMapping("/unbinding_device")
    @PreAuthorize("@ps.hasPerms('station:bind')")
    public Result<Void> unbindingDevice(@RequestBody @Valid BindingReq bindingReq) {
        Station station = stationMapper.selectById(bindingReq.getId());
        if (station == null) {
            return fail("站点不存在");
        }
        else if (StringUtil.equals(CommonConstants.STATUS_DISABLE, station.getStatus())) {
            return fail("该站点已被禁用, 禁止绑定设备");
        }
        stationService.unbindDevice(bindingReq.getId(), bindingReq.getBindingIds());
        return ok();
    }

    @Operation(summary = "删除站点")
    @OperateLog(title = "站点管理", subTitle = "删除站点", operateType = OperateType.UPDATE)
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('station:remove')")
    public Result<Void> deleteById(@RequestBody Long stationId) {
        if (stationService.checkStationUsed(stationId)) {
            return fail("删除失败, 该站点正在使用");
        }
        return toRes(stationMapper.deleteById(stationId));
    }

}
