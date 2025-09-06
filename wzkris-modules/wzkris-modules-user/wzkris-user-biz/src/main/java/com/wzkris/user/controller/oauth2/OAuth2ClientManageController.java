package com.wzkris.user.controller.oauth2;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.excel.utils.ExcelUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckSystemPerms;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.user.domain.OAuth2ClientDO;
import com.wzkris.user.domain.export.oauth2.OAuth2ClientExport;
import com.wzkris.user.domain.req.EditStatusReq;
import com.wzkris.user.domain.req.oauth2.EditClientSecretReq;
import com.wzkris.user.domain.req.oauth2.OAuth2ClientManageQueryReq;
import com.wzkris.user.domain.req.oauth2.OAuth2ClientManageReq;
import com.wzkris.user.mapper.OAuth2ClientMapper;
import com.wzkris.user.service.OAuth2ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * OAuth2客户端
 *
 * @author wzkris
 */
@Tag(name = "OAuth2客户端管理")
@RestController
@RequestMapping("/oauth2client-manage")
@PreAuthorize("@su.isSuperTenant()")
@RequiredArgsConstructor
public class OAuth2ClientManageController extends BaseController {

    private final PasswordEncoder passwordEncoder;

    private final OAuth2ClientMapper oauth2ClientMapper;

    private final OAuth2ClientService oAuth2ClientService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @CheckSystemPerms("user-mod:oauth2client-mng:list")
    public Result<Page<OAuth2ClientDO>> listPage(OAuth2ClientManageQueryReq req) {
        startPage();
        List<OAuth2ClientDO> list = oauth2ClientMapper.selectList(this.buildQueryWrapper(req));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<OAuth2ClientDO> buildQueryWrapper(OAuth2ClientManageQueryReq req) {
        return new LambdaQueryWrapper<OAuth2ClientDO>()
                .eq(StringUtil.isNotEmpty(req.getStatus()), OAuth2ClientDO::getStatus, req.getStatus())
                .like(StringUtil.isNotEmpty(req.getClientId()), OAuth2ClientDO::getClientId, req.getClientId());
    }

    @Operation(summary = "根据id查详情")
    @GetMapping("/{id}")
    @CheckSystemPerms("user-mod:oauth2client-mng:query")
    public Result<OAuth2ClientDO> query(@PathVariable Long id) {
        return ok(oauth2ClientMapper.selectById(id));
    }

    @Operation(summary = "根据id修改客户端")
    @OperateLog(title = "OAuth2客户端管理", subTitle = "修改客户端", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckSystemPerms("user-mod:oauth2client-mng:edit")
    public Result<Void> edit(@RequestBody OAuth2ClientManageReq clientReq) {
        return toRes(oauth2ClientMapper.updateById(BeanUtil.convert(clientReq, OAuth2ClientDO.class)));
    }

    @Operation(summary = "修改密钥")
    @OperateLog(title = "OAuth2客户端管理", subTitle = "修改密钥", operateType = OperateType.UPDATE)
    @PostMapping("/edit-secret")
    @CheckSystemPerms("user-mod:oauth2client-mng:edit-secret")
    public Result<Void> editSecret(@RequestBody @Valid EditClientSecretReq req) {
        OAuth2ClientDO update = new OAuth2ClientDO();
        update.setId(req.getId());
        update.setClientSecret(passwordEncoder.encode(req.getSecret()));
        return toRes(oauth2ClientMapper.updateById(update));
    }

    @Operation(summary = "状态修改")
    @OperateLog(title = "OAuth2客户端管理", subTitle = "状态修改", operateType = OperateType.UPDATE)
    @PostMapping("/edit-status")
    @CheckSystemPerms("user-mod:oauth2client-mng:edit")
    public Result<Void> editStatus(@RequestBody EditStatusReq statusReq) {
        // 校验权限
        OAuth2ClientDO update = new OAuth2ClientDO();
        update.setId(statusReq.getId());
        update.setStatus(statusReq.getStatus());
        return toRes(oauth2ClientMapper.updateById(update));
    }

    @Operation(summary = "添加客户端")
    @OperateLog(title = "OAuth2客户端管理", subTitle = "添加客户端", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckSystemPerms("user-mod:oauth2client-mng:add")
    public Result<String> add(@RequestBody @Valid OAuth2ClientManageReq clientReq) {
        OAuth2ClientDO client = BeanUtil.convert(clientReq, OAuth2ClientDO.class);

        String secret = RandomStringUtils.secure().next(16);
        client.setClientSecret(passwordEncoder.encode(secret));
        oauth2ClientMapper.insert(client);
        return ok(secret);
    }

    @Operation(summary = "删除客户端")
    @OperateLog(title = "OAuth2客户端管理", subTitle = "删除客户端", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckSystemPerms("user-mod:oauth2client-mng:remove")
    public Result<Void> remove(@RequestBody Long id) {
        return toRes(oauth2ClientMapper.deleteById(id));
    }

    @Operation(summary = "导出")
    @OperateLog(title = "OAuth2客户端管理", subTitle = "导出客户端数据", operateType = OperateType.EXPORT)
    @GetMapping("/export")
    @CheckSystemPerms("user-mod:oauth2client-mng:export")
    public void export(HttpServletResponse response, OAuth2ClientManageQueryReq queryReq) {
        List<OAuth2ClientDO> list = oauth2ClientMapper.selectList(this.buildQueryWrapper(queryReq));
        List<OAuth2ClientExport> convert = BeanUtil.convert(list, OAuth2ClientExport.class);
        ExcelUtil.exportExcel(convert, "客户端数据", OAuth2ClientExport.class, response);
    }

}
