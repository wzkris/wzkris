package com.wzkris.common.excel.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.excel.annotation.ExcelDictFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典格式化转换处理, 配合@ExcelDictFormat使用
 *
 * @author Lion Li
 */
@Slf4j
public class ExcelDictConvert implements Converter<Object> {

    @Override
    public Class<Object> supportJavaTypeKey() {
        return Object.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return null;
    }

    @Override
    public Object convertToJavaData(
            ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        ExcelDictFormat anno = getAnnotation(contentProperty.getField());
        String label = cellData.getStringValue();
        return reverseByExp(label, anno.readConverterExp(), anno.separator());
    }

    // 扩展支持list字典翻译
    @Override
    public WriteCellData<String> convertToExcelData(
            Object object, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if (ObjectUtils.isEmpty(object)) {
            return new WriteCellData<>("");
        }
        String value;
        if (object instanceof List<?> list) {
            value = list.stream().map(String::valueOf).collect(Collectors.joining(","));
        } else {
            value = String.valueOf(object);
        }
        ExcelDictFormat anno = getAnnotation(contentProperty.getField());
        String label = convertByExp(value, anno.readConverterExp(), anno.separator());
        return new WriteCellData<>(label);
    }

    private ExcelDictFormat getAnnotation(Field field) {
        return AnnotationUtils.getAnnotation(field, ExcelDictFormat.class);
    }

    /**
     * 解析导出值 0=男,1=女,2=未知
     *
     * @param propertyValue 参数值
     * @param converterExp  翻译注解
     * @param separator     分隔符
     * @return 解析后值
     */
    private String convertByExp(String propertyValue, String converterExp, String separator) {
        if (StringUtil.isBlank(propertyValue) || StringUtil.isBlank(converterExp)) {
            return propertyValue;
        }
        StringBuilder propertyString = new StringBuilder();
        String[] convertSource = converterExp.split(",");
        for (String item : convertSource) {
            String[] itemArray = item.split("=", 2);
            if (itemArray.length != 2) {
                continue;
            }
            if (StringUtil.containsAny(propertyValue, separator)) {
                for (String value : propertyValue.split(separator)) {
                    if (itemArray[0].equals(value)) {
                        propertyString.append(itemArray[1]).append(separator);
                        break;
                    }
                }
            } else {
                if (itemArray[0].equals(propertyValue)) {
                    return itemArray[1];
                }
            }
        }
        return StringUtil.removeEnd(propertyString.toString(), separator);
    }

    /**
     * 反向解析值 男=0,女=1,未知=2
     *
     * @param propertyValue 参数值
     * @param converterExp  翻译注解
     * @param separator     分隔符
     * @return 解析后值
     */
    private String reverseByExp(String propertyValue, String converterExp, String separator) {
        if (StringUtil.isBlank(propertyValue) || StringUtil.isBlank(converterExp)) {
            return propertyValue;
        }
        StringBuilder propertyString = new StringBuilder();
        String[] convertSource = converterExp.split(",");
        for (String item : convertSource) {
            String[] itemArray = item.split("=", 2);
            if (itemArray.length != 2) {
                continue;
            }
            if (StringUtil.containsAny(propertyValue, separator)) {
                for (String value : propertyValue.split(separator)) {
                    if (itemArray[1].equals(value)) {
                        propertyString.append(itemArray[0]).append(separator);
                        break;
                    }
                }
            } else {
                if (itemArray[1].equals(propertyValue)) {
                    return itemArray[0];
                }
            }
        }
        return StringUtil.removeEnd(propertyString.toString(), separator);
    }

}
