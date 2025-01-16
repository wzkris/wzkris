package com.wzkris.common.orm.page;

import cn.hutool.core.convert.Convert;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 分页数据处理
 *
 * @author wzkris
 */
public class PageSupport {

    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 封装分页对象
     */
    public static <T> Page<T> initPage() {
        Page<T> page = new Page<>();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return page;
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        page.setPageNum(Convert.toLong(request.getParameter(PAGE_NUM), page.getPageNum()));
        page.setPageSize(Convert.toLong(request.getParameter(PAGE_SIZE), page.getPageSize()));
        return page;
    }

    public static <T> Page<T> initPage(int pageNum, int pageSize) {
        return new Page<>(pageNum, pageSize);
    }

    public static <T> Page<T> buildPageRequest() {
        return initPage();
    }
}
