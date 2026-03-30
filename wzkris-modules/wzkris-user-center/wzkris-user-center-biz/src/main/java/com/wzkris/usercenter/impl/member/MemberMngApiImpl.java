package com.wzkris.usercenter.impl.member;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.model.TenantLoginUser;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.api.member.MemberMngApi;
import com.wzkris.usercenter.domain.MemberInfoDO;
import com.wzkris.usercenter.event.CreateMemberEvent;
import com.wzkris.usercenter.mapper.MemberInfoMapper;
import com.wzkris.usercenter.request.PwdResetRequest;
import com.wzkris.usercenter.request.StatusUpdateRequest;
import com.wzkris.usercenter.request.member.MemberMngGrantPostRequest;
import com.wzkris.usercenter.request.member.MemberMngQueryRequest;
import com.wzkris.usercenter.request.member.MemberMngSaveRequest;
import com.wzkris.usercenter.request.member.MemberMngUpdateRequest;
import com.wzkris.usercenter.response.CheckedSelectResponse;
import com.wzkris.usercenter.response.member.MemberMngResponse;
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
    public Result<Page<MemberMngResponse>> queryPage(MemberMngQueryRequest request) {
        startPage();
        List<MemberMngResponse> list = memberInfoMapper.listVO(this.buildPageWrapper(request));
        return getDataTable(list);
    }

    private QueryWrapper<MemberInfoDO> buildPageWrapper(MemberMngQueryRequest request) {
        return new QueryWrapper<MemberInfoDO>()
                .like(ObjectUtils.isNotEmpty(request.getUsername()), "username", request.getUsername())
                .like(ObjectUtils.isNotEmpty(request.getPhoneNumber()), "phone_number", request.getPhoneNumber())
                .eq(ObjectUtils.isNotEmpty(request.getStatus()), "s.status", request.getStatus())
                .between(request.getBeginTime() != null && request.getEndTime() != null,
                        "s.create_at",
                        request.getBeginTime(),
                        request.getEndTime());
    }

    @Override
    public Result<MemberMngResponse> queryInfo(Long memberId) {
        if (tenantInfoService.checkAdministrator(memberId)) {
            return accessDenied("数据权限不足");
        }
        return ok(BeanUtil.convert(memberInfoService.getById(memberId), MemberMngResponse.class));
    }

    @Override
    public Result<CheckedSelectResponse> queryPostSelect(Long memberId, String postName) {
        CheckedSelectResponse checkedSelectResponse = new CheckedSelectResponse();
        checkedSelectResponse.setCheckedKeys(memberId == null ? Collections.emptyList() : postInfoService.listIdByMemberId(memberId));
        checkedSelectResponse.setSelects(postInfoService.listSelect(postName));
        return ok(checkedSelectResponse);
    }

    @Override
    public Result<Void> save(MemberMngSaveRequest memberReq) {
        TenantLoginUser loginUser = SecurityUtil.getLoginUser(TenantLoginUser.class);
        if (!tenantInfoService.checkAccountLimit(loginUser.getTenantId())) {
            return requestFail("账号数量已达上限，请联系管理员");
        } else if (memberInfoService.existByUsername(null, memberReq.getUsername())) {
            return requestFail("添加成员'" + memberReq.getUsername() + "'失败，登录账号已存在");
        } else if (StringUtil.isNotEmpty(memberReq.getPhoneNumber())
                && memberInfoService.existByPhoneNumber(null, memberReq.getPhoneNumber())) {
            return requestFail("添加成员'" + memberReq.getUsername() + "'失败，手机号码已存在");
        }
        MemberInfoDO member = BeanUtil.convert(memberReq, MemberInfoDO.class);
        String password = RandomStringUtils.secure().nextAlphabetic(8);
        member.setPassword(password);
        boolean success = memberInfoService.saveMember(member, memberReq.getPostIds());
        if (success) {
            SpringUtil.getContext()
                    .publishEvent(new CreateMemberEvent(loginUser.getUid(), memberReq.getUsername(), password));
        }
        return toRes(success);
    }

    @Override
    public Result<Void> update(MemberMngUpdateRequest memberReq) {
        if (tenantInfoService.checkAdministrator(memberReq.getMemberId())) {
            return accessDenied("数据权限不足");
        }
        if (memberInfoService.existByUsername(memberReq.getMemberId(), memberReq.getUsername())) {
            return requestFail("修改成员'" + memberReq.getUsername() + "'失败，登录账号已存在");
        } else if (StringUtil.isNotEmpty(memberReq.getPhoneNumber())
                && memberInfoService.existByPhoneNumber(memberReq.getMemberId(), memberReq.getPhoneNumber())) {
            return requestFail("修改成员'" + memberReq.getUsername() + "'失败，手机号码已存在");
        }
        MemberInfoDO member = BeanUtil.convert(memberReq, MemberInfoDO.class);
        return toRes(memberInfoService.updateMember(member, memberReq.getPostIds()));
    }

    @Override
    public Result<Void> resetPwd(PwdResetRequest request) {
        if (tenantInfoService.checkAdministrator(request.getId())) {
            return accessDenied("数据权限不足");
        }
        MemberInfoDO update = new MemberInfoDO(request.getId());
        update.setPassword(passwordEncoder.encode(request.getPassword()));
        return toRes(memberInfoMapper.updateById(update));
    }

    @Override
    public Result<Void> updateStatus(StatusUpdateRequest request) {
        if (tenantInfoService.checkAdministrator(request.getId())) {
            return accessDenied("数据权限不足");
        }
        MemberInfoDO update = new MemberInfoDO(request.getId());
        update.setStatus(request.getStatus());
        return toRes(memberInfoMapper.updateById(update));
    }

    @Override
    public Result<Void> grantPosts(MemberMngGrantPostRequest request) {
        if (tenantInfoService.checkAdministrator(request.getMemberId())) {
            return accessDenied("数据权限不足");
        }
        return toRes(memberInfoService.grantPosts(request.getMemberId(), request.getPostIds()));
    }

    @Override
    public Result<Void> remove(List<Long> memberIds) {
        if (tenantInfoService.checkAdministrator(memberIds)) {
            return accessDenied("数据权限不足");
        }
        return toRes(memberInfoService.removeMembers(memberIds));
    }

}
