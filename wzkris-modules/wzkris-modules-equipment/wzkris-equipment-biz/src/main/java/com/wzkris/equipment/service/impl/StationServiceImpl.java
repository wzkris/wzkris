package com.wzkris.equipment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.equipment.domain.Device;
import com.wzkris.equipment.domain.Station;
import com.wzkris.equipment.mapper.DeviceMapper;
import com.wzkris.equipment.mapper.StationMapper;
import com.wzkris.equipment.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StationServiceImpl implements StationService {
    private final DeviceMapper deviceMapper;
    private final StationMapper stationMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindDevice(Long stationId, List<Long> deviceIds) {
        Station station = stationMapper.selectByIdForUpdate(stationId);
        List<Device> list = deviceIds.stream().map(id -> {
            Device device = new Device(id);
            device.setStationId(stationId);
            device.setLatitude(station.getLatitude());
            device.setLongitude(station.getLongitude());
            return device;
        }).toList();
        deviceMapper.updateById(list);
    }

    @Override
    public void unbindDevice(Long stationId, List<Long> deviceIds) {
        LambdaUpdateWrapper<Device> luw = Wrappers.lambdaUpdate(Device.class)
                .eq(Device::getStationId, stationId)
                .in(Device::getDeviceId, deviceIds)
                .set(Device::getStationId, null)
                .set(Device::getLatitude, null)
                .set(Device::getLongitude, null);
        deviceMapper.update(luw);
    }

    @Override
    public boolean checkStationUsed(Long stationId) {
        LambdaQueryWrapper<Device> lqw = Wrappers.lambdaQuery(Device.class)
                .eq(Device::getStationId, stationId);
        return deviceMapper.exists(lqw);
    }
}
