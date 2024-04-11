package com.thingslink.common.third.dingtalk.utils;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thingslink.common.core.utils.json.JsonUtil;
import com.thingslink.common.third.dingtalk.config.DingTalkConfig;
import com.thingslink.common.third.dingtalk.domain.AccessToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 钉钉工具
 * @date : 2023/5/20 15:55
 */
@Slf4j
public class DingTalkUtil {

    private static RestTemplate restTemplate;

    private static DingTalkConfig dingTalkConfig;

    private static HttpHeaders jsonHeader = new HttpHeaders();

    private static final String HOST = "https://api.dingtalk.com";

    static {
        jsonHeader.setContentType(MediaType.APPLICATION_JSON);
        jsonHeader.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }


    /**
     * @return 获取企业应用内部的access_token
     */
    public static AccessToken getAccessToken() {
        //组装参数
        ObjectNode params = JsonUtil.createObjectNode();
        params.put(DingTalkConfig.Fields.appKey, dingTalkConfig.getAppKey());
        params.put(DingTalkConfig.Fields.appSecret, dingTalkConfig.getAppSecret());
        //拿到token
        return restTemplate.postForObject(HOST + "/v1.0/oauth2/accessToken", new HttpEntity<>(params, jsonHeader), AccessToken.class);
    }


}
