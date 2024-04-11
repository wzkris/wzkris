package com.thingslink.system.service;

import com.thingslink.system.domain.Notice;

import java.util.List;

/**
 * 公告 服务层
 *
 * @author wzkris
 */
public interface NoticeService {
    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    List<Notice> list(Notice notice);

}
