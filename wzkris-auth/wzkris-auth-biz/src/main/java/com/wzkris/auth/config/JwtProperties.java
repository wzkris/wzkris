package com.wzkris.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : JWT参数
 * @date : 2025/06/06 09:40
 */
@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * 多密钥配置（新版）
     * key: 密钥ID
     * value: 密钥配置
     */
    private Map<String, KeyConfig> keys = new HashMap<>();

    @Data
    public static class KeyConfig {

        private String algorithm;

        private String jwtAlgorithm;

        private String issuer;

        private String publicKey;

        private String privateKey;

        public PublicKey getPublicKey() {
            try {
                String pem = publicKey
                        .replace("-----BEGIN PUBLIC KEY-----", "")
                        .replace("-----END PUBLIC KEY-----", "")
                        .replaceAll("\\s", "");

                byte[] encoded = Base64.getDecoder().decode(pem);
                KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
                return keyFactory.generatePublic(keySpec);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                throw new IllegalArgumentException(e);
            }
        }

        public PrivateKey getPrivateKey() {
            try {
                String privateKeyPem = privateKey
                        .replace("-----BEGIN PRIVATE KEY-----", "")
                        .replace("-----END PRIVATE KEY-----", "")
                        .replaceAll("\\s", "");

                byte[] encoded = Base64.getDecoder().decode(privateKeyPem);
                KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
                return keyFactory.generatePrivate(keySpec);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

}
