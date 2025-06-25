package com.wzkris.common.excel.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.excel.annotation.ExcelEnumFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * 枚举格式化转换处理
 *
 * @author Liang
 */
@Slf4j
public class ExcelEnumConvert implements Converter<Object> {

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
            ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        cellData.checkEmpty();
        // Excel中填入的是枚举中指定的描述
        Object textValue =
                switch (cellData.getType()) {
                    case STRING, DIRECT_STRING, RICH_TEXT_STRING -> cellData.getStringValue();
                    case NUMBER -> cellData.getNumberValue();
                    case BOOLEAN -> cellData.getBooleanValue();
                    default -> throw new IllegalArgumentException("单元格类型异常!");
                };
        // 如果是空值
        if (ObjectUtils.isEmpty(textValue)) {
            return null;
        }
        Map<Object, String> enumCodeToTextMap = beforeConvert(contentProperty);
        // 从Java输出至Excel是code转text
        // 因此从Excel转Java应该将text与code对调
        Map<Object, Object> enumTextToCodeMap = new HashMap<>();
        enumCodeToTextMap.forEach((key, value) -> enumTextToCodeMap.put(value, key));
        // 应该从text -> code中查找
        return enumTextToCodeMap.get(textValue);
    }

    @Override
    public WriteCellData<String> convertToExcelData(
            Object object, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (ObjectUtils.isEmpty(object)) {
            return new WriteCellData<>(StringUtil.EMPTY);
        }
        Map<Object, String> enumValueMap = beforeConvert(contentProperty);
        String value = StringUtil.defaultIfBlank(enumValueMap.get(object), StringUtil.EMPTY);
        return new WriteCellData<>(value);
    }

    private Map<Object, String> beforeConvert(ExcelContentProperty contentProperty) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        ExcelEnumFormat anno = getAnnotation(contentProperty.getField());
        Map<Object, String> enumValueMap = new HashMap<>();
        Enum<?>[] enumConstants = anno.enumClass().getEnumConstants();
        for (Enum<?> enumConstant : enumConstants) {
            Object codeValue = MethodUtils.invokeMethod(
                    enumConstant,
                    anno.codeField(),
                    (Object[]) null  // 无参数
            );

            // 使用 MethodUtils 调用 textField 方法
            String textValue = (String) MethodUtils.invokeMethod(
                    enumConstant,
                    anno.textField(),
                    (Object[]) null  // 无参数
            );
            enumValueMap.put(codeValue, textValue);
        }
        return enumValueMap;
    }

    private ExcelEnumFormat getAnnotation(Field field) {
        return AnnotationUtils.getAnnotation(field, ExcelEnumFormat.class);
    }

}
