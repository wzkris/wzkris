package com.wzkris.equipment.service;

import java.util.List;

public interface StationService {

    /**
     * 设备绑定电站
     *
     * @param stationId 电站ID
     * @param deviceIds 设备ID集合
     */
    void bindDevice(Long stationId, List<Long> deviceIds);

    /**
     * 设备解绑绑定电站
     *
     * @param stationId 电站ID
     * @param deviceIds 设备ID集合
     */
    void unbindDevice(Long stationId, List<Long> deviceIds);

    /**
     * 站点是否被绑定
     *
     * @param stationId 站点
     * @return 结果
     */
    boolean checkStationUsed(Long stationId);

}
