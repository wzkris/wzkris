package com.wzkris.usercenter.controller.member;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckTenantPerms;
import com.wzkris.common.security.enums.CheckMode;
import com.wzkris.common.security.model.TenantLoginUser;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.domain.MemberInfoDO;
import com.wzkris.usercenter.domain.req.PwdResetReq;
import com.wzkris.usercenter.domain.req.StatusEditReq;
import com.wzkris.usercenter.domain.req.member.MemberMngAddReq;
import com.wzkris.usercenter.domain.req.member.MemberMngEditReq;
import com.wzkris.usercenter.domain.req.member.MemberMngGrantPostReq;
import com.wzkris.usercenter.domain.req.member.MemberMngQueryReq;
import com.wzkris.usercenter.domain.resp.CheckedSelectResp;
import com.wzkris.usercenter.domain.resp.member.MemberMngResp;
import com.wzkris.usercenter.event.CreateMemberEvent;
import com.wzkris.usercenter.mapper.MemberInfoMapper;
import com.wzkris.usercenter.service.MemberInfoService;
import com.wzkris.usercenter.service.PostInfoService;
import com.wzkris.usercenter.service.TenantInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Tag(name = "租户成员管理")
@Validated
@RestController
@RequestMapping("/member-manage")
@RequiredArgsConstructor
public class MemberMngController extends BaseController {

    private final MemberInfoMapper memberInfoMapper;

    private final MemberInfoService memberInfoService;

    private final PostInfoService postInfoService;

    private final TenantInfoService tenantInfoService;

    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "分页列表")
    @GetMapping("/page")
    @CheckTenantPerms("user-mod:member-mng:page")
    public Result<Page<MemberMngResp>> page(MemberMngQueryReq queryReq) {
        startPage();
        List<MemberMngResp> list = memberInfoMapper.listVO(this.buildPageWrapper(queryReq));
        return getDataTable(list);
    }

    private QueryWrapper<MemberInfoDO> buildPageWrapper(MemberMngQueryReq queryReq) {
        return new QueryWrapper<MemberInfoDO>()
                .like(ObjectUtils.isNotEmpty(queryReq.getUsername()), "username", queryReq.getUsername())
                .like(ObjectUtils.isNotEmpty(queryReq.getPhoneNumber()), "phone_number", queryReq.getPhoneNumber())
                .eq(ObjectUtils.isNotEmpty(queryReq.getStatus()), "s.status", queryReq.getStatus())
                .between(queryReq.getParam("beginTime") != null && queryReq.getParam("endTime") != null,
                        "s.create_at",
                        queryReq.getParam("beginTime"),
                        queryReq.getParam("endTime"));
    }

    @Operation(summary = "成员详细信息")
    @GetMapping("/{memberId}")
    @CheckTenantPerms("user-mod:member-mng:page")
    public Result<MemberInfoDO> getInfo(@PathVariable Long memberId) {
        tenantInfoService.checkAdministrator(memberId);
        return ok(memberInfoMapper.selectById(memberId));
    }

    @Operation(summary = "成员-职位选择列表")
    @GetMapping({"/post-checked-select/", "/post-checked-select/{memberId}"})
    @CheckTenantPerms(
            value = {"user-mod:member-mng:edit", "user-mod:member-mng:add"},
            mode = CheckMode.OR)
    public Result<CheckedSelectResp> postSelect(@PathVariable(required = false) Long memberId, String postName) {
        CheckedSelectResp checkedSelectResp = new CheckedSelectResp();
        checkedSelectResp.setCheckedKeys(memberId == null ? Collections.emptyList() : postInfoService.listIdByMemberId(memberId));
        checkedSelectResp.setSelects(postInfoService.listSelect(postName));
        return ok(checkedSelectResp);
    }

    @Operation(summary = "新增成员")
    @OperateLog(title = "成员管理", subTitle = "新增成员", type = OperateTypeEnum.INSERT)
    @PostMapping("/add")
    @CheckTenantPerms("user-mod:member-mng:add")
    public Result<Void> add(@Validated @RequestBody MemberMngAddReq memberReq) {
        Long tenantId = SecurityUtil.getLoginUser(TenantLoginUser.class).getTenantId();
        if (!tenantInfoService.checkAccountLimit(tenantId)) {
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
                    .publishEvent(new CreateMemberEvent(SecurityUtil.getUid(), memberReq.getUsername(), password));
        }
        return toRes(success);
    }

    @Operation(summary = "修改成员")
    @OperateLog(title = "成员管理", subTitle = "修改成员", type = OperateTypeEnum.UPDATE)
    @PostMapping("/edit")
    @CheckTenantPerms("user-mod:member-mng:edit")
    public Result<Void> edit(@Validated @RequestBody MemberMngEditReq memberReq) {
        tenantInfoService.checkAdministrator(memberReq.getMemberId());
        if (memberInfoService.existByUsername(memberReq.getMemberId(), memberReq.getUsername())) {
            return requestFail("修改成员'" + memberReq.getUsername() + "'失败，登录账号已存在");
        } else if (StringUtil.isNotEmpty(memberReq.getPhoneNumber())
                && memberInfoService.existByPhoneNumber(memberReq.getMemberId(), memberReq.getPhoneNumber())) {
            return requestFail("修改成员'" + memberReq.getUsername() + "'失败，手机号码已存在");
        }
        MemberInfoDO member = BeanUtil.convert(memberReq, MemberInfoDO.class);

        return toRes(memberInfoService.modifyMember(member, memberReq.getPostIds()));
    }

    @Operation(summary = "重置密码")
    @OperateLog(title = "成员管理", subTitle = "重置密码", type = OperateTypeEnum.UPDATE)
    @PostMapping("/reset-password")
    @CheckTenantPerms("user-mod:member-mng:edit")
    public Result<Void> resetPwd(@RequestBody @Valid PwdResetReq req) {
        tenantInfoService.checkAdministrator(req.getId());
        MemberInfoDO update = new MemberInfoDO(req.getId());
        update.setPassword(passwordEncoder.encode(req.getPassword()));
        return toRes(memberInfoMapper.updateById(update));
    }

    @Operation(summary = "状态修改")
    @OperateLog(title = "成员管理", subTitle = "状态修改", type = OperateTypeEnum.UPDATE)
    @PostMapping("/edit-status")
    @CheckTenantPerms("user-mod:member-mng:edit")
    public Result<Void> editStatus(@RequestBody StatusEditReq editReq) {
        tenantInfoService.checkAdministrator(editReq.getId());
        MemberInfoDO update = new MemberInfoDO(editReq.getId());
        update.setStatus(editReq.getStatus());
        return toRes(memberInfoMapper.updateById(update));
    }

    @Operation(summary = "授权职位")
    @OperateLog(title = "成员管理", subTitle = "授权成员职位", type = OperateTypeEnum.GRANT)
    @PostMapping("/grant-post")
    @CheckTenantPerms("user-mod:member-mng:grant-post")
    public Result<Void> grantPosts(@RequestBody @Valid MemberMngGrantPostReq req) {
        tenantInfoService.checkAdministrator(req.getMemberId());
        return toRes(memberInfoService.grantPosts(req.getMemberId(), req.getPostIds()));
    }

    @Operation(summary = "删除成员")
    @OperateLog(title = "成员管理", subTitle = "删除成员", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckTenantPerms("user-mod:member-mng:remove")
    public Result<Void> remove(@RequestBody List<Long> memberIds) {
        tenantInfoService.checkAdministrator(memberIds);
        return toRes(memberInfoService.removeByIds(memberIds));
    }

}
