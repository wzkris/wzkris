package com.thingslink.equipment.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.redis.util.RedisUtil;
import com.thingslink.equipment.constants.DeviceConstant;
import com.thingslink.equipment.domain.Device;
import com.thingslink.equipment.domain.Station;
import com.thingslink.equipment.domain.vo.DeviceVO;
import com.thingslink.equipment.domain.vo.NetworkVO;
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
        DeviceVO deviceVO = deviceMapper.selectVoById(deviceId, DeviceVO.class);
        if (ObjUtil.isNotNull(deviceVO.getStationId())) {
            Station station = stationMapper.selectById(deviceVO.getStationId());
            deviceVO.setStationName(station.getStationName());
        }
        return deviceVO;
    }

    @Override
    public NetworkVO getNetworkVOBySerialNo(String serialNo) {
        // 查询入网后的信息
        return null;
    }

    @Override
    public String subDevice(String serialNo) {
        String roomNo = IdUtil.fastSimpleUUID();
        RedisUtil.setObj(DeviceConstant.ROOM_PREFIX + roomNo, serialNo, 60 * 60);// 默认一个小时
        return roomNo;
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
