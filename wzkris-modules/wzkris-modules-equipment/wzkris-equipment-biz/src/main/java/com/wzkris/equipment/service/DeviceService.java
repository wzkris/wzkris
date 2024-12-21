package com.wzkris.equipment.service;

import com.wzkris.equipment.domain.vo.DeviceVO;
import com.wzkris.equipment.domain.vo.NetworkVO;

/**
 * (Device)表服务接口
 *
 * @author wzkris
 * @since 2023-06-05 10:38:53
 */
public interface DeviceService {

    /**
     * 设备ID查询详情
     *
     * @param deviceId 设备ID
     * @return result
     */
    DeviceVO getVOById(Long deviceId);

    /**
     * 设备ID查询网络信息
     *
     * @param deviceId 设备ID
     * @return result
     */
    NetworkVO getNetInfoBySno(Long deviceId);

}
