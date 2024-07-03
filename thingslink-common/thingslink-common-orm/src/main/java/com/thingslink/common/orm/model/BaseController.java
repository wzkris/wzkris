package com.thingslink.common.orm.model;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.orm.page.Page;
import com.thingslink.common.orm.utils.PageUtil;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.util.List;

/**
 * web层通用数据处理
 *
 * @author wzkris
 */
public class BaseController {

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings("unchecked")
    protected static <T> Result<Page<T>> getDataTable(List<T> list) {
        Page<T> page = PageUtil.getPage();
        page.setRows(list);
        return Result.success(page);
    }

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(LocalDateTimeUtil.parse(text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        PageUtil.startPage();
    }

    /**
     * 清理分页的线程变量
     */
    protected void clearPage() {
        PageUtil.clear();
    }

    /**
     * 返回成功
     */
    public <T> Result<T> success() {
        return Result.success();
    }

    /**
     * 返回成功消息
     */
    public <T> Result<T> success(T data) {
        return Result.success(data);
    }

    /**
     * 返回失败消息
     */
    public <T> Result<T> fail() {
        return Result.fail();
    }

    public <T> Result<T> fail(String errMsg) {
        return Result.fail(errMsg);
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected <T> Result<T> toRes(int rows) {
        return rows > 0 ? success() : fail();
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected <T> Result<T> toRes(boolean result) {
        return result ? success() : fail();
    }

}
