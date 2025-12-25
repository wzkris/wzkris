package com.wzkris.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.exception.service.GenericException;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.utils.TenantUtil;
import com.wzkris.usercenter.domain.PostInfoDO;
import com.wzkris.usercenter.domain.PostToMenuDO;
import com.wzkris.usercenter.domain.vo.SelectVO;
import com.wzkris.usercenter.mapper.MemberToPostMapper;
import com.wzkris.usercenter.mapper.PostInfoMapper;
import com.wzkris.usercenter.mapper.PostToMenuMapper;
import com.wzkris.usercenter.service.PostInfoService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostInfoServiceImpl implements PostInfoService {

    private final MemberToPostMapper memberToPostMapper;

    private final PostToMenuMapper postToMenuMapper;

    private final PostInfoMapper postInfoMapper;

    @Override
    public List<PostInfoDO> listByMemberId(Long memberId) {
        List<Long> postIds = memberToPostMapper.listPostIdByMemberId(memberId);
        if (CollectionUtils.isEmpty(postIds)) {
            return Collections.emptyList();
        }
        // 只能查出状态正常的职位
        LambdaQueryWrapper<PostInfoDO> lqw = new LambdaQueryWrapper<PostInfoDO>()
                .in(PostInfoDO::getPostId, postIds)
                .eq(PostInfoDO::getStatus, CommonConstants.STATUS_ENABLE);
        return postInfoMapper.selectList(lqw);
    }

    @Override
    public List<Long> listIdByMemberId(Long memberId) {
        List<Long> postIds = memberToPostMapper.listPostIdByMemberId(memberId);
        if (CollectionUtils.isEmpty(postIds)) {
            return Collections.emptyList();
        }
        // 只能查出状态正常的角色
        LambdaQueryWrapper<PostInfoDO> lqw = new LambdaQueryWrapper<PostInfoDO>()
                .select(PostInfoDO::getPostId)
                .in(PostInfoDO::getPostId, postIds)
                .eq(PostInfoDO::getStatus, CommonConstants.STATUS_ENABLE);
        return postInfoMapper.selectList(lqw).stream().map(PostInfoDO::getPostId).toList();
    }

    @Override
    public List<SelectVO> listSelect(String postName) {
        return postInfoMapper.selectList(Wrappers.lambdaQuery(PostInfoDO.class)
                        .select(PostInfoDO::getPostId, PostInfoDO::getPostName)
                        .eq(PostInfoDO::getStatus, CommonConstants.STATUS_ENABLE)
                        .like(StringUtil.isNotBlank(postName), PostInfoDO::getPostName, postName)
                        .orderByAsc(PostInfoDO::getPostId))
                .stream()
                .map(SelectVO::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPostGroup() {
        if (TenantUtil.isAdmin()) {
            return SecurityConstants.SUPER_ADMIN_NAME;
        }
        List<PostInfoDO> posts = this.listByMemberId(TenantUtil.getId());
        return posts.stream().map(PostInfoDO::getPostName).collect(Collectors.joining(","));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean savePost(PostInfoDO post, List<Long> menuIds) {
        boolean success = postInfoMapper.insert(post) > 0;
        if (success) {
            // 新增职位菜单信息
            this.insertPostMenu(post.getPostId(), menuIds);
        }
        return success;
    }

    @Override
    public boolean modifyPost(PostInfoDO post, List<Long> menuIds) {
        // 修改角色信息
        boolean success = postInfoMapper.updateById(post) > 0;
        if (success && menuIds != null) {
            // 删除角色与菜单关联
            postToMenuMapper.deleteByPostId(post.getPostId());
            // 插入角色菜单信息
            this.insertPostMenu(post.getPostId(), menuIds);
        }
        return success;
    }

    private void insertPostMenu(Long postId, List<Long> menuIds) {
        if (CollectionUtils.isNotEmpty(menuIds)) {
            List<PostToMenuDO> list = menuIds.stream()
                    .map(menuId -> new PostToMenuDO(postId, menuId))
                    .toList();
            postToMenuMapper.insert(list);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(List<Long> postIds) {
        boolean success = postInfoMapper.deleteByIds(postIds) > 0;
        if (success) {
            // 删除职位与菜单关联
            postToMenuMapper.deleteByPostIds(postIds);
            // 删除租户成员与职位关联
            memberToPostMapper.deleteByPostIds(postIds);
        }
        return success;
    }

    @Override
    public void existMember(List<Long> postIds) {
        postIds = postIds.stream().filter(Objects::nonNull).toList();
        // 是否被用户使用
        if (memberToPostMapper.existByPostIds(postIds)) {
            throw new GenericException("当前职位已被分配");
        }
    }

}
