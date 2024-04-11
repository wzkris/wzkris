package com.thingslink.equipment.controller;

import com.thingslink.common.core.domain.Result;
import com.thingslink.common.orm.page.Page;
import com.thingslink.common.web.controller.BaseController;
import com.thingslink.equipment.domain.Device;
import com.thingslink.equipment.domain.vo.DeviceVO;
import com.thingslink.equipment.mapper.DeviceMapper;
import com.thingslink.equipment.service.DeviceService;
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
    @PreAuthorize("hasAuthority('device:list')")
    public Result<Page<DeviceVO>> listPage(Device device) {
        startPage();
        List<DeviceVO> list = deviceService.listVO(device);
        return getDataTable(list);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param deviceId 主键
     * @return 单条数据
     */
    @GetMapping("/{deviceId}")
    @PreAuthorize("hasAuthority('device:query')")
    public Result<Device> query(@PathVariable Long deviceId) {
        return success(deviceMapper.getById(deviceId));
    }

    /**
     * 新增数据
     *
     * @param device 实体
     * @return 新增结果
     */
    @PostMapping
    @PreAuthorize("hasAuthority('device:add')")
    public Result<?> add(@RequestBody Device device) {
        return toRes(deviceMapper.insert(device));
    }

    /**
     * 编辑数据
     *
     * @param device 实体
     * @return 编辑结果
     */
    @PutMapping
    @PreAuthorize("hasAuthority('device:edit')")
    public Result<?> edit(@RequestBody Device device) {
        return toRes(deviceMapper.updateById(device));
    }

    /**
     * 删除数据
     *
     * @param deviceId 主键
     * @return 删除是否成功
     */
    @DeleteMapping("/{deviceId}")
    @PreAuthorize("hasAuthority('device:remove')")
    public Result<?> deleteById(@PathVariable Long deviceId) {
        return toRes(deviceMapper.deleteById(deviceId));
    }

}