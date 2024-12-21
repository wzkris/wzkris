package com.wzkris.common.security.field;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

@DisplayName(value = "字段权限测试")
public class FieldPermsTest {

    static TestDomain testDomain = new TestDomain();

    static ObjectMapper objectMapper = new ObjectMapper();

    static {
        TestDefault testDefault = new TestDefault();
        testDefault.setIds(Arrays.asList(1L, 2L, 3L));
        testDomain.setId(123L);
        testDomain.setYoung(true);
        testDomain.setTestDefault(testDefault);
        testDomain.setTestDefaults(Collections.singletonList(testDefault));
    }

    @Test
    @DisplayName(value = "序列化测试")
    public void serialize() throws JsonProcessingException {
        String jsonString = objectMapper.writeValueAsString(testDomain);
        System.out.println(jsonString);
    }

    @Test
    @DisplayName(value = "反序列化测试")
    public void deserialize() throws JsonProcessingException {
        String str = """
                {"id":11,"young":true,"testDefault":{"ids":[1,2,3]},"testDefaults":[{"ids":[1,2,3]}]}
                """;
        TestDomain testDomain1 = objectMapper.readValue(str, TestDomain.class);
        System.out.println(testDomain1);
    }
}
