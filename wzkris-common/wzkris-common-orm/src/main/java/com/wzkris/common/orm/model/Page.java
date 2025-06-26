package com.wzkris.common.orm.model;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wzkris.common.orm.utils.PageUtil;
import lombok.Getter;
import lombok.Setter;

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
public class Page<T> implements AutoCloseable {

    /**
     * 列表数据
     */
    private List<T> rows = Collections.emptyList();

    /**
     * 当前记录起始索引
     */
    private long pageNum;

    /**
     * 每页显示记录数
     */
    private long pageSize;

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

    @Override
    public void close() {
        PageUtil.clear();
    }

}
