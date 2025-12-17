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
        return new RSAKey.Builder(publicKey)
                .keyUse(KeyUse.SIGNATURE)
                .privateKey(privateKey)
                .build();
    }

}
