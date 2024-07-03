package com.thingslink.equipment.domain.dto;

import lombok.Data;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 设备消息传输体
 * @date : 2023/12/4 16:39
 */
@Data
public class MessageDTO {
    // sn号
    private String serialNo;
    // 产品key
    private String productKey;
    // 数据
    private byte[] data;

    public MessageDTO() {
    }

    public MessageDTO(String serialNo, String productKey, byte[] data) {
        this.serialNo = serialNo;
        this.productKey = productKey;
        this.data = data;
    }
}
