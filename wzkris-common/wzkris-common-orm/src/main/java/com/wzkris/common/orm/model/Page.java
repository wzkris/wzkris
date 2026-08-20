package com.wzkris.common.orm.model;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页返回数据（纯 VO）
 * <p>
 * 对外序列化字段：rows / pageNum / pageSize / total / pages
 * <p>
 *
 * @author wzkris
 */
@Getter
@Setter
@ToString
public class Page<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 列表数据
     */
    private List<T> rows = Collections.emptyList();

    /**
     * 当前页码
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

    public Page(long pageNum, long pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    /**
     * 将 MP 分页结果转换为自定义 Page（同类型）
     */
    public static <T> Page<T> of(IPage<T> source) {
        Page<T> page = new Page<>(source.getCurrent(), source.getSize());
        page.setRows(source.getRecords());
        page.setTotal(source.getTotal());
        page.setPages(source.getPages());
        return page;
    }

    /**
     * 将 MP 分页结果转换为自定义 Page（带类型转换）
     */
    public static <S, T> Page<T> of(IPage<S> source, List<T> rows) {
        Page<T> page = new Page<>(source.getCurrent(), source.getSize());
        page.setRows(rows);
        page.setTotal(source.getTotal());
        page.setPages(source.getPages());
        return page;
    }

}
