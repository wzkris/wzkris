package com.wzkris.common.orm.utils;

import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.PagingRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 当前线程的分页上下文，供分页插件读取。
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PageUtil {

    private static final ThreadLocal<Page<?>> LOCAL_PAGE = new ThreadLocal<>();

    public static void bind(PagingRequest request) {
        Page<?> page = new Page<>(request.normalizedPageNum(), request.normalizedPageSize());
        page.setOrders(request.buildOrderItems());
        LOCAL_PAGE.set(page);
    }

    @SuppressWarnings("unchecked")
    public static <T> Page<T> getPage() {
        return (Page<T>) LOCAL_PAGE.get();
    }

    public static void clear() {
        LOCAL_PAGE.remove();
    }

}
