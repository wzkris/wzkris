package com.wzkris.usercenter.impl.member;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.BeanCopierUtil;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.usercenter.api.member.MemberMngApi;
import com.wzkris.usercenter.api.member.request.*;
import com.wzkris.usercenter.api.member.response.MemberMngQueryResponse;
import com.wzkris.usercenter.api.member.response.MemberMngPageResponse;
import com.wzkris.usercenter.domain.MemberInfoDO;
import com.wzkris.usercenter.domain.PostInfoDO;
import com.wzkris.usercenter.event.CreateMemberEvent;
import com.wzkris.usercenter.mapper.MemberInfoMapper;
import com.wzkris.usercenter.request.PwdResetRequest;
import com.wzkris.usercenter.response.CheckedSelectResponse;
import com.wzkris.usercenter.service.MemberInfoService;
import com.wzkris.usercenter.service.PostInfoService;
import com.wzkris.usercenter.service.TenantInfoService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberMngApiImpl extends AbstractApi implements MemberMngApi {

    private final MemberInfoMapper memberInfoMapper;

    private final MemberInfoService memberInfoService;

    private final PostInfoService postInfoService;

    private final TenantInfoService tenantInfoService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Result<Page<MemberMngPageResponse>> queryPage(MemberMngPageRequest request) {
        IPage<MemberMngPageResponse> page = memberInfoMapper.selectVOPage(request.buildPage(), this.buildPageWrapper(request));
        return ok(Page.of(page));
    }

    private QueryWrapper<MemberInfoDO> buildPageWrapper(MemberMngPageRequest request) {
        return new QueryWrapper<MemberInfoDO>()
                .apply("s.deleted = false")
                .like(ObjectUtils.isNotEmpty(request.getUsername()), "username", request.getUsername())
                .like(ObjectUtils.isNotEmpty(request.getPhoneNumber()), "phone_number", request.getPhoneNumber())
                .eq(ObjectUtils.isNotEmpty(request.getStatus()), "s.status", request.getStatus())
                .between(request.getBeginTime() != null && request.getEndTime() != null,
                        "s.create_at",
                        request.getBeginTime(), request.getEndTime());
    }

    @Override
    public Result<MemberMngQueryResponse> queryInfo(IdRequest request) {
        Long memberId = request.getId();
        if (tenantInfoService.checkAdministrator(memberId)) {
            return accessDenied("数据权限不足");
        }
        return ok(BeanCopierUtil.copy(memberInfoService.getById(memberId), MemberMngQueryResponse.class));
    }

    @Override
    public Result<CheckedSelectResponse> queryPostSelect(MemberMngPostSelectRequest request) {
        Long memberId = request.getId();
        CheckedSelectResponse checkedSelectResponse = new CheckedSelectResponse();
        checkedSelectResponse.setCheckedKeys(memberId == null ? Collections.emptyList() : postInfoService.listByMemberId(memberId).stream().map(PostInfoDO::getId).toList());
        checkedSelectResponse.setSelects(postInfoService.listSelect(request.getPostName()));
        return ok(checkedSelectResponse);
    }

    @Override
    public Result<Void> save(MemberMngSaveRequest memberReq) {
        if (!tenantInfoService.checkAccountLimit(SecurityUtil.getTenantId())) {
            return requestFail("账号数量已达上限，请联系管理员");
        } else if (memberInfoService.existByUsername(null, memberReq.getUsername())) {
            return requestFail("添加成员'" + memberReq.getUsername() + "'失败，登录账号已存在");
        } else if (StringUtil.isNotEmpty(memberReq.getPhoneNumber())
                && memberInfoService.existByPhoneNumber(null, memberReq.getPhoneNumber())) {
            return requestFail("添加成员'" + memberReq.getUsername() + "'失败，手机号码已存在");
        }
        MemberInfoDO member = BeanCopierUtil.copy(memberReq, MemberInfoDO.class);
        member.setStatus(memberReq.getStatus());
        String password = RandomStringUtils.secure().nextAlphabetic(8);
        member.setPassword(password);
        boolean success = memberInfoService.saveMember(member, memberReq.getPostIds());
        if (success) {
            SpringUtil.getContext()
                    .publishEvent(new CreateMemberEvent(SecurityUtil.getUid(), memberReq.getUsername(), password));
        }
        return toRes(success);
    }

    @Override
    public Result<Void> update(MemberMngUpdateRequest memberReq) {
        if (tenantInfoService.checkAdministrator(memberReq.getId())) {
            return accessDenied("数据权限不足");
        }
        if (memberInfoService.existByUsername(memberReq.getId(), memberReq.getUsername())) {
            return requestFail("修改成员'" + memberReq.getUsername() + "'失败，登录账号已存在");
        } else if (StringUtil.isNotEmpty(memberReq.getPhoneNumber())
                && memberInfoService.existByPhoneNumber(memberReq.getId(), memberReq.getPhoneNumber())) {
            return requestFail("修改成员'" + memberReq.getUsername() + "'失败，手机号码已存在");
        }
        MemberInfoDO member = BeanCopierUtil.copy(memberReq, MemberInfoDO.class);
        member.setStatus(memberReq.getStatus());
        return toRes(memberInfoService.updateMember(member, memberReq.getPostIds()));
    }

    @Override
    public Result<Void> resetPwd(PwdResetRequest request) {
        if (tenantInfoService.checkAdministrator(request.getId())) {
            return accessDenied("数据权限不足");
        }
        MemberInfoDO update = new MemberInfoDO(request.getId());
        update.setPassword(passwordEncoder.encode(request.getPassword()));
        return toRes(memberInfoService.updateById(update));
    }

    @Override
    public Result<Void> grantPosts(MemberMngGrantPostRequest request) {
        if (tenantInfoService.checkAdministrator(request.getId())) {
            return accessDenied("数据权限不足");
        }
        return toRes(memberInfoService.grantPosts(request.getId(), request.getPostIds()));
    }

    @Override
    public Result<Void> remove(IdListRequest request) {
        List<Long> memberIds = request.getIdList();
        if (tenantInfoService.checkAdministrator(memberIds)) {
            return accessDenied("数据权限不足");
        }
        return toRes(memberInfoService.removeMembers(memberIds));
    }

}
