package com.wzkris.common.core.utils;

import com.wzkris.common.core.constant.CustomHeaderConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TraceIdUtil 测试类
 *
 * @author wzkris
 */
@DisplayName("TraceId工具类测试")
class TraceIdUtilTest {

    @AfterEach
    void tearDown() {
        TraceIdUtil.clear();
    }

    @Test
    @DisplayName("测试设置TraceId")
    void testSet() {
        String traceId = "test-trace-id-12345";
        TraceIdUtil.set(traceId);
        
        assertEquals(traceId, MDC.get(CustomHeaderConstants.X_TRACING_ID));
    }

    @Test
    @DisplayName("测试设置TraceId - 空字符串")
    void testSet_EmptyString() {
        String originalTraceId = "original-trace-id";
        TraceIdUtil.set(originalTraceId);
        TraceIdUtil.set("");
        
        // 空字符串不应该设置
        assertEquals(originalTraceId, MDC.get(CustomHeaderConstants.X_TRACING_ID));
    }

    @Test
    @DisplayName("测试设置TraceId - null值")
    void testSet_Null() {
        String originalTraceId = "original-trace-id";
        TraceIdUtil.set(originalTraceId);
        TraceIdUtil.set(null);
        
        // null值不应该设置
        assertEquals(originalTraceId, MDC.get(CustomHeaderConstants.X_TRACING_ID));
    }

    @Test
    @DisplayName("测试设置Hint")
    void testSetHint() {
        String hint = "test-hint-12345";
        TraceIdUtil.setHint(hint);
        
        assertEquals(hint, MDC.get(CustomHeaderConstants.X_ROUTE_HINT));
    }

    @Test
    @DisplayName("测试设置Hint - 空字符串")
    void testSetHint_EmptyString() {
        String originalHint = "original-hint";
        TraceIdUtil.setHint(originalHint);
        TraceIdUtil.setHint("");
        
        // 空字符串不应该设置
        assertEquals(originalHint, MDC.get(CustomHeaderConstants.X_ROUTE_HINT));
    }

    @Test
    @DisplayName("测试设置Hint - null值")
    void testSetHint_Null() {
        String originalHint = "original-hint";
        TraceIdUtil.setHint(originalHint);
        TraceIdUtil.setHint(null);
        
        // null值不应该设置
        assertEquals(originalHint, MDC.get(CustomHeaderConstants.X_ROUTE_HINT));
    }

    @Test
    @DisplayName("测试清除MDC")
    void testClear() {
        TraceIdUtil.set("test-trace-id");
        TraceIdUtil.setHint("test-hint");
        
        TraceIdUtil.clear();
        
        assertNull(MDC.get(CustomHeaderConstants.X_TRACING_ID));
        assertNull(MDC.get(CustomHeaderConstants.X_ROUTE_HINT));
    }

    @Test
    @DisplayName("测试获取TraceId")
    void testGet() {
        String traceId = "test-trace-id-12345";
        TraceIdUtil.set(traceId);
        
        assertEquals(traceId, TraceIdUtil.get());
    }

    @Test
    @DisplayName("测试获取TraceId - 不存在时返回null")
    void testGet_NotExists() {
        TraceIdUtil.clear();
        
        assertNull(TraceIdUtil.get());
    }

    @Test
    @DisplayName("测试获取或生成TraceId - 已存在")
    void testGetOrGenerate_Exists() {
        String traceId = "existing-trace-id";
        TraceIdUtil.set(traceId);
        
        String result = TraceIdUtil.getOrGenerate();
        
        assertEquals(traceId, result);
    }

    @Test
    @DisplayName("测试获取或生成TraceId - 不存在时生成")
    void testGetOrGenerate_NotExists() {
        TraceIdUtil.clear();
        
        String result = TraceIdUtil.getOrGenerate();
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        // 生成的TraceId应该符合格式：yyyyMMddHHmmssSSS-sequence-random
        assertTrue(result.matches("^\\d{17}-\\d+-\\d+$"));
    }

    @Test
    @DisplayName("测试生成TraceId")
    void testGenerate() {
        String traceId1 = TraceIdUtil.generate();
        String traceId2 = TraceIdUtil.generate();
        
        assertNotNull(traceId1);
        assertNotNull(traceId2);
        assertNotEquals(traceId1, traceId2);
        
        // 验证格式：yyyyMMddHHmmssSSS-sequence-random
        assertTrue(traceId1.matches("^\\d{17}-\\d+-\\d+$"));
        assertTrue(traceId2.matches("^\\d{17}-\\d+-\\d+$"));
    }

    @Test
    @DisplayName("测试生成TraceId - 唯一性")
    void testGenerate_Uniqueness() {
        String[] traceIds = new String[100];
        for (int i = 0; i < 100; i++) {
            traceIds[i] = TraceIdUtil.generate();
        }
        
        // 验证所有生成的TraceId都是唯一的
        for (int i = 0; i < traceIds.length; i++) {
            for (int j = i + 1; j < traceIds.length; j++) {
                assertNotEquals(traceIds[i], traceIds[j], 
                    "TraceId should be unique, but found duplicate at index " + i + " and " + j);
            }
        }
    }

    @Test
    @DisplayName("测试生成TraceId - 格式验证")
    void testGenerate_Format() {
        String traceId = TraceIdUtil.generate();
        
        // 格式：yyyyMMddHHmmssSSS-sequence-random
        String[] parts = traceId.split("-");
        assertEquals(3, parts.length);
        
        // 第一部分应该是17位数字（时间戳）
        assertEquals(17, parts[0].length());
        assertTrue(parts[0].matches("^\\d+$"));
        
        // 第二部分应该是序列号（数字）
        assertTrue(parts[1].matches("^\\d+$"));
        
        // 第三部分应该是随机数（7位数字）
        assertTrue(parts[2].matches("^\\d+$"));
        assertTrue(parts[2].length() <= 7);
    }

    @Test
    @DisplayName("测试完整流程")
    void testCompleteFlow() {
        // 1. 生成TraceId
        String traceId = TraceIdUtil.generate();
        assertNotNull(traceId);
        
        // 2. 设置TraceId
        TraceIdUtil.set(traceId);
        assertEquals(traceId, TraceIdUtil.get());
        
        // 3. 设置Hint
        String hint = "test-hint";
        TraceIdUtil.setHint(hint);
        assertEquals(hint, MDC.get(CustomHeaderConstants.X_ROUTE_HINT));
        
        // 4. 获取或生成（应该返回已存在的）
        String result = TraceIdUtil.getOrGenerate();
        assertEquals(traceId, result);
        
        // 5. 清除
        TraceIdUtil.clear();
        assertNull(TraceIdUtil.get());
        
        // 6. 清除后获取或生成（应该生成新的）
        String newTraceId = TraceIdUtil.getOrGenerate();
        assertNotNull(newTraceId);
        assertNotEquals(traceId, newTraceId);
    }
}

