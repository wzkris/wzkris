package com.thingslink.equipment.service.impl;

import com.thingslink.equipment.domain.Device;
import com.thingslink.equipment.domain.Station;
import com.thingslink.equipment.domain.vo.DeviceVO;
import com.thingslink.equipment.mapper.DeviceMapper;
import com.thingslink.equipment.mapper.StationMapper;
import com.thingslink.equipment.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    /**
     * 返回设备vo
     *
     * @param device 筛选条件
     * @return 设备展示层
     */
    @Override
    public List<DeviceVO> listVO(Device device) {
        List<DeviceVO> deviceVOS = deviceMapper.listVO(device);
        List<Long> stationIds = deviceVOS.stream().map(Device::getStationId).collect(Collectors.toList());
        Map<Long, String> nameMap = stationMapper.listByIds(stationIds)
                .stream()
                .collect(Collectors.toMap(Station::getStationId, Station::getStationName));
        for (DeviceVO deviceVO : deviceVOS) {
            deviceVO.setStationName(nameMap.get(deviceVO.getStationId()));
        }
        return deviceVOS;
    }
}
