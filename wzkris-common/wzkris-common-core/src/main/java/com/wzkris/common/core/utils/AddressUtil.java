package com.wzkris.common.core.utils;

import cn.hutool.core.net.NetUtil;
import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * 获取地址类
 *
 * @author wzkris
 */
@Slf4j
public class AddressUtil {

    /**
     * IP地址查询
     */
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp?ip={}&json=true";

    // 未知地址
    public static final String UNKNOWN = "未知地址";

    /**
     * @param ip IP地址
     * @return 根据ip获取真实物理地址
     */
    public static String getRealAddressByIp(String ip) {
        // 内网不查询
        if (StringUtil.equals("0:0:0:0:0:0:0:1", ip) || NetUtil.isInnerIP(ip)) {
            return "内网IP";
        }
        try {
            String res = HttpUtil.get(StringUtil.format(IP_URL, ip), StandardCharsets.UTF_8);
            if (StringUtil.isEmpty(res)) {
                log.error("获取地理位置异常，ip：{}", ip);
                return UNKNOWN;
            }
            ObjectNode objectNode = JsonUtil.parseNode(res);
            String region = objectNode.get("pro").asText();
            String city = objectNode.get("city").asText();
            String location = String.format("%s%s", region, city);
            if (StringUtil.isBlank(location)) {
                location = UNKNOWN;
            }
            log.info("获取到地理位置：{}", location);
            return location;
        } catch (Exception e) {
            log.error("获取地理位置异常，ip：{}，errMsg：{}", ip, e.getMessage());
        }
        return UNKNOWN;
    }
}
