package com.wzkris.usercenter.controller.post;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckTenantPerms;
import com.wzkris.common.security.enums.CheckMode;
import com.wzkris.common.security.utils.TenantUtil;
import com.wzkris.common.validator.group.ValidationGroups;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.domain.PostInfoDO;
import com.wzkris.usercenter.domain.req.EditStatusReq;
import com.wzkris.usercenter.domain.req.post.PostMngQueryReq;
import com.wzkris.usercenter.domain.req.post.PostMngReq;
import com.wzkris.usercenter.domain.vo.CheckedSelectTreeVO;
import com.wzkris.usercenter.mapper.PostInfoMapper;
import com.wzkris.usercenter.mapper.PostToMenuMapper;
import com.wzkris.usercenter.service.MenuInfoService;
import com.wzkris.usercenter.service.PostInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * 职位信息
 *
 * @author wzkris
 */
@Tag(name = "租户职位管理")
@Validated
@RestController
@RequestMapping("/post-manage")
@RequiredArgsConstructor
public class PostMngController extends BaseController {

    private final PostInfoMapper postInfoMapper;

    private final PostToMenuMapper postToMenuMapper;

    private final PostInfoService postInfoService;

    private final MenuInfoService menuInfoService;

    @Operation(summary = "职位分页")
    @GetMapping("/page")
    @CheckTenantPerms("user-mod:post-mng:page")
    public Result<Page<PostInfoDO>> page(PostMngQueryReq queryReq) {
        startPage();
        List<PostInfoDO> list = postInfoMapper.selectList(this.buildQueryWrapper(queryReq));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<PostInfoDO> buildQueryWrapper(PostMngQueryReq queryReq) {
        return new LambdaQueryWrapper<PostInfoDO>()
                .like(StringUtil.isNotEmpty(queryReq.getPostName()), PostInfoDO::getPostName, queryReq.getPostName())
                .eq(StringUtil.isNotEmpty(queryReq.getStatus()), PostInfoDO::getStatus, queryReq.getStatus())
                .orderByDesc(PostInfoDO::getPostSort, PostInfoDO::getPostId);
    }

    @Operation(summary = "职位详细信息")
    @GetMapping("/{postId}")
    @CheckTenantPerms("user-mod:post-mng:page")
    public Result<PostInfoDO> getInfo(@PathVariable Long postId) {
        return ok(postInfoMapper.selectById(postId));
    }

    @Operation(summary = "职位-菜单选择树")
    @GetMapping({"/menu-checked-selecttree/", "/menu-checked-selecttree/{postId}"})
    @CheckTenantPerms(
            value = {"user-mod:post-mng:edit", "user-mod:post-mng:add"},
            mode = CheckMode.OR)
    public Result<CheckedSelectTreeVO> roleMenuSelectTree(@PathVariable(required = false) Long postId) {
        CheckedSelectTreeVO checkedSelectTreeVO = new CheckedSelectTreeVO();
        checkedSelectTreeVO.setCheckedKeys(
                postId == null ? Collections.emptyList()
                        : postToMenuMapper.listMenuIdByPostIds(Collections.singletonList(postId)));
        checkedSelectTreeVO.setSelectTrees(menuInfoService.listTenantSelectTree(TenantUtil.getId()));
        return ok(checkedSelectTreeVO);
    }

    @Operation(summary = "新增职位")
    @OperateLog(title = "职位管理", subTitle = "新增职位", type = OperateTypeEnum.INSERT)
    @PostMapping("/add")
    @CheckTenantPerms("user-mod:post-mng:add")
    public Result<Void> add(@Validated @RequestBody PostMngReq req) {
        PostInfoDO post = BeanUtil.convert(req, PostInfoDO.class);
        return toRes(postInfoService.savePost(post, req.getMenuIds()));
    }

    @Operation(summary = "修改职位")
    @OperateLog(title = "职位管理", subTitle = "修改职位", type = OperateTypeEnum.UPDATE)
    @PostMapping("/edit")
    @CheckTenantPerms("user-mod:post-mng:edit")
    public Result<Void> edit(@Validated(value = ValidationGroups.Update.class) @RequestBody PostMngReq req) {
        PostInfoDO post = BeanUtil.convert(req, PostInfoDO.class);
        return toRes(postInfoService.modifyPost(post, req.getMenuIds()));
    }

    @Operation(summary = "状态修改")
    @OperateLog(title = "用户管理", subTitle = "状态修改", type = OperateTypeEnum.UPDATE)
    @PostMapping("/edit-status")
    @CheckTenantPerms("user-mod:post-mng:edit")
    public Result<Void> editStatus(@RequestBody EditStatusReq statusReq) {
        PostInfoDO update = new PostInfoDO(statusReq.getId());
        update.setStatus(statusReq.getStatus());
        return toRes(postInfoMapper.updateById(update));
    }

    @Operation(summary = "删除职位")
    @OperateLog(title = "职位管理", subTitle = "删除职位", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckTenantPerms("user-mod:post-mng:remove")
    public Result<Void> remove(@RequestBody @NotEmpty(message = "{invalidParameter.id.invalid}")
                               List<Long> postIds) {
        if (postInfoService.existMember(postIds)) {
            return requestFail("当前职位已被分配");
        }
        return toRes(postInfoService.removeByIds(postIds));
    }

}
