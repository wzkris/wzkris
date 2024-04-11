package com.thingslink.equipment.enums;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 设备状态
 * @date : 2023/6/10 14:14
 */
public enum DeviceStatus {
    ONLINE("在线"),
    OFFLINE("离线"),
    FAULT("故障"),
    FIX("维修");

    DeviceStatus(String desc) {}
}
