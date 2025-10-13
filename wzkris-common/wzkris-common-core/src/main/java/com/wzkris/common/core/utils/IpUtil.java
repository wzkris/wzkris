package com.wzkris.common.core.utils;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * ip工具
 *
 * @author wzkris
 */
@Slf4j
public abstract class IpUtil {

    /**
     * IP地址查询
     */
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp?ip=%s&json=true";

    public static final String UNKNOWN = "未知地址";

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(3))
            .build();

    /**
     * 地址响应速度会达到秒级
     *
     * @param ip IP地址
     * @return 根据ip获取真实物理地址
     */
    public static String parseIp(String ip) {
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(String.format(IP_URL, ip)))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                log.error("获取地理位置异常, ip：{}, 响应体：{}", ip, response);
            }
            JsonNode nodes = JsonUtil.readTree(response.body());
            String region = nodes.get("pro").asText();
            String city = nodes.get("city").asText();
            String location = String.format("%s%s", region, city);
            if (StringUtil.isBlank(location)) {
                location = nodes.get("addr").asText();
            }
            if (log.isDebugEnabled()) {
                log.debug("获取到地理位置：{}", location);
            }
            return location;
        } catch (Exception e) {
            log.error("获取地理位置异常，ip：{}，errMsg：{}", ip, e.getMessage());
        }
        return UNKNOWN;
    }

}
