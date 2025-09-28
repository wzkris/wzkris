package com.wzkris.common.apikey.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 请求签名工具
 */
public class RequestSignerUtil {

    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private static final String DELIMITER = "|"; // 分隔符

    private static final long TIMESTAMP_THRESHOLD = 30 * 1000; // 30秒有效期

    /**
     * 生成请求签名
     *
     * @param secretKey 外部传入的密钥（需保密）
     * @param path      请求路径（如 "/api/v1/orders"）
     * @param body      请求体（JSON/XML等，可为空）
     * @param timestamp 时间戳（毫秒）
     * @return Base64编码的签名字符串
     */
    public static String generateSignature(
            String secretKey,
            String path,
            String body,
            long timestamp
    ) {
        try {
            // 1. 构建待签名字符串：PATH + TIMESTAMP + BODY
            String dataToSign = path + DELIMITER + timestamp + DELIMITER + (body != null ? body : "");

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
     * @return true=验证通过，false=验证失败
     */
    public static boolean verifySignature(
            String secretKey,
            String path,
            String body,
            long timestamp,
            String signature
    ) {
        // 1. 检查时间戳是否在有效期内（防重放攻击）
        long currentTime = System.currentTimeMillis();
        if (Math.abs(currentTime - timestamp) > TIMESTAMP_THRESHOLD) {
            return false;
        }

        // 2. 重新生成签名并比对
        String expectedSignature = generateSignature(secretKey, path, body, timestamp);
        return expectedSignature.equals(signature);
    }

}
