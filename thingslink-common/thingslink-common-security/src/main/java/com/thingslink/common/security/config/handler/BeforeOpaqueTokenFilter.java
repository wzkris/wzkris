package com.thingslink.common.security.config.handler;

import cn.hutool.core.net.NetUtil;
import com.thingslink.common.core.constant.SecurityConstants;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.core.enums.BizCode;
import com.thingslink.common.core.utils.ServletUtil;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.core.utils.json.JsonUtil;
import com.thingslink.common.security.config.white.PermitIpConfig;
import com.thingslink.common.security.model.LoginUser;
import com.thingslink.common.security.utils.CurrentUserHolder;
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
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author wzkris
 * @description 自省token之前执行
 * @date 2024-04-11
 */
@AllArgsConstructor
public class BeforeOpaqueTokenFilter extends OncePerRequestFilter {

    private final PermitIpConfig ipConfig;
    private final static LinkedHashSet<String> localV6;
    private final static LinkedHashSet<String> localV4;

    static {
        localV6 = NetUtil.localIpv6s();
        localV4 = NetUtil.localIpv4s();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 校验ip
        if (ipConfig.getEnable() != null && ipConfig.getEnable()) {
            String headerIp = request.getHeader(SecurityConstants.GATEWAY_IP_HEADER);
            // 请求头为空或非法则返回
            if (headerIp == null || !isPermitIp(headerIp, ipConfig.getIpList())) {
                responseJson(response, Result.resp(BizCode.FORBID, "该ip不允许访问"));
                return;
            }
        }

        // 拿到用户信息请求头，避免多次token自省
        String currentPrincipal = request.getHeader(SecurityConstants.PRINCIPAL_HEADER);
        if (StringUtil.isNotBlank(currentPrincipal)) {
            // 不为空说明经过一次调用，直接读取值
            LinkedHashMap<String, Object> resMap = JsonUtil.parseObject(currentPrincipal, LinkedHashMap.class);

            LoginUser loginUser = CustomOpaqueTokenIntrospector.buildAuthenticatedUser(resMap);
            CurrentUserHolder.setAuthentication(loginUser);
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
