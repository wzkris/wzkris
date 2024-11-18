package com.wzkris.equipment.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.security.utils.SysUtil;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.equipment.domain.Device;
import com.wzkris.equipment.domain.req.DeviceQueryReq;
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
                .eq(StringUtil.isNotBlank(req.getConnStatus()), Device::getConnStatus, req.getConnStatus());
    }

    @Operation(summary = "详情")
    @GetMapping("/{deviceId}")
    @PreAuthorize("@ps.hasPerms('device:query')")
    public Result<DeviceVO> queryById(@PathVariable Long deviceId) {
        return ok(deviceService.getVOById(deviceId));
    }

    @Operation(summary = "设备号查询入网信息")
    @GetMapping("/network_detail/{serialNo}")
    @PreAuthorize("@ps.hasPerms('device:query')")
    public Result<NetworkVO> queryNetwork(@PathVariable String serialNo) {
        return ok(deviceService.getNetworkVOBySerialNo(serialNo));
    }

    @Operation(summary = "订阅设备信息")
    @PostMapping("/sub_dev")
    @PreAuthorize("@ps.hasPerms('device:sub_dev')")
    public Result<String> subInfo(@RequestBody Long deviceId) {
        Device device = deviceMapper.selectOne(new QueryWrapper<>(new Device(deviceId).setTenantId(SysUtil.getTenantId())));
        if (device == null) {
            return fail("设备不存在");
        }

        String roomNo = deviceService.subDevice(device.getSerialNo());
        return ok(roomNo);
    }

    @Operation(summary = "添加设备")
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('device:add')")
    public Result<?> add(@RequestBody Device device) {
        return toRes(deviceMapper.insert(device));
    }

    @Operation(summary = "修改设备")
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('device:edit')")
    public Result<?> edit(@RequestBody Device device) {
        return toRes(deviceMapper.updateById(device));
    }

    @Operation(summary = "删除设备")
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('device:remove')")
    public Result<?> deleteById(@PathVariable Long deviceId) {
        return toRes(deviceMapper.deleteById(deviceId));
    }

}
