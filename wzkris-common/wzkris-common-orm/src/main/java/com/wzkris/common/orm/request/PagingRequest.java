package com.wzkris.common.orm.request;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils;
import com.wzkris.common.core.model.QueryRequest;
import com.wzkris.common.core.utils.StringUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class PagingRequest extends QueryRequest {

    public static final long DEFAULT_PAGE_NUM = 1L;

    public static final long DEFAULT_PAGE_SIZE = 10L;

    private Long pageNum;

    private Long pageSize;

    private String orderBy;

    private Boolean asc;

    public long normalizedPageNum() {
        return pageNum != null && pageNum > 0 ? pageNum : DEFAULT_PAGE_NUM;
    }

    public long normalizedPageSize() {
        return pageSize != null && pageSize > 0 ? pageSize : DEFAULT_PAGE_SIZE;
    }

    public List<OrderItem> buildOrderItems() {
        List<OrderItem> orders = new ArrayList<>();
        if (StringUtil.isBlank(orderBy)) {
            return orders;
        }
        if (SqlInjectionUtils.check(orderBy)) {
            throw new IllegalArgumentException("存在sql注入参数");
        }
        boolean ascending = Boolean.TRUE.equals(asc);
        for (String column : orderBy.split(",")) {
            String c = column.trim();
            if (StringUtil.isBlank(c)) {
                continue;
            }
            orders.add(ascending ? OrderItem.asc(c) : OrderItem.desc(c));
        }
        return orders;
    }

}
