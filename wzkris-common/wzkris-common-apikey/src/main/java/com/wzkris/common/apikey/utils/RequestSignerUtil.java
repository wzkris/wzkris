package com.wzkris.common.apikey.utils;

import com.wzkris.common.core.constant.HeaderConstants;
import com.wzkris.common.core.utils.TraceIdUtil;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.function.BiConsumer;

/**
 * 请求签名工具
 */
public class RequestSignerUtil {

    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private static final String DELIMITER = "\n"; // 分隔符

    public static void setCommonHeaders(BiConsumer<String, String> headerSetter,
                                        String applicationName,
                                        String secret,
                                        String requestBody,
                                        long timestamp) {
        final String traceId = TraceIdUtil.get();
        headerSetter.accept(HeaderConstants.X_TRACING_ID, traceId);
        headerSetter.accept(HeaderConstants.X_REQUEST_TIME, String.valueOf(timestamp));
        headerSetter.accept(HeaderConstants.X_REQUEST_FROM, applicationName);
        headerSetter.accept(HeaderConstants.X_REQUEST_SIGN,
                RequestSignerUtil.generateSignature(secret, traceId, requestBody, timestamp));
    }

    /**
     * 生成请求签名
     *
     * @param secretKey 外部传入的密钥（需保密）
     * @param traceId   请求ID
     * @param body      请求体（JSON/XML等，可为空）
     * @param timestamp 时间戳（毫秒）
     * @return Base64编码的签名字符串
     */
    public static String generateSignature(
            String secretKey,
            String traceId,
            String body,
            long timestamp
    ) {
        try {
            // 1. 构建待签名字符串：traceID + TIMESTAMP + BODY
            String dataToSign = traceId + DELIMITER + timestamp + DELIMITER + (body != null ? body : "");

            // 2. 使用HMAC-SHA256计算签名
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM);
            mac.init(secretKeySpec);
            byte[] rawSignature = mac.doFinal(dataToSign.getBytes(StandardCharsets.UTF_8));

            // 3. Base64编码签名结果
            return Base64.getEncoder().encodeToString(rawSignature);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to generate signature", e);
        }
    }

    /**
     * 验证签名是否合法
     *
     * @param secretKey 外部传入的密钥（需与生成签名时一致）
     * @param path      请求路径
     * @param body      请求体
     * @param timestamp 请求时间戳
     * @param signature 待验证的签名
     * @param interval  调用允许的时间间隔
     * @return true=验证通过，false=验证失败
     */
    public static boolean verifySignature(
            String secretKey,
            String path,
            String body,
            long timestamp,
            String signature,
            long interval
    ) {
        // 1. 检查时间戳是否在有效期内（防重放攻击）
        long currentTime = System.currentTimeMillis();
        if (Math.abs(currentTime - timestamp) > interval) {
            return false;
        }

        // 2. 重新生成签名并比对
        String expectedSignature = generateSignature(secretKey, path, body, timestamp);
        return expectedSignature.equals(signature);
    }

}
