package com.wzkris.user.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.exception.BusinessExceptionI18n;
import com.wzkris.user.domain.SysPost;
import com.wzkris.user.mapper.SysPostMapper;
import com.wzkris.user.mapper.SysUserPostMapper;
import com.wzkris.user.service.SysPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 岗位信息 服务层处理
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class SysPostServiceImpl implements SysPostService {
    private final SysPostMapper sysPostMapper;
    private final SysUserPostMapper sysUserPostMapper;

    /**
     * 根据条件查询岗位集合
     *
     * @param sysPost 筛选条件
     * @return 岗位列表
     */
    @Override
    public List<SysPost> list(SysPost sysPost) {
        LambdaQueryWrapper<SysPost> lqw = this.buildQueryWrapper(sysPost);
        return sysPostMapper.selectList(lqw);
    }

    private LambdaQueryWrapper<SysPost> buildQueryWrapper(SysPost sysPost) {
        return new LambdaQueryWrapper<SysPost>()
                .eq(ObjUtil.isNotNull(sysPost.getTenantId()), SysPost::getTenantId, sysPost.getTenantId())
                .like(ObjUtil.isNotEmpty(sysPost.getPostName()), SysPost::getPostName, sysPost.getPostName())
                .like(ObjUtil.isNotEmpty(sysPost.getPostCode()), SysPost::getPostCode, sysPost.getPostCode())
                .eq(ObjUtil.isNotEmpty(sysPost.getStatus()), SysPost::getStatus, sysPost.getStatus())
                .orderByDesc(SysPost::getPostSort);
    }

    /**
     * 根据用户id查询岗位集合
     *
     * @param userId 用户id
     * @return 岗位列表
     */
    @Override
    public List<SysPost> listByUserId(Long userId) {
        List<Long> postIds = sysUserPostMapper.listPostIdByUserId(userId);
        if (CollectionUtils.isEmpty(postIds)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SysPost> lqw = new LambdaQueryWrapper<SysPost>()
                .in(SysPost::getPostId, postIds);
        return sysPostMapper.selectList(lqw);
    }

    /**
     * 根据用户id获取岗位
     */
    @Override
    public String getPostGroup(Long userId) {
        // 岗位组
        List<SysPost> sysPosts = this.listByUserId(userId);
        return sysPosts.stream().map(SysPost::getPostName).collect(Collectors.joining(","));
    }

    /**
     * 批量删除岗位信息
     *
     * @param postIds 需要删除的岗位ID
     * @return 结果
     */
    @Override
    public int deleteByPostIds(List<Long> postIds) {
        if (sysUserPostMapper.countByPostIds(postIds) > 0) {
            throw new BusinessExceptionI18n("business.allocated");
        }
        return sysPostMapper.deleteByIds(postIds);
    }

}
