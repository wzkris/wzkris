package com.wzkris.common.captcha.handler;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256Helper {

    private static final MessageDigest digest;

    static {
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 计算字符串的SHA-256哈希值并转换为十六进制字符串
     *
     * @param input 输入字符串
     * @return SHA-256哈希的十六进制表示
     */
    public static String sha256Hex(String input) {
        // 获取SHA-256消息摘要实例

        // 计算哈希值
        byte[] hash = digest.digest(input.getBytes());

        // 将字节数组转换为十六进制字符串
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = String.format("%02x", b);
            hexString.append(hex);
        }

        return hexString.toString();
    }

}
