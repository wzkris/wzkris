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
     * 设备号查询详情
     *
     * @param sno 设备号
     * @return result
     */
    DeviceVO getVOBySno(String sno);

    /**
     * 设备号查询网络信息
     *
     * @param sno 设备号
     * @return result
     */
    NetworkVO getNetInfoBySno(String sno);

}
