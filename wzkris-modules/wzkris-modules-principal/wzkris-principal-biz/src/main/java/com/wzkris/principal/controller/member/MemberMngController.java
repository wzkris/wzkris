package com.wzkris.principal.controller.member;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckTenantPerms;
import com.wzkris.common.security.enums.CheckMode;
import com.wzkris.common.security.utils.TenantUtil;
import com.wzkris.common.validator.group.ValidationGroups;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.principal.domain.MemberInfoDO;
import com.wzkris.principal.domain.req.EditStatusReq;
import com.wzkris.principal.domain.req.ResetPwdReq;
import com.wzkris.principal.domain.req.member.MemberMngQueryReq;
import com.wzkris.principal.domain.req.member.MemberMngReq;
import com.wzkris.principal.domain.req.member.MemberToPostsReq;
import com.wzkris.principal.domain.vo.CheckedSelectVO;
import com.wzkris.principal.domain.vo.member.MemberMngVO;
import com.wzkris.principal.listener.event.CreateMemberEvent;
import com.wzkris.principal.mapper.MemberInfoMapper;
import com.wzkris.principal.service.MemberInfoService;
import com.wzkris.principal.service.PostInfoService;
import com.wzkris.principal.service.TenantInfoService;
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
    @GetMapping("/list")
    @CheckTenantPerms("prin-mod:member-mng:list")
    public Result<Page<MemberMngVO>> listPage(MemberMngQueryReq queryReq) {
        startPage();
        List<MemberMngVO> list = memberInfoMapper.listVO(this.buildPageWrapper(queryReq));
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
    @CheckTenantPerms("prin-mod:member-mng:list")
    public Result<MemberInfoDO> getInfo(@PathVariable Long memberId) {
        tenantInfoService.checkAdministrator(memberId);
        return ok(memberInfoMapper.selectById(memberId));
    }

    @Operation(summary = "成员-职位选择列表")
    @GetMapping({"/post-checked-select/", "/post-checked-select/{memberId}"})
    @CheckTenantPerms(
            value = {"prin-mod:member-mng:edit", "prin-mod:member-mng:add"},
            mode = CheckMode.OR)
    public Result<CheckedSelectVO> postSelect(@PathVariable(required = false) Long memberId, String postName) {
        CheckedSelectVO checkedSelectVO = new CheckedSelectVO();
        checkedSelectVO.setCheckedKeys(memberId == null ? Collections.emptyList() : postInfoService.listIdByMemberId(memberId));
        checkedSelectVO.setSelects(postInfoService.listSelect(postName));
        return ok(checkedSelectVO);
    }

    @Operation(summary = "新增成员")
    @OperateLog(title = "成员管理", subTitle = "新增成员", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckTenantPerms("prin-mod:member-mng:add")
    public Result<Void> add(@Validated(ValidationGroups.Insert.class) @RequestBody MemberMngReq memberReq) {
        if (!tenantInfoService.checkAccountLimit(TenantUtil.getTenantId())) {
            return err40000("账号数量已达上限，请联系管理员");
        } else if (memberInfoService.existByUsername(memberReq.getMemberId(), memberReq.getUsername())) {
            return err40000("添加成员'" + memberReq.getUsername() + "'失败，登录账号已存在");
        } else if (StringUtil.isNotEmpty(memberReq.getPhoneNumber())
                && memberInfoService.existByPhoneNumber(memberReq.getMemberId(), memberReq.getPhoneNumber())) {
            return err40000("添加成员'" + memberReq.getUsername() + "'失败，手机号码已存在");
        }
        MemberInfoDO member = BeanUtil.convert(memberReq, MemberInfoDO.class);
        String password = RandomStringUtils.secure().nextAlphabetic(8);
        member.setPassword(password);

        boolean success = memberInfoService.saveMember(member, memberReq.getPostIds());
        if (success) {
            SpringUtil.getContext()
                    .publishEvent(new CreateMemberEvent(TenantUtil.getId(), memberReq.getUsername(), password));
        }
        return toRes(success);
    }

    @Operation(summary = "修改成员")
    @OperateLog(title = "成员管理", subTitle = "修改成员", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckTenantPerms("prin-mod:member-mng:edit")
    public Result<Void> edit(@Validated @RequestBody MemberMngReq memberReq) {
        tenantInfoService.checkAdministrator(memberReq.getMemberId());
        if (memberInfoService.existByUsername(memberReq.getMemberId(), memberReq.getUsername())) {
            return err40000("修改成员'" + memberReq.getUsername() + "'失败，登录账号已存在");
        } else if (StringUtil.isNotEmpty(memberReq.getPhoneNumber())
                && memberInfoService.existByPhoneNumber(memberReq.getMemberId(), memberReq.getPhoneNumber())) {
            return err40000("修改成员'" + memberReq.getUsername() + "'失败，手机号码已存在");
        }
        MemberInfoDO member = BeanUtil.convert(memberReq, MemberInfoDO.class);

        return toRes(memberInfoService.modifyMember(member, memberReq.getPostIds()));
    }

    @Operation(summary = "重置密码")
    @OperateLog(title = "成员管理", subTitle = "重置密码", operateType = OperateType.UPDATE)
    @PostMapping("/reset-password")
    @CheckTenantPerms("prin-mod:member-mng:edit")
    public Result<Void> resetPwd(@RequestBody @Valid ResetPwdReq req) {
        tenantInfoService.checkAdministrator(req.getId());
        MemberInfoDO update = new MemberInfoDO(req.getId());
        update.setPassword(passwordEncoder.encode(req.getPassword()));
        return toRes(memberInfoMapper.updateById(update));
    }

    @Operation(summary = "状态修改")
    @OperateLog(title = "成员管理", subTitle = "状态修改", operateType = OperateType.UPDATE)
    @PostMapping("/edit-status")
    @CheckTenantPerms("prin-mod:member-mng:edit")
    public Result<Void> editStatus(@RequestBody EditStatusReq statusReq) {
        tenantInfoService.checkAdministrator(statusReq.getId());
        MemberInfoDO update = new MemberInfoDO(statusReq.getId());
        update.setStatus(statusReq.getStatus());
        return toRes(memberInfoMapper.updateById(update));
    }

    @Operation(summary = "授权职位")
    @OperateLog(title = "成员管理", subTitle = "授权成员职位", operateType = OperateType.GRANT)
    @PostMapping("/grant-post")
    @CheckTenantPerms("prin-mod:member-mng:grant-post")
    public Result<Void> grantPosts(@RequestBody @Valid MemberToPostsReq req) {
        tenantInfoService.checkAdministrator(req.getMemberId());
        return toRes(memberInfoService.grantPosts(req.getMemberId(), req.getPostIds()));
    }

    @Operation(summary = "删除成员")
    @OperateLog(title = "成员管理", subTitle = "删除成员", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckTenantPerms("prin-mod:member-mng:remove")
    public Result<Void> remove(@RequestBody List<Long> memberIds) {
        tenantInfoService.checkAdministrator(memberIds);
        return toRes(memberInfoService.removeByIds(memberIds));
    }

}
