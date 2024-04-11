package com.thingslink.auth.service;

import com.thingslink.auth.domain.SysPost;

import java.util.List;

/**
 * 岗位信息 服务层
 *
 * @author wzkris
 */
public interface SysPostService {

    /**
     * 根据条件查询岗位集合
     *
     * @param sysPost 筛选条件
     * @return 岗位列表
     */
    List<SysPost> list(SysPost sysPost);

    /**
     * 根据用户id查询岗位集合
     *
     * @param userId 用户id
     * @return 岗位列表
     */
    List<SysPost> listByUserId(Long userId);

    /**
     * 根据用户id获取岗位
     */
    String getPostGroup(Long userId);

    /**
     * 批量删除岗位信息
     *
     * @param postIds 需要删除的岗位ID
     * @return 结果
     */
    int deleteByPostIds(Long... postIds);

}
