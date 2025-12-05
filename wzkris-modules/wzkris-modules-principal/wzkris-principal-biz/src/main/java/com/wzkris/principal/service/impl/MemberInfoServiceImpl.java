package com.wzkris.principal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.orm.utils.SkipTenantInterceptorUtil;
import com.wzkris.common.security.component.PasswordEncoderDelegate;
import com.wzkris.principal.domain.MemberInfoDO;
import com.wzkris.principal.domain.MemberToPostDO;
import com.wzkris.principal.mapper.MemberInfoMapper;
import com.wzkris.principal.mapper.MemberToPostMapper;
import com.wzkris.principal.service.MemberInfoService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemberInfoServiceImpl implements MemberInfoService {

    private final MemberInfoMapper memberInfoMapper;

    private final MemberToPostMapper memberToPostMapper;

    private final PasswordEncoderDelegate passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveMember(MemberInfoDO member, @Nullable List<Long> postIds) {
        if (member.getPassword() != null && !passwordEncoder.isEncode(member.getPassword())) {
            member.setPassword(passwordEncoder.encode(member.getPassword()));
        }
        boolean success = memberInfoMapper.insert(member) > 0;
        if (success) {
            this.insertMemberPost(member.getMemberId(), postIds);
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean modifyMember(MemberInfoDO member, List<Long> postIds) {
        if (member.getPassword() != null && !passwordEncoder.isEncode(member.getPassword())) {
            member.setPassword(passwordEncoder.encode(member.getPassword()));
        }
        boolean success = memberInfoMapper.updateById(member) > 0;
        if (success && postIds != null) {
            memberToPostMapper.deleteByMemberId(member.getMemberId());
            this.insertMemberPost(member.getMemberId(), postIds);
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean grantPosts(Long memberId, List<Long> postIds) {
        memberToPostMapper.deleteByMemberId(memberId);
        return this.insertMemberPost(memberId, postIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(List<Long> memberIds) {
        boolean success = memberInfoMapper.deleteByIds(memberIds) > 0;
        if (success) {
            memberToPostMapper.deleteByMemberIds(memberIds);
        }
        return success;
    }

    @Override
    public boolean existByUsername(Long memberId, String username) {
        return SkipTenantInterceptorUtil.ignore(() -> {
            LambdaQueryWrapper<MemberInfoDO> lqw = new LambdaQueryWrapper<>(MemberInfoDO.class)
                    .eq(MemberInfoDO::getUsername, username)
                    .ne(Objects.nonNull(memberId), MemberInfoDO::getMemberId, memberId);
            return memberInfoMapper.exists(lqw);
        });
    }

    @Override
    public boolean existByPhoneNumber(Long memberId, String phoneNumber) {
        return SkipTenantInterceptorUtil.ignore(() -> {
            LambdaQueryWrapper<MemberInfoDO> lqw = new LambdaQueryWrapper<>(MemberInfoDO.class)
                    .eq(MemberInfoDO::getPhoneNumber, phoneNumber)
                    .ne(Objects.nonNull(memberId), MemberInfoDO::getMemberId, memberId);
            return memberInfoMapper.exists(lqw);
        });
    }

    private boolean insertMemberPost(Long memberId, List<Long> postIds) {
        if (CollectionUtils.isNotEmpty(postIds)) {
            List<MemberToPostDO> list = postIds.stream()
                    .map(postId -> new MemberToPostDO(memberId, postId))
                    .toList();
            return memberToPostMapper.insert(list) > 0;
        }
        return false;
    }

}
