package com.wzkris.system.service;

import com.wzkris.system.domain.SysNotice;

import java.util.List;

/**
 * 公告 服务层
 *
 * @author wzkris
 */
public interface SysNoticeService {
    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    List<SysNotice> list(SysNotice notice);

}
