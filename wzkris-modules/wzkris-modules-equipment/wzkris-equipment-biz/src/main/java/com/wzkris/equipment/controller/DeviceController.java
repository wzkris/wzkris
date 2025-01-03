package com.wzkris.equipment.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.annotation.group.ValidationGroups;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.BeanUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.annotation.CheckFieldPerms;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.security.oauth2.annotation.CheckPerms;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.equipment.domain.Device;
import com.wzkris.equipment.domain.req.DeviceQueryReq;
import com.wzkris.equipment.domain.req.DeviceReq;
import com.wzkris.equipment.domain.req.EditStatusReq;
import com.wzkris.equipment.domain.vo.DeviceVO;
import com.wzkris.equipment.domain.vo.NetworkVO;
import com.wzkris.equipment.mapper.DeviceMapper;
import com.wzkris.equipment.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * (Device)表控制层
 *
 * @author wzkris
 * @since 2023-06-05 10:38:51
 */
@Tag(name = "设备管理")
@RestController
@RequestMapping("/device")
@RequiredArgsConstructor
public class DeviceController extends BaseController {
    private final DeviceMapper deviceMapper;
    private final DeviceService deviceService;

    @Operation(summary = "分页查询")
    @GetMapping("/list")
    @CheckPerms("device:list")
    public Result<Page<Device>> listPage(DeviceQueryReq queryReq) {
        startPage();
        List<Device> list = deviceMapper.selectList(this.buildQueryWrapper(queryReq));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<Device> buildQueryWrapper(DeviceQueryReq queryReq) {
        return new LambdaQueryWrapper<Device>()
                .like(StringUtil.isNotBlank(queryReq.getDeviceName()), Device::getDeviceName, queryReq.getDeviceName())
                .like(StringUtil.isNotBlank(queryReq.getCmcid()), Device::getCmcid, queryReq.getCmcid())
                .eq(StringUtil.isNotNull(queryReq.getStationId()), Device::getStationId, queryReq.getStationId())
                .eq(StringUtil.isNotNull(queryReq.getPdtId()), Device::getPdtId, queryReq.getPdtId())
                .eq(StringUtil.isNotBlank(queryReq.getStatus()), Device::getStatus, queryReq.getStatus())
                .eq(StringUtil.isNotNull(queryReq.getOnline()), Device::getOnline, queryReq.getOnline())
                .orderByDesc(Device::getOnline, Device::getDeviceId);
    }

    @Operation(summary = "查询未绑定站点设备(带分页)")
    @GetMapping("/list_unbinding")
    @CheckPerms("device:list")
    public Result<Page<Device>> listUnbinding(DeviceQueryReq req) {
        startPage();
        LambdaQueryWrapper<Device> lqw = this.buildQueryWrapper(req)
                .isNull(Device::getStationId);
        List<Device> list = deviceMapper.selectList(lqw);
        return getDataTable(list);
    }

    @Operation(summary = "id查询设备信息")
    @GetMapping("/{deviceId}")
    @CheckPerms("device:query")
    public Result<Device> queryById(@PathVariable Long deviceId) {
        return ok(deviceMapper.selectById(deviceId));
    }

    @Operation(summary = "id查询设备详情")
    @GetMapping("/details/{deviceId}")
    @CheckPerms("device:query")
    public Result<DeviceVO> queryDetailsById(@PathVariable Long deviceId) {
        return ok(deviceService.getVOById(deviceId));
    }

    @Operation(summary = "设备号查询入网信息")
    @GetMapping("/network_detail/{deviceId}")
    @CheckPerms("device:query")
    public Result<NetworkVO> queryNetwork(@PathVariable Long deviceId) {
        return ok(deviceService.getNetInfoBySno(deviceId));
    }

    @Operation(summary = "添加设备")
    @OperateLog(title = "设备管理", subTitle = "添加设备", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckPerms("device:add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroups.Insert.class) DeviceReq deviceReq) {
        Device device = BeanUtil.convert(deviceReq, Device.class);
        return toRes(deviceMapper.insert(device));
    }

    @Operation(summary = "修改设备")
    @OperateLog(title = "设备管理", subTitle = "修改设备", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckPerms("device:edit")
    @CheckFieldPerms(value = "@ps.hasPerms('device:field')", rw = CheckFieldPerms.Perms.WRITE, groups = ValidationGroups.Update.class)
    public Result<Void> edit(@RequestBody DeviceReq deviceReq) {
        Device device = BeanUtil.convert(deviceReq, Device.class);
        return toRes(deviceMapper.updateById(device));
    }

    @Operation(summary = "解绑站点")
    @OperateLog(title = "设备管理", subTitle = "解绑站点", operateType = OperateType.UPDATE)
    @PostMapping("/unbinding_station")
    @CheckPerms("device:edit")
    public Result<Void> unbindingDevice(@RequestBody Long deviceId) {
        LambdaUpdateWrapper<Device> luw = Wrappers.lambdaUpdate(Device.class)
                .eq(Device::getDeviceId, deviceId)
                .set(Device::getStationId, null)
                .set(Device::getLatitude, null)
                .set(Device::getLongitude, null);
        return toRes(deviceMapper.update(luw));
    }

    @Operation(summary = "修改设备状态")
    @OperateLog(title = "设备管理", subTitle = "修改设备状态", operateType = OperateType.UPDATE)
    @PostMapping("/edit_status")
    @CheckPerms("device:edit")
    public Result<Void> editStatus(@RequestBody EditStatusReq statusReq) {
        Device update = new Device(statusReq.getId());
        update.setStatus(statusReq.getStatus());
        return toRes(deviceMapper.updateById(update));
    }

    @Operation(summary = "删除设备")
    @OperateLog(title = "设备管理", subTitle = "删除设备", operateType = OperateType.UPDATE)
    @PostMapping("/remove")
    @CheckPerms("device:remove")
    public Result<Void> deleteById(@RequestBody Long deviceId) {
        return toRes(deviceMapper.deleteById(deviceId));
    }

}
