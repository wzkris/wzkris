package com.thingslink.common.sms.service.impl;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.teaopenapi.models.Config;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.core.utils.json.JsonUtil;
import com.thingslink.common.sms.config.AliyunSmsConfig;
import com.thingslink.common.sms.enums.SmsEnums;
import com.thingslink.common.sms.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Collection;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 阿里云短信实现类
 * @date : 2023/5/13 8:23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliyunSmsServiceImpl implements SmsService {

    private final AliyunSmsConfig aliyunSmsConfig;

    private static Client client;

    @PostConstruct
    public void init() throws Exception {
        Config config = new Config();
        config.setAccessKeyId(aliyunSmsConfig.getAccessKey());
        config.setAccessKeySecret(aliyunSmsConfig.getAccessSecret());
        config.setEndpoint("dysmsapi.aliyuncs.com");
        client = new Client(config);
    }

    @Override
    public boolean send(String phone, SmsEnums enums, ObjectNode params) {
        SendSmsRequest request = new SendSmsRequest()
                .setPhoneNumbers(phone)
                .setSignName(aliyunSmsConfig.getSignName())
                .setTemplateCode(enums.getTemplateCode())
                .setTemplateParam(JsonUtil.toJsonString(params));

        try {
            SendSmsResponse response = client.sendSms(request);
            SendSmsResponseBody smsBody = response.getBody();
            log.info("{Code: {}, Message: {}, RequestId: {}, BizId:{}"
                    , smsBody.getCode()
                    , smsBody.getMessage()
                    , smsBody.getRequestId()
                    , smsBody.getBizId());
            if (StringUtil.equals("OK", smsBody.getCode())) {
                return true;
            }
        } catch (Exception e) {
            log.error("发送阿里云短信发生异常，异常信息：{}", e.getMessage());
            return false;
        }
        return false;
    }

    @Override
    public boolean batchSend(Collection<String> phones, SmsEnums enums, ObjectNode params) {
        return false;
    }
}
