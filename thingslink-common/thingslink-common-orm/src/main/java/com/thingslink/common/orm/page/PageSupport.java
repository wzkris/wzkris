package com.thingslink.common.orm.page;

import cn.hutool.core.convert.Convert;
import com.thingslink.common.core.utils.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;

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
    public static Page initPage() {
        Page page = new Page();
        HttpServletRequest request = ServletUtil.getRequest();
        if (request == null) {
            return page;
        }
        page.setPageNum(Convert.toLong(request.getParameter(PAGE_NUM), page.getPageNum()));
        page.setPageSize(Convert.toLong(request.getParameter(PAGE_SIZE), page.getPageSize()));
        return page;
    }

    public static Page initPage(int pageNum, int pageSize) {
        return new Page(pageNum, pageSize);
    }


    public static Page buildPageRequest() {
        return initPage();
    }
}
