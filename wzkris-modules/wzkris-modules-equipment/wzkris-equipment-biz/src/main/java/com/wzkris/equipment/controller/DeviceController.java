package com.wzkris.equipment.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.security.utils.SysUtil;
import com.wzkris.equipment.domain.Device;
import com.wzkris.equipment.domain.vo.DeviceVO;
import com.wzkris.equipment.domain.vo.NetworkVO;
import com.wzkris.equipment.mapper.DeviceMapper;
import com.wzkris.equipment.service.DeviceService;
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

    /**
     * 分页查询
     *
     * @param device 筛选条件
     * @return 查询结果
     */
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('device:list')")
    public Result<Page<Device>> listPage(Device device) {
        startPage();
        List<Device> list = deviceService.list(device);
        return getDataTable(list);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param deviceId 主键
     * @return 单条数据
     */
    @GetMapping("/{deviceId}")
    @PreAuthorize("@ps.hasPerms('device:query')")
    public Result<DeviceVO> queryById(@PathVariable Long deviceId) {
        return ok(deviceService.getVOById(deviceId));
    }

    /**
     * 通过设备号查询设备入网信息
     *
     * @param serialNo 设备号
     * @return 单条数据
     */
    @GetMapping("/network_detail/{serialNo}")
    @PreAuthorize("@ps.hasPerms('device:query')")
    public Result<NetworkVO> queryNetwork(@PathVariable String serialNo) {
        return ok(deviceService.getNetworkVOBySerialNo(serialNo));
    }

    /**
     * 订阅设备信息
     *
     * @param deviceId 主键
     * @return 房间号
     */
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

    /**
     * 新增数据
     *
     * @param device 实体
     * @return 新增结果
     */
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('device:add')")
    public Result<?> add(@RequestBody Device device) {
        return toRes(deviceMapper.insert(device));
    }

    /**
     * 编辑数据
     *
     * @param device 实体
     * @return 编辑结果
     */
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('device:edit')")
    public Result<?> edit(@RequestBody Device device) {
        return toRes(deviceMapper.updateById(device));
    }

    /**
     * 删除数据
     *
     * @param deviceId 主键
     * @return 删除是否成功
     */
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('device:remove')")
    public Result<?> deleteById(@PathVariable Long deviceId) {
        return toRes(deviceMapper.deleteById(deviceId));
    }

}
