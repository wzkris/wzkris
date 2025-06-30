package com.wzkris.common.excel.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.wzkris.common.core.utils.StringUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class ExcelListConverter implements Converter<List<?>> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return List.class;
    }

    @Override
    public WriteCellData<?> convertToExcelData(
            List<?> list, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if (CollectionUtils.isEmpty(list)) {
            return new WriteCellData<>(StringUtil.EMPTY);
        }
        String join = String.join(StringUtil.COMMA, (List<String>) list);
        return new WriteCellData<>(join);
    }

}
