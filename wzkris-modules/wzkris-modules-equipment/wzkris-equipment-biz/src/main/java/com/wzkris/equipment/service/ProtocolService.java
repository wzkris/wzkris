package com.wzkris.equipment.service;

public interface ProtocolService {

    /**
     * @param ptcId 协议ID
     * @return 是否使用
     */
    boolean checkProtocolUsed(Long ptcId);
}
