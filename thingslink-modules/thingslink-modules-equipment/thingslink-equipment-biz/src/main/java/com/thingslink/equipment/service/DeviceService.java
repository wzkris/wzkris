package com.thingslink.equipment.service;

import com.thingslink.equipment.domain.Device;
import com.thingslink.equipment.domain.vo.DeviceVO;
import com.thingslink.equipment.domain.vo.NetworkVO;

import java.util.List;

/**
 * (Device)表服务接口
 *
 * @author wzkris
 * @since 2023-06-05 10:38:53
 */
public interface DeviceService {

    List<Device> list(Device device);

    DeviceVO getVOById(Long deviceId);

    NetworkVO getNetworkVOBySerialNo(String serialNo);

    // 订阅设备消息
    String subDevice(String serialNo);
}
