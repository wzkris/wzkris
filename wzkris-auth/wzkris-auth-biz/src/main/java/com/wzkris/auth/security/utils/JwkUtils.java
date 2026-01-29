package com.wzkris.auth.security.utils;

import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * jwk工具类
 *
 * @author wzkris
 */
public class JwkUtils {

    /**
     * 加载 RSA 密钥对
     * <p>
     * 注意：使用 keyIDFromThumbprint() 自动生成 kid（Key ID）。
     * 这会基于 JWK 的 SHA-256 thumbprint（RFC 7638）生成 kid。
     * </p>
     *
     * @param publicKeyStr  公钥字符串（PEM 格式）
     * @param privateKeyStr 私钥字符串（PEM 格式）
     * @return RSAKey 实例（包含自动生成的 kid）
     * @throws Exception 加载失败时抛出异常
     */
    public static RSAKey load(String publicKeyStr, String privateKeyStr) throws Exception {
        // 从字符串内容加载RSA公钥
        byte[] publicKeyBytes = Base64.getDecoder().decode(
                publicKeyStr
                        .replace("-----BEGIN PUBLIC KEY-----", "")
                        .replaceAll(System.lineSeparator(), "")
                        .replace("-----END PUBLIC KEY-----", ""));
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);

        // 从字符串内容加载RSA私钥
        byte[] privateKeyBytes = Base64.getDecoder().decode(
                privateKeyStr
                        .replace("-----BEGIN PRIVATE KEY-----", "")
                        .replaceAll(System.lineSeparator(), "")
                        .replace("-----END PRIVATE KEY-----", ""));
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);

        // 使用构建器创建RSAKey实例
        // 使用 keyIDFromThumbprint() 自动生成 kid（基于 JWK 的 SHA-256 thumbprint）
        RSAKey.Builder builder = new RSAKey.Builder(publicKey)
                .keyUse(KeyUse.SIGNATURE)
                .privateKey(privateKey)
                .keyIDFromThumbprint(); // 自动生成 kid

        return builder.build();
    }

}
