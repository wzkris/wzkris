package com.wzkris.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.exception.service.BusinessException;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.utils.SystemUserUtil;
import com.wzkris.user.domain.SysPost;
import com.wzkris.user.domain.vo.SelectVO;
import com.wzkris.user.mapper.SysPostMapper;
import com.wzkris.user.mapper.SysUserPostMapper;
import com.wzkris.user.service.SysPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
    public List<SelectVO> listSelect(String postName) {
        return postMapper
                .selectList(Wrappers.lambdaQuery(SysPost.class)
                        .select(SysPost::getPostId, SysPost::getPostName)
                        .eq(SysPost::getStatus, CommonConstants.STATUS_ENABLE)
                        .like(StringUtil.isNotBlank(postName), SysPost::getPostName, postName)
                        .orderByAsc(SysPost::getPostId))
                .stream()
                .map(SelectVO::new)
                .collect(Collectors.toList());
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
        if (SystemUserUtil.isAdmin()) {
            return SecurityConstants.SUPER_ADMIN_NAME;
        }
        List<SysPost> sysPosts = this.listByUserId(SystemUserUtil.getUserId());
        return sysPosts.stream().map(SysPost::getPostName).collect(Collectors.joining(","));
    }

    @Override
    public boolean deleteByPostIds(List<Long> postIds) {
        boolean success = postMapper.deleteByIds(postIds) > 0;
        if (success) {
            userPostMapper.deleteByPostIds(postIds);
        }
        return success;
    }

    @Override
    public void checkExistUser(List<Long> postIds) {
        postIds = postIds.stream().filter(Objects::nonNull).toList();
        if (userPostMapper.existByPostIds(postIds)) {
            throw new BusinessException("business.allocated");
        }
    }

}
