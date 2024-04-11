package org.example;

import cn.hutool.crypto.SecureUtil;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.util.Base64;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    public void test() {
        KeyPair keyPair = SecureUtil.generateKeyPair("RSA/ECB/PKCS1Padding");
        System.out.println(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));

        System.out.println("-----");
        System.out.println(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
    }
}
