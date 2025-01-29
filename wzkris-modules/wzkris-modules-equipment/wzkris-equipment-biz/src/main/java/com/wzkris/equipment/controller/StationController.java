package com.wzkris.equipment.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.BeanUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.security.oauth2.annotation.CheckPerms;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.equipment.domain.Station;
import com.wzkris.equipment.domain.req.BindingReq;
import com.wzkris.equipment.domain.req.EditStatusReq;
import com.wzkris.equipment.domain.req.StationQueryReq;
import com.wzkris.equipment.domain.req.StationReq;
import com.wzkris.equipment.mapper.StationMapper;
import com.wzkris.equipment.service.StationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    @CheckPerms("station:list")
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

    @Operation(summary = "站点选择列表(不带分页)")
    @GetMapping("/selectlist")
    @CheckPerms(value = {"device:add", "device:edit"}, mode = CheckPerms.Mode.OR)// 设备添加修改时使用
    public Result<List<Station>> selectList(String stationName) {
        LambdaQueryWrapper<Station> lqw = new LambdaQueryWrapper<Station>()
                .select(Station::getStationId, Station::getStationName)
                .eq(Station::getStatus, CommonConstants.STATUS_ENABLE)
                .like(StringUtil.isNotBlank(stationName), Station::getStationName, stationName);
        List<Station> list = stationMapper.selectList(lqw);
        return ok(list);
    }

    @Operation(summary = "id查询站点信息")
    @GetMapping("/{stationId}")
    @CheckPerms("station:query")
    public Result<Station> queryById(@PathVariable Long stationId) {
        return ok(stationMapper.selectById(stationId));
    }

    @Operation(summary = "添加站点")
    @OperateLog(title = "站点管理", subTitle = "添加站点", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckPerms("station:add")
    public Result<Void> add(@RequestBody @Valid StationReq req) {
        return toRes(stationMapper.insert(BeanUtil.convert(req, Station.class)));
    }

    @Operation(summary = "修改站点")
    @OperateLog(title = "站点管理", subTitle = "修改站点", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckPerms("station:edit")
    public Result<Void> edit(@RequestBody @Valid StationReq req) {
        return toRes(stationMapper.updateById(BeanUtil.convert(req, Station.class)));
    }

    @Operation(summary = "修改站点状态")
    @OperateLog(title = "站点管理", subTitle = "修改站点状态", operateType = OperateType.UPDATE)
    @PostMapping("/edit_status")
    @CheckPerms("station:edit")
    public Result<Void> editStatus(@RequestBody @Valid EditStatusReq statusReq) {
        Station update = new Station(statusReq.getId());
        update.setStatus(statusReq.getStatus());
        return toRes(stationMapper.updateById(update));
    }

    @Operation(summary = "绑定设备")
    @OperateLog(title = "站点管理", subTitle = "绑定设备", operateType = OperateType.UPDATE)
    @PostMapping("/binding_device")
    @CheckPerms("station:bind")
    public Result<Void> bindingDevice(@RequestBody @Valid BindingReq bindingReq) {
        Station station = stationMapper.selectById(bindingReq.getId());
        if (station == null) {
            return error412("站点不存在");
        } else if (StringUtil.equals(CommonConstants.STATUS_DISABLE, station.getStatus())) {
            return error412("该站点已被禁用, 禁止绑定设备");
        }
        stationService.bindDevice(bindingReq.getId(), bindingReq.getBindingIds());
        return ok();
    }

    @Operation(summary = "解绑设备")
    @OperateLog(title = "站点管理", subTitle = "解绑设备", operateType = OperateType.UPDATE)
    @PostMapping("/unbinding_device")
    @CheckPerms("station:bind")
    public Result<Void> unbindingDevice(@RequestBody @Valid BindingReq bindingReq) {
        Station station = stationMapper.selectById(bindingReq.getId());
        if (station == null) {
            return error412("站点不存在");
        }
        stationService.unbindDevice(bindingReq.getId(), bindingReq.getBindingIds());
        return ok();
    }

    @Operation(summary = "删除站点")
    @OperateLog(title = "站点管理", subTitle = "删除站点", operateType = OperateType.UPDATE)
    @PostMapping("/remove")
    @CheckPerms("station:remove")
    public Result<Void> deleteById(@RequestBody Long stationId) {
        if (stationService.checkStationUsed(stationId)) {
            return error412("删除失败, 该站点正在使用");
        }
        return toRes(stationMapper.deleteById(stationId));
    }

}
