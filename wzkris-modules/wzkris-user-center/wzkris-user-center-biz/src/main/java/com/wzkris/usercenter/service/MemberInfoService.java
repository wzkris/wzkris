package com.wzkris.usercenter.service;

import com.wzkris.common.orm.plus.IServicePlus;
import com.wzkris.usercenter.domain.MemberInfoDO;
import jakarta.annotation.Nullable;

import java.util.List;

public interface MemberInfoService extends IServicePlus<MemberInfoDO> {

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
    boolean updateMember(MemberInfoDO member, @Nullable List<Long> postIds);

    /**
     * 分配职位
     *
     * @param memberId 成员ID
     * @param postIds  关联职位
     */
    boolean grantPosts(Long memberId, List<Long> postIds);

    boolean removeMembers(List<Long> memberIds);

    boolean existByUsername(Long memberId, String username);

    boolean existByPhoneNumber(Long memberId, String phoneNumber);

}
