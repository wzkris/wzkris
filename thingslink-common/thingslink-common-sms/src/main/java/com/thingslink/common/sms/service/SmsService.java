package com.thingslink.common.sms.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thingslink.common.sms.enums.SmsEnums;

import java.util.Collection;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 短信服务
 * @date : 2023/5/13 8:17
 */
public interface SmsService {

    /**
     * 发送短信
     *
     * @param phone  手机号
     * @param enums  替换参数
     * @param params 发送参数
     * @return 成功或失败
     */
    boolean send(String phone, SmsEnums enums, ObjectNode params);


    /**
     * 批量发送短信
     *
     * @param phones 手机号
     * @param enums  替换参数
     * @param params 发送参数
     * @return 成功或失败
     */
    boolean batchSend(Collection<String> phones, SmsEnums enums, ObjectNode params);
}
