package com.wzkris.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.security.utils.LoginUserUtil;
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

    private final SysPostMapper postMapper;

    private final SysUserPostMapper userPostMapper;

    @Override
    public List<SysPost> listCanGranted() {
        return postMapper.selectList(Wrappers.lambdaQuery(SysPost.class)
                .eq(SysPost::getStatus, CommonConstants.STATUS_ENABLE));
    }

    @Override
    public List<SysPost> listByUserId(Long userId) {
        List<Long> postIds = userPostMapper.listPostIdByUserId(userId);
        if (CollectionUtils.isEmpty(postIds)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SysPost> lqw = new LambdaQueryWrapper<SysPost>()
                .in(SysPost::getPostId, postIds)
                .eq(SysPost::getStatus, CommonConstants.STATUS_ENABLE);
        return postMapper.selectList(lqw);
    }

    @Override
    public List<Long> listIdByUserId(Long userId) {
        List<Long> postIds = userPostMapper.listPostIdByUserId(userId);
        if (CollectionUtils.isEmpty(postIds)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SysPost> lqw = new LambdaQueryWrapper<SysPost>()
                .select(SysPost::getPostId)
                .in(SysPost::getPostId, postIds)
                .eq(SysPost::getStatus, CommonConstants.STATUS_ENABLE);
        return postMapper.selectList(lqw).stream().map(SysPost::getPostId).toList();
    }

    @Override
    public String getPostGroup() {
        if (LoginUserUtil.isAdmin()) {
            return "超级管理员";
        }
        List<SysPost> sysPosts = this.listByUserId(LoginUserUtil.getUserId());
        return sysPosts.stream().map(SysPost::getPostName).collect(Collectors.joining(","));
    }

    @Override
    public void deleteByPostIds(List<Long> postIds) {
        postMapper.deleteByIds(postIds);
        userPostMapper.deleteByPostIds(postIds);
    }

    @Override
    public boolean checkPostUse(List<Long> postIds) {
        return userPostMapper.countByPostIds(postIds) > 0;
    }

}
