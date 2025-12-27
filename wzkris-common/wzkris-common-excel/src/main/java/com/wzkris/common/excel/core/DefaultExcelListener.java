package com.wzkris.common.excel.core;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.core.utils.SpringUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Excel 导入监听
 *
 * @author Yjoioooo
 * @author Lion Li
 */
@Slf4j
public class DefaultExcelListener<T> extends AnalysisEventListener<T> implements ExcelListener<T> {

    /**
     * 是否进行Validator校验
     * 当isValidate=true时，在invoke方法中对数据进行Bean Validation校验
     */
    private final boolean isValidate;

    /**
     * 导入回执
     */
    private final ExcelResult<T> excelResult;

    /**
     * excel 表头数据
     */
    private Map<Integer, String> headMap;

    /**
     * Validator实例（延迟加载，可选依赖）
     */
    private Validator validator;

    public DefaultExcelListener(boolean isValidate) {
        this.isValidate = isValidate;
        this.excelResult = new DefaultExcelResult<>();
    }

    /**
     * 处理异常
     *
     * @param exception ExcelDataConvertException
     * @param context   Excel 上下文
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        String errMsg = "解析异常";
        if (exception instanceof ExcelDataConvertException excelDataConvertException) {
            // 如果是某一个单元格的转换异常 能获取到具体行号
            Integer rowIndex = excelDataConvertException.getRowIndex();
            Integer columnIndex = excelDataConvertException.getColumnIndex();
            String headerName = headMap != null ? headMap.get(columnIndex) : "未知列";
            errMsg = String.format("第%s行-第%s列-表头%s: 解析异常<br/>",
                    rowIndex + 1, columnIndex + 1, headerName);
            log.error("Excel解析异常: {}", errMsg);
        } else {
            log.error("Excel解析异常", exception);
        }
        excelResult.getErrorList().add(errMsg);
        throw new ExcelAnalysisException(errMsg);
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        this.headMap = headMap;
        log.debug("解析到一条表头数据: {}", JsonUtil.toJsonString(headMap));
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        // 如果需要校验，则进行Bean Validation校验
        if (isValidate && data != null) {
            validateData(data, context);
        }
        excelResult.getList().add(data);
    }

    /**
     * 校验数据
     *
     * @param data    数据对象
     * @param context Excel上下文
     */
    private void validateData(T data, AnalysisContext context) {
        try {
            Validator validator = getValidator();
            if (validator == null) {
                return;
            }
            Set<ConstraintViolation<T>> violations = validator.validate(data);
            if (!violations.isEmpty()) {
                int rowIndex = context.readRowHolder().getRowIndex() + 1;
                String errorMsg = violations.stream()
                        .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                        .collect(Collectors.joining("; "));
                String fullErrorMsg = String.format("第%s行数据校验失败: %s<br/>", rowIndex, errorMsg);
                excelResult.getErrorList().add(fullErrorMsg);
                if (log.isDebugEnabled()) {
                    log.debug("Excel数据校验失败: {}", fullErrorMsg);
                }
            }
        } catch (Exception e) {
            log.warn("数据校验过程中发生异常，跳过校验: {}", e.getMessage());
            if (log.isDebugEnabled()) {
                log.debug("校验异常详情", e);
            }
        }
    }

    /**
     * 获取Validator实例（延迟加载）
     *
     * @return Validator实例
     */
    private Validator getValidator() {
        if (validator == null && SpringUtil.getContext() != null) {
            try {
                validator = SpringUtil.getContext().getBean(Validator.class);
            } catch (Exception e) {
                if (log.isDebugEnabled()) {
                    log.debug("无法获取Validator Bean，将跳过数据校验: {}", e.getMessage());
                }
            }
        }
        return validator;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.debug("所有数据解析完成！");
    }

    @Override
    public ExcelResult<T> getExcelResult() {
        return excelResult;
    }

}
