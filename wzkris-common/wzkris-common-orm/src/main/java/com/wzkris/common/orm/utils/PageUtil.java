package com.wzkris.common.orm.utils;

import com.wzkris.common.orm.page.Page;
import com.wzkris.common.orm.page.PageSupport;
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

    public static void startPage() {
        LOCAL_PAGE.set(PageSupport.initPage());
    }

    public static void startPage(int pageNum, int pageSize) {
        LOCAL_PAGE.set(PageSupport.initPage(pageNum, pageSize));
    }

    public static <T> Page<T> getPage() {
        return getPage(true);
    }

    /**
     * 获取当前线程的分页数据并清理
     */
    @SuppressWarnings("unchecked")
    public static <T> Page<T> getPage(boolean clear) {
        if (clear) {
            try {
                return (Page<T>) LOCAL_PAGE.get();
            } finally {
                clear();
            }
        } else {
            return (Page<T>) LOCAL_PAGE.get();
        }
    }

    public static void clear() {
        LOCAL_PAGE.remove();
    }
}
