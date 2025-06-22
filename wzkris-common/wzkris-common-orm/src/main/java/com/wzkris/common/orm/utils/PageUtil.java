package com.wzkris.common.orm.utils;

import com.wzkris.common.orm.model.Page;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 分页工具 必须手动清理线程信息
 * @date : 2024/1/11 16:41
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PageUtil {

    private static final ThreadLocal<Page<?>> LOCAL_PAGE = new ThreadLocal<>();

    public static void startPage(long pageNum, long pageSize) {
        Page<?> page = new Page<>(pageNum, pageSize);
        LOCAL_PAGE.set(page);
    }

    /**
     * 获取当前分页对象
     */
    @SuppressWarnings("unchecked")
    public static <T> Page<T> getPage() {
        return (Page<T>) LOCAL_PAGE.get();
    }

    public static void clear() {
        LOCAL_PAGE.remove();
    }
}
