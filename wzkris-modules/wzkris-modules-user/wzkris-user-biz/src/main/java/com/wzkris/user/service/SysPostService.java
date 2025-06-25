package com.wzkris.user.service;

import com.wzkris.user.domain.SysPost;
import com.wzkris.user.domain.vo.SelectVO;
import jakarta.annotation.Nullable;

import java.util.List;

/**
 * 岗位信息 服务层
 *
 * @author wzkris
 */
public interface SysPostService {

    /**
     * 查询可以选择的岗位
     */
    List<SelectVO> listSelect(@Nullable String postName);

    /**
     * 根据用户id查询关联岗位(正常状态)
     *
     * @param userId 用户id
     * @return 岗位列表
     */
    List<SysPost> listByUserId(Long userId);

    /**
     * 根据用户id查询岗位ID(正常状态)
     *
     * @param userId 用户id
     * @return 岗位列表
     */
    List<Long> listIdByUserId(@Nullable Long userId);

    /**
     * 获取当前岗位组
     */
    String getPostGroup();

    /**
     * 批量删除岗位信息
     *
     * @param postIds 需要删除的岗位ID
     */
    boolean deleteByPostIds(List<Long> postIds);

    /**
     * 校验岗位是否被用户关联
     */
    void checkPostUsed(List<Long> postIds);

}
