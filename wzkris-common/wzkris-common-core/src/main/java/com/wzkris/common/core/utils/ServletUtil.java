package com.wzkris.common.core.utils;

import com.wzkris.common.core.exception.util.UtilException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 客户端工具类
 *
 * @author wzkris
 */
@Slf4j
public class ServletUtil {

    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
                    "(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$");

    private static final Pattern IPV6_STD_PATTERN = Pattern.compile(
            "^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");

    private static final Pattern IPV6_HEX_COMPRESSED_PATTERN = Pattern.compile(
            "^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");

    /**
     * 获得所有请求参数
     *
     * @param request 请求对象{@link ServletRequest}
     * @return Map
     */
    public static Map<String, String[]> getParams(ServletRequest request) {
        final Map<String, String[]> map = request.getParameterMap();
        return Collections.unmodifiableMap(map);
    }

    /**
     * 获得所有请求参数
     *
     * @param request 请求对象{@link ServletRequest}
     * @return Map
     */
    public static Map<String, String> getParamMap(ServletRequest request) {
        Map<String, String> params = new HashMap<>();
        for (Map.Entry<String, String[]> entry : getParams(request).entrySet()) {
            params.put(entry.getKey(), String.join(StringUtil.COMMA, entry.getValue()));
        }
        return params;
    }

    /**
     * 获取请求体<br>
     * 调用该方法后，getParam方法将失效
     *
     * @param request {@link ServletRequest}
     * @return 获得请求体
     * @since 4.0.2
     */
    public static String getBody(ServletRequest request) throws IOException {
        try (final BufferedReader reader = request.getReader()) {
            final StringBuilder builder = new StringBuilder();
            final CharBuffer buffer = CharBuffer.allocate(2 << 12);
            while (-1 != reader.read(buffer)) {
                builder.append(buffer.flip());
            }
            return builder.toString();
        }
    }

    /**
     * 获取客户端IP
     *
     * <p>
     * 默认检测的Header:
     *
     * <pre>
     * 1、X-Forwarded-For
     * 2、X-Real-IP
     * 3、Proxy-Client-IP
     * 4、WL-Proxy-Client-IP
     * </pre>
     *
     * <p>
     * otherHeaderNames参数用于自定义检测的Header<br>
     * 需要注意的是，使用此方法获取的客户IP地址必须在Http服务器（例如Nginx）中配置头信息，否则容易造成IP伪造。
     * </p>
     *
     * @param request          请求对象{@link HttpServletRequest}
     * @param otherHeaderNames 其他自定义头文件，通常在Http服务器（例如Nginx）中配置
     * @return IP地址
     */
    public static String getClientIP(HttpServletRequest request, String... otherHeaderNames) {
        String[] headers = {
                "X-Forwarded-For",
                "X-Real-IP",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR"
        };
        if (ArrayUtils.isNotEmpty(otherHeaderNames)) {
            headers = ArrayUtils.addAll(headers, otherHeaderNames);
        }

        return getClientIPByHeader(request, headers);
    }

    /**
     * 获取客户端IP
     *
     * <p>
     * headerNames参数用于自定义检测的Header<br>
     * 需要注意的是，使用此方法获取的客户IP地址必须在Http服务器（例如Nginx）中配置头信息，否则容易造成IP伪造。
     * </p>
     *
     * @param request     请求对象{@link HttpServletRequest}
     * @param headerNames 自定义头，通常在Http服务器（例如Nginx）中配置
     * @return IP地址
     * @since 4.4.1
     */
    public static String getClientIPByHeader(HttpServletRequest request, String... headerNames) {
        String ip;
        for (String header : headerNames) {
            ip = request.getHeader(header);
            if (isValidIpCandidate(ip)) {
                return getFirstNonProxyIp(ip);
            }
        }

        return request.getRemoteAddr();
    }

    /**
     * 判断字符串是否为有效的IP候选值
     */
    private static boolean isValidIpCandidate(String ip) {
        return StringUtils.isNotBlank(ip) &&
                !"unknown".equalsIgnoreCase(ip.trim()) &&
                !"127.0.0.1".equals(ip.trim()) &&
                !"0:0:0:0:0:0:0:1".equals(ip.trim());
    }

    /**
     * 从多级代理IP列表中提取第一个非代理IP
     */
    private static String getFirstNonProxyIp(String ip) {
        // 如果有多个IP（如多级代理），通常用逗号分隔
        if (ip != null && ip.contains(",")) {
            String[] ips = ip.split(",");
            for (String candidate : ips) {
                candidate = candidate.trim();
                if (isValidIp(candidate) && !isPrivateIp(candidate)) {
                    return candidate;
                }
            }
        }

        // 如果没有逗号分隔或没有有效IP，返回原始值
        return ip;
    }

    /**
     * 验证是否为有效的IP地址（IPv4或IPv6）
     */
    private static boolean isValidIp(String ip) {
        if (StringUtils.isBlank(ip)) {
            return false;
        }
        ip = ip.trim();

        if (ip.contains(":")) {
            // 可能是IPv6
            return IPV6_STD_PATTERN.matcher(ip).matches() ||
                    IPV6_HEX_COMPRESSED_PATTERN.matcher(ip).matches();
        } else {
            // 可能是IPv4
            return IPV4_PATTERN.matcher(ip).matches();
        }
    }

    /**
     * 判断是否为私有IP地址
     */
    private static boolean isPrivateIp(String ip) {
        if (ip == null || !isValidIp(ip)) {
            return false;
        }

        // IPv4私有地址范围
        if (ip.contains(".")) {
            String[] parts = ip.split("\\.");
            int firstOctet = Integer.parseInt(parts[0]);

            if (firstOctet == 10) return true; // 10.0.0.0/8
            if (firstOctet == 172) {
                int secondOctet = Integer.parseInt(parts[1]);
                return secondOctet >= 16 && secondOctet <= 31; // 172.16.0.0/12
            }
            if (firstOctet == 192) {
                int secondOctet = Integer.parseInt(parts[1]);
                return secondOctet == 168; // 192.168.0.0/16
            }
        }

        // IPv6链路本地地址
        return ip.startsWith("fe80:");
    }

    // --------------------------------------------------------- Header start

    /**
     * 获取请求所有的头（header）信息
     *
     * @param request 请求对象{@link HttpServletRequest}
     * @return header值
     * @since 4.6.2
     */
    public static Map<String, String> getHeaderMap(HttpServletRequest request) {
        final Map<String, String> headerMap = new HashMap<>();

        final Enumeration<String> names = request.getHeaderNames();
        String name;
        while (names.hasMoreElements()) {
            name = names.nextElement();
            headerMap.put(name, request.getHeader(name));
        }

        return headerMap;
    }

    /**
     * 获取请求所有的头（header）信息
     *
     * @param request 请求对象{@link HttpServletRequest}
     * @return header值
     * @since 6.0.0
     */
    public static Map<String, List<String>> getHeadersMap(final HttpServletRequest request) {
        final Map<String, List<String>> headerMap = new LinkedHashMap<>();

        final Enumeration<String> names = request.getHeaderNames();
        String name;
        while (names.hasMoreElements()) {
            name = names.nextElement();
            headerMap.put(name, Collections.list(request.getHeaders(name)));
        }

        return headerMap;
    }

    /**
     * 获取响应所有的头（header）信息
     *
     * @param response 响应对象{@link HttpServletResponse}
     * @return header值
     */
    public static Map<String, Collection<String>> getHeadersMap(HttpServletResponse response) {
        final Map<String, Collection<String>> headerMap = new HashMap<>();

        final Collection<String> names = response.getHeaderNames();
        for (String name : names) {
            headerMap.put(name, response.getHeaders(name));
        }

        return headerMap;
    }

    /**
     * 忽略大小写获得请求header中的信息
     *
     * @param request        请求对象{@link HttpServletRequest}
     * @param nameIgnoreCase 忽略大小写头信息的KEY
     * @return header值
     */
    public static String getHeaderIgnoreCase(HttpServletRequest request, String nameIgnoreCase) {
        final Enumeration<String> names = request.getHeaderNames();
        String name;
        while (names.hasMoreElements()) {
            name = names.nextElement();
            if (name != null && name.equalsIgnoreCase(nameIgnoreCase)) {
                return request.getHeader(name);
            }
        }

        return null;
    }

    /**
     * 获得请求header中的信息
     *
     * @param name 头信息的KEY
     * @return header值
     */
    public static String getHeader(HttpServletRequest request, String name) {
        return getHeader(request, name, StandardCharsets.UTF_8);
    }

    /**
     * 获得请求header中的信息
     *
     * @param request     请求对象{@link HttpServletRequest}
     * @param name        头信息的KEY
     * @param charsetName 字符集
     * @return header值
     */
    public static String getHeader(HttpServletRequest request, String name, String charsetName) {
        return getHeader(request, name, Charset.forName(charsetName));
    }

    /**
     * 获得请求header中的信息
     *
     * @param request 请求对象{@link HttpServletRequest}
     * @param name    头信息的KEY
     * @param charset 字符集
     * @return header值
     * @since 4.6.2
     */
    public static String getHeader(final HttpServletRequest request, String name, Charset charset) {
        if (null == request) {
            return null;
        }
        final String header = request.getHeader(name);
        if (null == header) {
            return null;
        }
        return new String(header.getBytes(StandardCharsets.ISO_8859_1), charset);
    }

    /**
     * 客户浏览器是否为IE
     *
     * @param request 请求对象{@link HttpServletRequest}
     * @return 客户浏览器是否为IE
     */
    public static boolean isIE(HttpServletRequest request) {
        String userAgent = getHeaderIgnoreCase(request, "User-Agent");
        if (StringUtil.isNotBlank(userAgent)) {
            //noinspection ConstantConditions
            userAgent = userAgent.toUpperCase();
            return userAgent.contains("MSIE") || userAgent.contains("TRIDENT");
        }
        return false;
    }

    // --------------------------------------------------------- Header end

    // --------------------------------------------------------- Cookie start

    /**
     * 获得指定的Cookie
     *
     * @param httpServletRequest {@link HttpServletRequest}
     * @param name               cookie名
     * @return Cookie对象
     */
    public static Cookie getCookie(HttpServletRequest httpServletRequest, String name) {
        return readCookieMap(httpServletRequest).get(name);
    }

    /**
     * 将cookie封装到Map里面
     *
     * @param httpServletRequest {@link HttpServletRequest}
     * @return Cookie map
     */
    public static Map<String, Cookie> readCookieMap(HttpServletRequest httpServletRequest) {
        final Cookie[] cookies = httpServletRequest.getCookies();
        if (ArrayUtils.isEmpty(cookies)) {
            return new HashMap<>();
        }

        Map<String, Cookie> map = new HashMap<>();
        for (Cookie cookie : cookies) {
            map.put(cookie.getName(), cookie);
        }

        return map;
    }

    /**
     * 设定返回给客户端的Cookie
     *
     * @param response 响应对象{@link HttpServletResponse}
     * @param cookie   Servlet Cookie对象
     */
    public static void addCookie(HttpServletResponse response, Cookie cookie) {
        response.addCookie(cookie);
    }

    /**
     * 设定返回给客户端的Cookie
     *
     * @param response 响应对象{@link HttpServletResponse}
     * @param name     Cookie名
     * @param value    Cookie值
     */
    public static void addCookie(HttpServletResponse response, String name, String value) {
        response.addCookie(new Cookie(name, value));
    }

    /**
     * 设定返回给客户端的Cookie
     *
     * @param response        响应对象{@link HttpServletResponse}
     * @param name            cookie名
     * @param value           cookie值
     * @param maxAgeInSeconds -1: 关闭浏览器清除Cookie. 0: 立即清除Cookie. &gt;0 : Cookie存在的秒数.
     * @param path            Cookie的有效路径
     * @param domain          the domain name within which this cookie is visible; form is according to RFC 2109
     */
    public static void addCookie(
            HttpServletResponse response, String name, String value, int maxAgeInSeconds, String path, String domain) {
        Cookie cookie = new Cookie(name, value);
        if (domain != null) {
            cookie.setDomain(domain);
        }
        cookie.setMaxAge(maxAgeInSeconds);
        cookie.setPath(path);
        addCookie(response, cookie);
    }

    /**
     * 设定返回给客户端的Cookie<br>
     * Path: "/"<br>
     * No Domain
     *
     * @param response        响应对象{@link HttpServletResponse}
     * @param name            cookie名
     * @param value           cookie值
     * @param maxAgeInSeconds -1: 关闭浏览器清除Cookie. 0: 立即清除Cookie. &gt;0 : Cookie存在的秒数.
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAgeInSeconds) {
        addCookie(response, name, value, maxAgeInSeconds, "/", null);
    }

    // --------------------------------------------------------- Cookie end
    // --------------------------------------------------------- Response start

    /**
     * 返回数据给客户端
     *
     * @param response    响应对象{@link HttpServletResponse}
     * @param text        返回的内容
     * @param contentType 返回的类型
     */
    public static void write(HttpServletResponse response, String text, String contentType) {
        response.setContentType(contentType);
        try (Writer writer = response.getWriter()) {
            writer.write(text);
            writer.flush();
        } catch (IOException e) {
            throw new UtilException(e.getMessage());
        }
    }

    /**
     * 设置响应的Header
     *
     * @param response 响应对象{@link HttpServletResponse}
     * @param name     名
     * @param value    值，可以是String，Date， int
     */
    public static void setHeader(HttpServletResponse response, String name, Object value) {
        if (value instanceof String) {
            response.setHeader(name, (String) value);
        } else if (Date.class.isAssignableFrom(value.getClass())) {
            response.setDateHeader(name, ((Date) value).getTime());
        } else if (value instanceof Integer
                || "int".equalsIgnoreCase(value.getClass().getSimpleName())) {
            response.setIntHeader(name, (int) value);
        } else {
            response.setHeader(name, value.toString());
        }
    }
    // --------------------------------------------------------- Response end
}
