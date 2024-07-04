package com.wzkris.common.orm.page;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 分页数据
 *
 * @author wzkris
 */
@Setter
@Getter
@Accessors(chain = true)
@NoArgsConstructor
public class Page<T> {
    /**
     * 列表数据
     */
    private List<T> rows = Collections.emptyList();

    /**
     * 当前记录起始索引
     */
    private long pageNum = 1;

    /**
     * 每页显示记录数
     */
    private long pageSize = 10;

    /**
     * 总数
     */
    private long total;

    /**
     * 总页数
     */
    private long pages;

    @JsonIgnore
    private List<OrderItem> orders = new ArrayList<>();

    public Page(long pageNum, long pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    /**
     * 计算当前分页偏移量
     */
    public long offset() {
        if (this.pageNum <= 1) {
            return 0;
        }
        return Math.max((this.pageNum - 1) * this.pageSize, 0);
    }
}
