package com.thingslink.equipment.service;

import com.thingslink.equipment.domain.Device;
import com.thingslink.equipment.domain.vo.DeviceVO;

import java.util.List;

/**
 * (Device)表服务接口
 *
 * @author wzkris
 * @since 2023-06-05 10:38:53
 */
public interface DeviceService {

    /**
     * 返回设备vo
     *
     * @param device 筛选条件
     * @return 设备展示层
     */
    List<DeviceVO> listVO(Device device);
}
