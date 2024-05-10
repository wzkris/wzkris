package com.thingslink.equipment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.equipment.domain.Device;
import com.thingslink.equipment.domain.vo.DeviceVO;
import com.thingslink.equipment.mapper.DeviceMapper;
import com.thingslink.equipment.mapper.StationMapper;
import com.thingslink.equipment.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Device)表服务实现类
 *
 * @author wzkris
 * @since 2023-06-05 10:38:53
 */
@Service("equipmentService")
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceMapper deviceMapper;
    private final StationMapper stationMapper;

    @Override
    public List<Device> list(Device device) {
        LambdaQueryWrapper<Device> lqw = this.buildQueryWrapper(device);

        return deviceMapper.selectList(lqw);
    }

    @Override
    public DeviceVO getVOById(Long deviceId) {
        return deviceMapper.selectVOById(deviceId);
    }

    private LambdaQueryWrapper<Device> buildQueryWrapper(Device device) {
        return new LambdaQueryWrapper<Device>()
                .like(StringUtil.isNotBlank(device.getDeviceName()), Device::getDeviceName, device.getDeviceName())
                .like(StringUtil.isNotBlank(device.getSerialNo()), Device::getSerialNo, device.getSerialNo())
                .eq(StringUtil.isNotBlank(device.getStatus()), Device::getStatus, device.getStatus())
                .eq(StringUtil.isNotBlank(device.getVersion()), Device::getVersion, device.getVersion())
                ;
    }
}
