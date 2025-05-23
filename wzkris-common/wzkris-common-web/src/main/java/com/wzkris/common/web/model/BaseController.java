package com.wzkris.common.web.model;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.utils.PageUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * web层通用数据处理
 *
 * @author wzkris
 */
public class BaseController {

    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 响应请求分页数据
     */
    protected static <T> Result<Page<T>> getDataTable(List<T> list) {
        try (Page<T> page = PageUtil.getPage()) {
            page.setRows(list);
            return Result.ok(page);
        }
    }

    /**
     * 自定义失败消息
     */
    public static <T> Result<T> resp(int biz, String errMsg) {
        return Result.resp(biz, null, errMsg);
    }

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为对应类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtil.parse(text).toLocalDateTime());
            }
        });
        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtil.parse(text).toLocalDateTime().toLocalDate());
            }
        });

    }

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        long pageNum = 1L, pageSize = 10L;
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

            pageNum = Convert.toLong(request.getParameter(PAGE_NUM), pageNum);
            pageSize = Convert.toLong(request.getParameter(PAGE_SIZE), pageSize);
        }

        PageUtil.startPage(pageNum, pageSize);
    }

    /**
     * 返回成功
     */
    public <T> Result<T> ok() {
        return Result.ok();
    }

    /**
     * 返回成功消息
     */
    public <T> Result<T> ok(T data) {
        return Result.ok(data);
    }

    /**
     * 返回失败消息
     */
    public <T> Result<T> err412(String errMsg) {
        return Result.err412(errMsg);
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected <T> Result<T> toRes(int rows) {
        return toRes(rows > 0);
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected <T> Result<T> toRes(boolean result) {
        return result ? ok() : Result.err1000();
    }

}
