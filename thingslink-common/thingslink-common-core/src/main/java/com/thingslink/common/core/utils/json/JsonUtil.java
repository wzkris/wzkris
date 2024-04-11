package com.thingslink.common.core.utils.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thingslink.common.core.exception.UtilException;
import com.thingslink.common.core.utils.SpringUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : json工具类
 * @date : 2023/1/2 8:37
 */
@Slf4j
public class JsonUtil {

    @Getter
    private static final ObjectMapper objectMapper = SpringUtil.getBean(ObjectMapper.class);

    /**
     * 对象转Json格式字符串
     *
     * @param obj 对象
     * @return Json格式字符串
     */
    public static String toJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        }
        catch (Exception e) {
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
        }
        catch (JsonProcessingException e) {
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
        }
        catch (IOException e) {
            log.error("convert error, errorMsg:{}", e.getMessage(), e);
            throw new UtilException("util.json.error");
        }
    }

    /**
     * Json 转为 Java Bean
     *
     * @param text  json字符串
     * @param clazz 对象类型class
     * @param <T>   对象类型
     * @return 对象类型
     */
    public static <T> T parseObject(String text, Class<T> clazz) {
        try {
            return objectMapper.readValue(text, clazz);
        }
        catch (Exception e) {
            log.error("convert error, errorMsg:{}", e.getMessage(), e);
            throw new UtilException("util.json.error");
        }
    }

    /**
     * Json 转为 Java Bean
     *
     * @param obj   对象
     * @param clazz 对象类型
     * @return bean
     */
    public static <T> T parseObject(Object obj, Class<T> clazz) {
        try {
            return objectMapper.convertValue(obj, clazz);
        }
        catch (Exception e) {
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
        }
        catch (Exception e) {
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
        }
        catch (Exception e) {
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
        }
        catch (IOException e) {
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
