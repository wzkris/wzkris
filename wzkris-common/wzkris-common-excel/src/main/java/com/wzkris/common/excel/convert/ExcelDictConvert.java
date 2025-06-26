package com.wzkris.common.excel.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.wzkris.common.excel.annotation.ExcelDictFormat;
import com.wzkris.common.excel.utils.ExcelUtil;
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
        return ExcelUtil.reverseByExp(label, anno.readConverterExp(), anno.separator());
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
        String label = ExcelUtil.convertByExp(value, anno.readConverterExp(), anno.separator());
        return new WriteCellData<>(label);
    }

    private ExcelDictFormat getAnnotation(Field field) {
        return AnnotationUtils.getAnnotation(field, ExcelDictFormat.class);
    }

}
