package com.wzkris.common.excel.convert;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.wzkris.common.core.utils.StringUtil;

import java.util.List;

public class ExcelListConverter implements Converter<List<?>> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return List.class;
    }

    @Override
    public WriteCellData<?> convertToExcelData(List<?> list, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (ObjectUtil.isNull(list)) {
            return new WriteCellData<>("");
        }
        String join = StringUtil.join(",", list);
        return new WriteCellData<>(join);
    }
}
