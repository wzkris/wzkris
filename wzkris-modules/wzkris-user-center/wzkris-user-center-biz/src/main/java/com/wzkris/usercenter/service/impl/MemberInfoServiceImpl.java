package com.wzkris.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.common.orm.utils.SkipTenantInterceptorUtil;
import com.wzkris.common.security.component.PasswordEncoderDelegate;
import com.wzkris.usercenter.domain.MemberInfoDO;
import com.wzkris.usercenter.domain.MemberToPostDO;
import com.wzkris.usercenter.mapper.MemberInfoMapper;
import com.wzkris.usercenter.mapper.MemberToPostMapper;
import com.wzkris.usercenter.service.MemberInfoService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemberInfoServiceImpl
        extends ServiceImplPlus<MemberInfoMapper, MemberInfoDO>
        implements MemberInfoService {

    private final MemberToPostMapper memberToPostMapper;

    private final PasswordEncoderDelegate passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveMember(MemberInfoDO member, @Nullable List<Long> postIds) {
        if (member.getPassword() != null && !passwordEncoder.isEncode(member.getPassword())) {
            member.setPassword(passwordEncoder.encode(member.getPassword()));
        }
        boolean success = baseMapper.insert(member) > 0;
        if (success) {
            this.insertMemberPost(member.getId(), postIds);
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateMember(MemberInfoDO member, List<Long> postIds) {
        if (member.getPassword() != null && !passwordEncoder.isEncode(member.getPassword())) {
            member.setPassword(passwordEncoder.encode(member.getPassword()));
        }
        boolean success = baseMapper.updateById(member) > 0;
        if (success && postIds != null) {
            memberToPostMapper.delete(new LambdaQueryWrapper<>(MemberToPostDO.class)
                    .eq(MemberToPostDO::getMemberId, member.getId()));
            this.insertMemberPost(member.getId(), postIds);
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean grantPosts(Long memberId, List<Long> postIds) {
        memberToPostMapper.delete(new LambdaQueryWrapper<>(MemberToPostDO.class)
                .eq(MemberToPostDO::getMemberId, memberId));
        return this.insertMemberPost(memberId, postIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeMembers(List<Long> memberIds) {
        boolean success = baseMapper.deleteByIds(memberIds) > 0;
        if (success) {
            memberToPostMapper.delete(new LambdaQueryWrapper<>(MemberToPostDO.class)
                    .in(MemberToPostDO::getMemberId, memberIds));
        }
        return success;
    }

    @Override
    public boolean existByUsername(Long memberId, String username) {
        return SkipTenantInterceptorUtil.ignore(() -> {
            LambdaQueryWrapper<MemberInfoDO> lqw = new LambdaQueryWrapper<>(MemberInfoDO.class)
                    .eq(MemberInfoDO::getUsername, username)
                    .ne(Objects.nonNull(memberId), MemberInfoDO::getId, memberId);
            return baseMapper.exists(lqw);
        });
    }

    @Override
    public boolean existByPhoneNumber(Long memberId, String phoneNumber) {
        return SkipTenantInterceptorUtil.ignore(() -> {
            LambdaQueryWrapper<MemberInfoDO> lqw = new LambdaQueryWrapper<>(MemberInfoDO.class)
                    .eq(MemberInfoDO::getPhoneNumber, phoneNumber)
                    .ne(Objects.nonNull(memberId), MemberInfoDO::getId, memberId);
            return baseMapper.exists(lqw);
        });
    }

    private boolean insertMemberPost(Long memberId, List<Long> postIds) {
        if (CollectionUtils.isNotEmpty(postIds)) {
            List<MemberToPostDO> list = postIds.stream()
                    .map(postId -> new MemberToPostDO(memberId, postId))
                    .toList();
            return !memberToPostMapper.insert(list).isEmpty();
        }
        return false;
    }

}
