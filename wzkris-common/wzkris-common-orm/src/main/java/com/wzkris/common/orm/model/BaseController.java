package com.wzkris.common.orm.model;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils;
import com.wzkris.common.core.exception.service.GenericException;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.enums.BizSqlCodeEnum;
import com.wzkris.common.orm.utils.PageUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;

/**
 * web层通用数据处理
 *
 * @author wzkris
 */
public abstract class BaseController {

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
        return Result.init(biz, null, errMsg);
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        List<OrderItem> orders = new ArrayList<>();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        long pageNum = request.getParameter(PAGE_NUM) == null ? 1 : Long.parseLong(request.getParameter(PAGE_NUM));
        long pageSize = request.getParameter(PAGE_SIZE) == null ? 10 : Long.parseLong(request.getParameter(PAGE_SIZE));
        String orderBys = request.getParameter(ORDER_BY);
        if (StringUtil.isNotBlank(orderBys)) {
            if (SqlInjectionUtils.check(orderBys)) {
                throw new GenericException(BizSqlCodeEnum.INJECT_SQL.value(), BizSqlCodeEnum.INJECT_SQL.desc());
            }
            for (String orderBy : orderBys.split(",")) {
                OrderItem orderItem = Boolean.TRUE.equals(Boolean.valueOf(request.getParameter(ASC)))
                        ? OrderItem.asc(orderBy) : OrderItem.desc(orderBy);
                orders.add(orderItem);
            }
        }

        PageUtil.startPage(pageNum, pageSize, orders);
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
        return Result.requestFail(errMsg);
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
        return result ? ok() : err40000("操作失败");
    }

}
