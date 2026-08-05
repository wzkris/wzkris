package com.wzkris.usercenter.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.usercenter.domain.MemberToPostDO;
import com.wzkris.usercenter.domain.PostInfoDO;
import com.wzkris.usercenter.domain.PostToMenuDO;
import com.wzkris.usercenter.enums.post.PostStatusEnum;
import com.wzkris.usercenter.mapper.MemberToPostMapper;
import com.wzkris.usercenter.mapper.PostInfoMapper;
import com.wzkris.usercenter.mapper.PostToMenuMapper;
import com.wzkris.usercenter.response.SelectResponse;
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
public class PostInfoServiceImpl
        extends ServiceImplPlus<PostInfoMapper, PostInfoDO>
        implements PostInfoService {

    private final MemberToPostMapper memberToPostMapper;

    private final PostToMenuMapper postToMenuMapper;

    @Override
    public List<PostInfoDO> listByMemberId(Long memberId) {
        List<Long> postIds = memberToPostMapper.selectObjsByObj(MemberToPostDO::getPostId, MemberToPostDO::getMemberId, memberId);
        if (CollectionUtils.isEmpty(postIds)) {
            return Collections.emptyList();
        }
        return this.lambdaQuery()
                .in(PostInfoDO::getId, postIds)
                .eq(PostInfoDO::getStatus, PostStatusEnum.ENABLE)
                .list();
    }

    @Override
    public List<SelectResponse> listSelect(String postName) {
        return baseMapper.selectList(Wrappers.lambdaQuery(PostInfoDO.class)
                        .select(PostInfoDO::getId, PostInfoDO::getPostName)
                        .eq(PostInfoDO::getStatus, PostStatusEnum.ENABLE)
                        .like(StringUtil.isNotBlank(postName), PostInfoDO::getPostName, postName)
                        .orderByAsc(PostInfoDO::getId))
                .stream()
                .map(postInfoDO -> {
                    SelectResponse SelectResponse = new SelectResponse();
                    SelectResponse.setId(postInfoDO.getId());
                    SelectResponse.setLabel(postInfoDO.getPostName());
                    return SelectResponse;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean savePost(PostInfoDO post, List<Long> menuIds) {
        boolean success = baseMapper.insert(post) > 0;
        if (success) {
            this.insertPostMenu(post.getId(), menuIds);
        }
        return success;
    }

    @Override
    public boolean updatePost(PostInfoDO post, List<Long> menuIds) {
        boolean success = baseMapper.updateById(post) > 0;
        if (success && menuIds != null) {
            postToMenuMapper.delete(Wrappers.lambdaQuery(PostToMenuDO.class)
                    .eq(PostToMenuDO::getPostId, post.getId()));
            this.insertPostMenu(post.getId(), menuIds);
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
    public boolean removePosts(List<Long> postIds) {
        boolean success = baseMapper.deleteByIds(postIds) > 0;
        if (success) {
            postToMenuMapper.delete(Wrappers.lambdaQuery(PostToMenuDO.class)
                    .in(PostToMenuDO::getPostId, postIds));
            memberToPostMapper.delete(Wrappers.lambdaQuery(MemberToPostDO.class)
                    .in(MemberToPostDO::getPostId, postIds));
        }
        return success;
    }

    @Override
    public boolean existMember(List<Long> postIds) {
        postIds = postIds.stream().filter(Objects::nonNull).toList();
        if (CollectionUtils.isEmpty(postIds)) {
            return false;
        }
        return memberToPostMapper.exists(Wrappers.lambdaQuery(MemberToPostDO.class)
                .in(MemberToPostDO::getPostId, postIds));
    }

}
