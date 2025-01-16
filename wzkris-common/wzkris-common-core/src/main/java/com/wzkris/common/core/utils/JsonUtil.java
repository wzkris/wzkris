package com.wzkris.common.core.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.wzkris.common.core.exception.UtilException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : json工具类
 * @date : 2023/1/2 8:37
 */
@Slf4j
public class JsonUtil {

    @Getter
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 配置MAPPER提升兼容性
     */
    static {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);// 允许空Bean序列化
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);// 允许不存在的属性反序列化
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);// 允许使用无引号字段
        objectMapper.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);// 忽略未定义的属性
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);// 是否允许使用注释
        objectMapper.configure(JsonReadFeature.ALLOW_TRAILING_COMMA.mappedFeature(), true);// 忽略json最后的逗号
        objectMapper.configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(), true);// 允许反斜杠
        objectMapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);// 允许出现特殊字符和转义符
        objectMapper.configure(JsonReadFeature.ALLOW_SINGLE_QUOTES.mappedFeature(), true);// 允许出现单引号
    }

    /**
     * 对象转Json格式字符串
     *
     * @param obj 对象
     * @return Json格式字符串
     */
    public static String toJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("convert error, errorMsg:{}", e.getMessage(), e);
            throw new UtilException("util.json.error");
        }
    }

    /**
     * 对象转字节
     *
     * @param obj 对象
     * @return 字节流
     */
    public static byte[] toBytes(Object obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            log.error("convert error, errorMsg:{}", e.getMessage(), e);
            throw new UtilException("util.json.error");
        }
    }

    /**
     * 将对象写入流
     */
    public static void writeValue(Writer writer, Object obj) {
        try {
            objectMapper.writeValue(writer, obj);
        } catch (IOException e) {
            log.error("convert error, errorMsg:{}", e.getMessage(), e);
            throw new UtilException("util.json.error");
        }
    }

    /**
     * obj 转 Java Bean
     *
     * @param obj   对象
     * @param clazz 对象类型
     * @return bean
     */
    public static <T> T parseObject(Object obj, Class<T> clazz) {
        try {
            return objectMapper.convertValue(obj, clazz);
        } catch (Exception e) {
            log.error("convert error, errorMsg:{}", e.getMessage(), e);
            throw new UtilException("util.json.error");
        }
    }

    /**
     * String转换成Map
     */
    public static <T> T toMap(String content, Class<? extends Map> mapClass,
                              Class<?> keyClass, Class<?> valueClass) {
        try {
            return objectMapper.readValue(content, TypeFactory.defaultInstance().constructMapType(mapClass, keyClass, valueClass));
        } catch (Exception e) {
            log.error("convert error, errorMsg:{}", e.getMessage(), e);
            throw new UtilException("util.json.error");
        }
    }

    /**
     * String转换成coll
     */
    public static <T> T toColl(String content, Class<? extends Collection> collectionClass, Class<?> elementClass) {
        try {
            return objectMapper.readValue(content, TypeFactory.defaultInstance().constructCollectionType(collectionClass, elementClass));
        } catch (Exception e) {
            log.error("convert error, errorMsg:{}", e.getMessage(), e);
            throw new UtilException("util.json.error");
        }
    }

    public static <T> T toColl(JsonParser parser, Class<? extends Collection> collectionClass, Class<?> elementClass) {
        try {
            return objectMapper.readValue(parser, TypeFactory.defaultInstance().constructCollectionType(collectionClass, elementClass));
        } catch (Exception e) {
            log.error("convert error, errorMsg:{}", e.getMessage(), e);
            throw new UtilException("util.json.error");
        }
    }

    /**
     * 流转换成java bean
     */
    public static <T> T parseObject(Reader reader, Class<T> clazz) {
        try {
            return objectMapper.readValue(reader, clazz);
        } catch (Exception e) {
            log.error("convert error, errorMsg:{}", e.getMessage(), e);
            throw new UtilException("util.json.error");
        }
    }

    /**
     * 字节换成java bean
     */
    public static <T> T parseObject(byte[] bytes, Class<T> clazz) {
        try {
            return objectMapper.readValue(bytes, clazz);
        } catch (Exception e) {
            log.error("convert error, errorMsg:{}", e.getMessage(), e);
            throw new UtilException("util.json.error");
        }
    }

    public static <T> T parseObject(JsonParser jsonParser, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonParser, clazz);
        } catch (IOException e) {
            log.error("convert error, errorMsg:{}", e.getMessage(), e);
            throw new UtilException("util.json.error");
        }
    }

    /**
     * @param text json字符串
     * @return 转为ObjectNode
     */
    public static ObjectNode parseNode(String text) {
        try {
            return objectMapper.readValue(text, ObjectNode.class);
        } catch (IOException e) {
            log.error("convert error, errorMsg:{}", e.getMessage(), e);
            throw new UtilException("util.json.error");
        }
    }

    public static ObjectNode parseNode(JsonParser jsonParser) {
        try {
            return objectMapper.readTree(jsonParser);
        } catch (IOException e) {
            log.error("convert error, errorMsg:{}", e.getMessage(), e);
            throw new UtilException("util.json.error");
        }
    }

    /**
     * 获取可以修改的node节点
     *
     * @return ObjectNode
     */
    public static ObjectNode createObjectNode() {
        return objectMapper.createObjectNode();
    }

}
