package com.wzkris.common.orm.model;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.orm.utils.PageUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
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

    // 支持的日期格式
    public static final String[] DATE_PATTERNS = {
            "yyyy-MM-dd",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy/MM/dd",
            "yyyy/MM/dd HH:mm:ss"
    };

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
        // LocalDateTime 类型转换
        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                try {
                    // 使用 DateUtils 解析日期字符串
                    Date date = DateUtils.parseDate(text, DATE_PATTERNS);
                    // 转换为 LocalDateTime
                    setValue(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                } catch (ParseException e) {
                    throw new IllegalArgumentException("无效的日期格式: " + text, e);
                }
            }
        });

        // LocalDate 类型转换
        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                try {
                    // 使用 DateUtils 解析日期字符串
                    Date date = DateUtils.parseDate(text, DATE_PATTERNS);
                    // 转换为 LocalDate
                    setValue(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                } catch (ParseException e) {
                    throw new IllegalArgumentException("无效的日期格式: " + text, e);
                }
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

            pageNum = request.getParameter(PAGE_NUM) == null ? pageNum : Long.parseLong(request.getParameter(PAGE_NUM));
            pageSize = request.getParameter(PAGE_SIZE) == null ? pageSize : Long.parseLong(request.getParameter(PAGE_SIZE));
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
    public <T> Result<T> err40000(String errMsg) {
        return Result.err40000(errMsg);
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
        return result ? ok() : Result.err10000();
    }

}
