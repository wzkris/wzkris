package com.wzkris.common.apikey.request;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 可重复读body的request
 *
 * @author wzkris
 */
public class RepeatableReadRequestWrapper extends HttpServletRequestWrapper {

    private static final String FORM_URL_ENCODED = "application/x-www-form-urlencoded";

    private final byte[] body;

    private final Map<String, String[]> formParameters;

    public RepeatableReadRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        // 一次性读取所有body内容
        this.body = readRequestBody(request);
        this.formParameters = shouldParseFormParameters(request) ? parseFormParameters(request) : null;
    }

    private boolean shouldParseFormParameters(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (contentType == null) {
            return false;
        }
        int separatorIndex = contentType.indexOf(';');
        if (separatorIndex >= 0) {
            contentType = contentType.substring(0, separatorIndex);
        }
        return FORM_URL_ENCODED.equalsIgnoreCase(contentType.trim());
    }

    private Map<String, String[]> parseFormParameters(HttpServletRequest request) throws UnsupportedEncodingException {
        String charsetName = request.getCharacterEncoding();
        Charset charset = charsetName != null ? Charset.forName(charsetName) : StandardCharsets.UTF_8;
        String bodyString = new String(this.body, charset);
        Map<String, List<String>> temp = new LinkedHashMap<>();
        for (String pair : bodyString.split("&")) {
            if (pair.isEmpty()) {
                continue;
            }
            String[] kv = pair.split("=", 2);
            String key = urlDecode(kv[0], charset);
            String value = kv.length > 1 ? urlDecode(kv[1], charset) : "";
            temp.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
        }
        Map<String, String[]> result = new LinkedHashMap<>(temp.size());
        temp.forEach((k, v) -> result.put(k, v.toArray(new String[0])));
        return Collections.unmodifiableMap(result);
    }

    private String urlDecode(String value, Charset charset) throws UnsupportedEncodingException {
        return URLDecoder.decode(value, charset.name());
    }

    private byte[] readRequestBody(HttpServletRequest request) throws IOException {
        try (InputStream inputStream = request.getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }

            return outputStream.toByteArray();
        }
    }

    @Override
    public ServletInputStream getInputStream() {
        return new ServletInputStream() {
            private final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream(),
                getCharacterEncoding() != null ? Charset.forName(getCharacterEncoding()) : StandardCharsets.UTF_8));
    }

    @Override
    public String getParameter(String name) {
        if (formParameters != null) {
            String[] values = formParameters.get(name);
            return values != null && values.length > 0 ? values[0] : null;
        }
        return super.getParameter(name);
    }

    @Override
    public String[] getParameterValues(String name) {
        if (formParameters != null) {
            return formParameters.get(name);
        }
        return super.getParameterValues(name);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        if (formParameters != null) {
            return Collections.enumeration(formParameters.keySet());
        }
        return super.getParameterNames();
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        if (formParameters != null) {
            return formParameters;
        }
        return super.getParameterMap();
    }

    public String getBodyAsString() {
        return new String(getBody(), StandardCharsets.UTF_8);
    }

    public final byte[] getBody() {
        return this.body;
    }

}