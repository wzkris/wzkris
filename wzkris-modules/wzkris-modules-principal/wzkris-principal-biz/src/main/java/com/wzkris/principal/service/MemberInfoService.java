package com.wzkris.principal.service;

import com.wzkris.principal.domain.MemberInfoDO;
import jakarta.annotation.Nullable;

import java.util.List;

public interface MemberInfoService {

    /**
     * 新增信息
     *
     * @param member  信息
     * @param postIds 关联职位
     */
    boolean saveMember(MemberInfoDO member, @Nullable List<Long> postIds);

    /**
     * 修改信息
     *
     * @param member  信息
     * @param postIds 关联职位
     */
    boolean modifyMember(MemberInfoDO member, @Nullable List<Long> postIds);

    /**
     * 分配职位
     *
     * @param memberId 成员ID
     * @param postIds  关联职位
     */
    boolean grantPosts(Long memberId, List<Long> postIds);

    boolean removeByIds(List<Long> memberIds);

    boolean existByUsername(Long memberId, String username);

    boolean existByPhoneNumber(Long memberId, String phoneNumber);

}
