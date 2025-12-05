package com.wzkris.common.apikey.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RequestSignerUtil 测试类
 *
 * @author wzkris
 */
@DisplayName("请求签名工具类测试")
class RequestSignerUtilTest {

    private static final String SECRET_KEY = "test-secret-key-12345";
    private static final String TRACE_ID = "test-trace-id-12345";
    private static final String REQUEST_BODY = "{\"name\":\"test\",\"value\":123}";
    private static final long TIMESTAMP = System.currentTimeMillis();
    private static final long INTERVAL = 300000; // 5分钟

    @Test
    @DisplayName("测试生成签名 - 正常情况")
    void testGenerateSignature_Normal() {
        String signature = RequestSignerUtil.generateSignature(SECRET_KEY, TRACE_ID, REQUEST_BODY, TIMESTAMP);
        
        assertNotNull(signature);
        assertFalse(signature.isEmpty());
        // Base64编码的签名应该只包含Base64字符
        assertTrue(signature.matches("^[A-Za-z0-9+/=]+$"));
    }

    @Test
    @DisplayName("测试生成签名 - 空请求体")
    void testGenerateSignature_EmptyBody() {
        String signature1 = RequestSignerUtil.generateSignature(SECRET_KEY, TRACE_ID, "", TIMESTAMP);
        String signature2 = RequestSignerUtil.generateSignature(SECRET_KEY, TRACE_ID, null, TIMESTAMP);
        
        assertNotNull(signature1);
        assertNotNull(signature2);
        assertEquals(signature1, signature2);
    }

    @Test
    @DisplayName("测试生成签名 - 相同输入产生相同签名")
    void testGenerateSignature_Consistency() {
        String signature1 = RequestSignerUtil.generateSignature(SECRET_KEY, TRACE_ID, REQUEST_BODY, TIMESTAMP);
        String signature2 = RequestSignerUtil.generateSignature(SECRET_KEY, TRACE_ID, REQUEST_BODY, TIMESTAMP);
        
        assertEquals(signature1, signature2);
    }

    @Test
    @DisplayName("测试生成签名 - 不同密钥产生不同签名")
    void testGenerateSignature_DifferentSecret() {
        String signature1 = RequestSignerUtil.generateSignature(SECRET_KEY, TRACE_ID, REQUEST_BODY, TIMESTAMP);
        String signature2 = RequestSignerUtil.generateSignature("different-secret", TRACE_ID, REQUEST_BODY, TIMESTAMP);
        
        assertNotEquals(signature1, signature2);
    }

    @Test
    @DisplayName("测试生成签名 - 不同请求体产生不同签名")
    void testGenerateSignature_DifferentBody() {
        String signature1 = RequestSignerUtil.generateSignature(SECRET_KEY, TRACE_ID, REQUEST_BODY, TIMESTAMP);
        String signature2 = RequestSignerUtil.generateSignature(SECRET_KEY, TRACE_ID, "{\"different\":\"body\"}", TIMESTAMP);
        
        assertNotEquals(signature1, signature2);
    }

    @Test
    @DisplayName("测试验证签名 - 有效签名")
    void testVerifySignature_Valid() {
        String path = "/api/test";
        String signature = RequestSignerUtil.generateSignature(SECRET_KEY, path, REQUEST_BODY, TIMESTAMP);
        boolean isValid = RequestSignerUtil.verifySignature(SECRET_KEY, path, REQUEST_BODY, TIMESTAMP, signature, INTERVAL);
        
        assertTrue(isValid);
    }

    @Test
    @DisplayName("测试验证签名 - 无效签名")
    void testVerifySignature_Invalid() {
        String path = "/api/test";
        String signature = RequestSignerUtil.generateSignature(SECRET_KEY, path, REQUEST_BODY, TIMESTAMP);
        String wrongSignature = signature + "wrong";
        boolean isValid = RequestSignerUtil.verifySignature(SECRET_KEY, path, REQUEST_BODY, TIMESTAMP, wrongSignature, INTERVAL);
        
        assertFalse(isValid);
    }

    @Test
    @DisplayName("测试验证签名 - 错误密钥")
    void testVerifySignature_WrongSecret() {
        String path = "/api/test";
        String signature = RequestSignerUtil.generateSignature(SECRET_KEY, path, REQUEST_BODY, TIMESTAMP);
        boolean isValid = RequestSignerUtil.verifySignature("wrong-secret", path, REQUEST_BODY, TIMESTAMP, signature, INTERVAL);
        
        assertFalse(isValid);
    }

    @Test
    @DisplayName("测试验证签名 - 时间戳过期")
    void testVerifySignature_ExpiredTimestamp() {
        String path = "/api/test";
        long expiredTimestamp = System.currentTimeMillis() - (INTERVAL + 1000); // 超过允许间隔
        String signature = RequestSignerUtil.generateSignature(SECRET_KEY, path, REQUEST_BODY, expiredTimestamp);
        boolean isValid = RequestSignerUtil.verifySignature(SECRET_KEY, path, REQUEST_BODY, expiredTimestamp, signature, INTERVAL);
        
        assertFalse(isValid);
    }

    @Test
    @DisplayName("测试验证签名 - 未来时间戳")
    void testVerifySignature_FutureTimestamp() {
        String path = "/api/test";
        long futureTimestamp = System.currentTimeMillis() + (INTERVAL + 1000); // 未来时间
        String signature = RequestSignerUtil.generateSignature(SECRET_KEY, path, REQUEST_BODY, futureTimestamp);
        boolean isValid = RequestSignerUtil.verifySignature(SECRET_KEY, path, REQUEST_BODY, futureTimestamp, signature, INTERVAL);
        
        assertFalse(isValid);
    }

    @Test
    @DisplayName("测试验证签名 - 在允许时间范围内")
    void testVerifySignature_WithinInterval() {
        String path = "/api/test";
        long recentTimestamp = System.currentTimeMillis() - (INTERVAL / 2); // 在允许范围内
        String signature = RequestSignerUtil.generateSignature(SECRET_KEY, path, REQUEST_BODY, recentTimestamp);
        boolean isValid = RequestSignerUtil.verifySignature(SECRET_KEY, path, REQUEST_BODY, recentTimestamp, signature, INTERVAL);
        
        assertTrue(isValid);
    }

    @Test
    @DisplayName("测试setCommonHeaders - 正常情况")
    void testSetCommonHeaders_Normal() {
        Map<String, String> headers = new HashMap<>();
        
        RequestSignerUtil.setCommonHeaders(
                headers::put,
                TRACE_ID,
                "test-app",
                SECRET_KEY,
                REQUEST_BODY,
                TIMESTAMP
        );
        
        assertTrue(headers.containsKey("X-Tracing-Id"));
        assertTrue(headers.containsKey("X-Request-Time"));
        assertTrue(headers.containsKey("X-Request-From"));
        assertTrue(headers.containsKey("X-Request-Sign"));
        assertEquals(TRACE_ID, headers.get("X-Tracing-Id"));
        assertEquals("test-app", headers.get("X-Request-From"));
        assertEquals(String.valueOf(TIMESTAMP), headers.get("X-Request-Time"));
        assertNotNull(headers.get("X-Request-Sign"));
    }

    @Test
    @DisplayName("测试签名算法 - 验证HMAC-SHA256")
    void testSignatureAlgorithm() {
        // 使用已知的输入和密钥，验证签名算法正确性
        String secret = "secret";
        String path = "/api/test";
        String body = "body";
        long timestamp = 1234567890L;
        
        String signature = RequestSignerUtil.generateSignature(secret, path, body, timestamp);
        
        // 验证签名可以正确验证
        boolean isValid = RequestSignerUtil.verifySignature(secret, path, body, timestamp, signature, Long.MAX_VALUE);
        assertTrue(isValid);
    }

    @Test
    @DisplayName("测试特殊字符处理")
    void testSpecialCharacters() {
        String path = "/api/test";
        String specialBody = "{\"name\":\"测试\",\"value\":\"特殊字符!@#$%^&*()\"}";
        String signature = RequestSignerUtil.generateSignature(SECRET_KEY, path, specialBody, TIMESTAMP);
        
        assertNotNull(signature);
        boolean isValid = RequestSignerUtil.verifySignature(SECRET_KEY, path, specialBody, TIMESTAMP, signature, INTERVAL);
        assertTrue(isValid);
    }

    @Test
    @DisplayName("测试长请求体")
    void testLongRequestBody() {
        String path = "/api/test";
        StringBuilder longBody = new StringBuilder("{");
        for (int i = 0; i < 1000; i++) {
            longBody.append("\"key").append(i).append("\":\"value").append(i).append("\"");
            if (i < 999) {
                longBody.append(",");
            }
        }
        longBody.append("}");
        
        String signature = RequestSignerUtil.generateSignature(SECRET_KEY, path, longBody.toString(), TIMESTAMP);
        boolean isValid = RequestSignerUtil.verifySignature(SECRET_KEY, path, longBody.toString(), TIMESTAMP, signature, INTERVAL);
        
        assertTrue(isValid);
    }
}

