package com.wzkris.common.orm.model;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.utils.PageUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;

/**
 * api层通用数据处理
 *
 * @author wzkris
 */
public abstract class AbstractApi {

    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 排序
     */
    public static final String ORDER_BY = "orderBy";

    /**
     * 排序
     */
    public static final String ASC = "asc";

    private static final long DEFAULT_PAGE_NUM = 1L;

    private static final long DEFAULT_PAGE_SIZE = 10L;

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
        return Result.init(biz, null, errMsg);
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes servletRequestAttributes)) {
            PageUtil.startPage(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE);
            return;
        }
        HttpServletRequest request = servletRequestAttributes.getRequest();
        long pageNum = parsePositiveLong(request.getParameter(PAGE_NUM), DEFAULT_PAGE_NUM);
        long pageSize = parsePositiveLong(request.getParameter(PAGE_SIZE), DEFAULT_PAGE_SIZE);
        String orderBys = request.getParameter(ORDER_BY);
        List<OrderItem> orders = new ArrayList<>();
        if (StringUtil.isNotBlank(orderBys)) {
            if (SqlInjectionUtils.check(orderBys)) {
                throw new IllegalArgumentException("存在sql注入参数");
            }
            boolean asc = Boolean.parseBoolean(request.getParameter(ASC));
            for (String orderBy : orderBys.split(",")) {
                OrderItem orderItem = asc ? OrderItem.asc(orderBy) : OrderItem.desc(orderBy);
                orders.add(orderItem);
            }
        }

        PageUtil.startPage(pageNum, pageSize, orders);
    }

    private long parsePositiveLong(String value, long defaultValue) {
        if (StringUtil.isBlank(value)) {
            return defaultValue;
        }
        try {
            long parsedValue = Long.parseLong(value);
            return parsedValue > 0 ? parsedValue : defaultValue;
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
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
    public <T> Result<T> requestFail(String errMsg) {
        return Result.requestFail(errMsg);
    }

    public <T> Result<T> accessDenied(String errMsg) {
        return Result.accessDenied(errMsg);
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
        return result ? ok() : requestFail("操作失败");
    }

}
