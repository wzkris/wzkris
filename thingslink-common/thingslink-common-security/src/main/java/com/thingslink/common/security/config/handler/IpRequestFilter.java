package com.thingslink.common.security.config.handler;

import cn.hutool.core.net.NetUtil;
import com.thingslink.common.core.constant.SecurityConstants;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.core.enums.BizCode;
import com.thingslink.common.core.utils.ServletUtil;
import com.thingslink.common.core.utils.json.JsonUtil;
import com.thingslink.common.security.config.white.PermitIpConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author wzkris
 * @description ip过滤器
 * @date 2024-04-11
 */
@AllArgsConstructor
public class IpRequestFilter extends OncePerRequestFilter {

    private final PermitIpConfig ipConfig;
    private final static LinkedHashSet<String> localV6;
    private final static LinkedHashSet<String> localV4;

    static {
        localV6 = NetUtil.localIpv6s();
        localV4 = NetUtil.localIpv4s();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (ipConfig.getEnable() != null && ipConfig.getEnable()) {
            String headerIp = request.getHeader(SecurityConstants.GATEWAY_IP);
            // 请求头为空代表网关转发，从request里获取
            if (headerIp == null) {
                headerIp = ServletUtil.getClientIP(request);
            }
            if (!isPermitIp(headerIp, ipConfig.getIpList())) {
                responseJson(response, Result.resp(BizCode.FORBID, "该ip不允许访问"));
                return;
            }
        }
        // 继续过滤器链
        filterChain.doFilter(request, response);
    }

    /**
     * true代表允许放行
     */
    public static boolean isPermitIp(String ip, List<String> ipList) {
        return localV6.contains(ip) || localV4.contains(ip) || ipList.contains(ip);
    }

    /**
     * 处理响应
     */
    private void responseJson(HttpServletResponse res, Object obj) throws IOException {
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.setCharacterEncoding(StandardCharsets.UTF_8.name());

        try (PrintWriter writer = res.getWriter()) {
            writer.write(JsonUtil.toJsonString(obj));
        }
    }
}
