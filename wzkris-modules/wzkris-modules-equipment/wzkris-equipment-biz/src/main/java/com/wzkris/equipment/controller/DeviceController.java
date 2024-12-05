package com.wzkris.equipment.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.equipment.domain.Device;
import com.wzkris.equipment.domain.req.DeviceQueryReq;
import com.wzkris.equipment.domain.req.EditStatusReq;
import com.wzkris.equipment.domain.vo.DeviceVO;
import com.wzkris.equipment.domain.vo.NetworkVO;
import com.wzkris.equipment.mapper.DeviceMapper;
import com.wzkris.equipment.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("@ps.hasPerms('device:list')")
    public Result<Page<Device>> listPage(DeviceQueryReq req) {
        startPage();
        List<Device> list = deviceMapper.selectList(this.buildQueryWrapper(req));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<Device> buildQueryWrapper(DeviceQueryReq req) {
        return new LambdaQueryWrapper<Device>()
                .like(StringUtil.isNotBlank(req.getDeviceName()), Device::getDeviceName, req.getDeviceName())
                .like(StringUtil.isNotBlank(req.getSerialNo()), Device::getSerialNo, req.getSerialNo())
                .eq(StringUtil.isNotBlank(req.getStatus()), Device::getStatus, req.getStatus())
                .eq(StringUtil.isNotBlank(req.getConnStatus()), Device::getConnStatus, req.getConnStatus())
                .last("ORDER BY CASE conn_status WHEN '1' THEN 1 ELSE 2 END , device_id DESC");
    }

    @Operation(summary = "id查询设备信息")
    @GetMapping("/{deviceId}")
    @PreAuthorize("@ps.hasPerms('device:query')")
    public Result<Device> queryById(@PathVariable Long deviceId) {
        return ok(deviceMapper.selectById(deviceId));
    }

    @Operation(summary = "设备号查询详情")
    @GetMapping("/by_sno/{sno}")
    @PreAuthorize("@ps.hasPerms('device:query')")
    public Result<DeviceVO> queryBySno(@PathVariable String sno) {
        return ok(deviceService.getVOBySno(sno));
    }

    @Operation(summary = "设备号查询入网信息")
    @GetMapping("/network_detail/{sno}")
    @PreAuthorize("@ps.hasPerms('device:query')")
    public Result<NetworkVO> queryNetwork(@PathVariable String sno) {
        return ok(deviceService.getNetInfoBySno(sno));
    }

    @Operation(summary = "添加设备")
    @OperateLog(title = "设备管理", subTitle = "添加设备", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('device:add')")
    public Result<Void> add(@RequestBody Device device) {
        return toRes(deviceMapper.insert(device));
    }

    @Operation(summary = "修改设备")
    @OperateLog(title = "设备管理", subTitle = "修改设备", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('device:edit')")
    public Result<Void> edit(@RequestBody Device device) {
        return toRes(deviceMapper.updateById(device));
    }

    @Operation(summary = "修改设备状态")
    @OperateLog(title = "设备管理", subTitle = "修改设备状态", operateType = OperateType.UPDATE)
    @PostMapping("/edit_status")
    @PreAuthorize("@ps.hasPerms('device:edit')")
    public Result<Void> editStatus(@RequestBody EditStatusReq statusReq) {
        Device update = new Device(statusReq.getId());
        update.setStatus(statusReq.getStatus());
        return toRes(deviceMapper.updateById(update));
    }

    @Operation(summary = "删除设备")
    @OperateLog(title = "设备管理", subTitle = "删除设备", operateType = OperateType.UPDATE)
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('device:remove')")
    public Result<Void> deleteById(@RequestBody Long deviceId) {
        return toRes(deviceMapper.deleteById(deviceId));
    }

}
