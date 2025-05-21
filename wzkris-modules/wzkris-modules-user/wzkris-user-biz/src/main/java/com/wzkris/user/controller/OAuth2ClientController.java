package com.wzkris.user.controller;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.BeanUtil;
import com.wzkris.common.excel.utils.ExcelUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.oauth2.annotation.CheckSystemPerms;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.domain.OAuth2Client;
import com.wzkris.user.domain.export.OAuth2ClientExport;
import com.wzkris.user.domain.req.EditClientSecretReq;
import com.wzkris.user.domain.req.EditStatusReq;
import com.wzkris.user.domain.req.OAuth2ClientQueryReq;
import com.wzkris.user.domain.req.OAuth2ClientReq;
import com.wzkris.user.mapper.OAuth2ClientMapper;
import com.wzkris.user.service.OAuth2ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/oauth2_client")
@RequiredArgsConstructor
public class OAuth2ClientController extends BaseController {

    private final PasswordEncoder passwordEncoder;

    private final OAuth2ClientMapper oauth2ClientMapper;

    private final OAuth2ClientService oAuth2ClientService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @CheckSystemPerms("oauth2_client:list")
    public Result<Page<OAuth2Client>> listPage(OAuth2ClientQueryReq req) {
        startPage();
        List<OAuth2Client> list = oauth2ClientMapper.selectList(this.buildQueryWrapper(req));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<OAuth2Client> buildQueryWrapper(OAuth2ClientQueryReq req) {
        return new LambdaQueryWrapper<OAuth2Client>()
                .eq(ObjUtil.isNotEmpty(req.getStatus()), OAuth2Client::getStatus, req.getStatus())
                .like(ObjUtil.isNotEmpty(req.getClientId()), OAuth2Client::getClientId, req.getClientId());
    }

    @Operation(summary = "根据id查详情")
    @GetMapping("/{id}")
    @CheckSystemPerms("oauth2_client:query")
    public Result<OAuth2Client> query(@PathVariable Long id) {
        return ok(oauth2ClientMapper.selectById(id));
    }

    @Operation(summary = "根据id修改客户端")
    @OperateLog(title = "OAuth2客户端管理", subTitle = "修改客户端", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckSystemPerms("oauth2_client:edit")
    public Result<Void> edit(@RequestBody OAuth2ClientReq clientReq) {
        return toRes(oauth2ClientMapper.updateById(BeanUtil.convert(clientReq, OAuth2Client.class)));
    }

    @Operation(summary = "修改密钥")
    @OperateLog(title = "OAuth2客户端管理", subTitle = "修改密钥", operateType = OperateType.UPDATE)
    @PostMapping("/edit_secret")
    @CheckSystemPerms("oauth2_client:edit_secret")
    public Result<Void> editSecret(@RequestBody @Valid EditClientSecretReq req) {
        OAuth2Client update = new OAuth2Client();
        update.setId(req.getId());
        update.setClientSecret(passwordEncoder.encode(req.getSecret()));
        return toRes(oauth2ClientMapper.updateById(update));
    }

    @Operation(summary = "状态修改")
    @OperateLog(title = "OAuth2客户端管理", subTitle = "状态修改", operateType = OperateType.UPDATE)
    @PostMapping("/edit_status")
    @CheckSystemPerms("app_user:edit")
    public Result<Void> editStatus(@RequestBody EditStatusReq statusReq) {
        // 校验权限
        OAuth2Client update = new OAuth2Client();
        update.setId(statusReq.getId());
        update.setStatus(statusReq.getStatus());
        return toRes(oauth2ClientMapper.updateById(update));
    }

    @Operation(summary = "添加客户端")
    @OperateLog(title = "OAuth2客户端管理", subTitle = "添加客户端", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckSystemPerms("oauth2_client:add")
    public Result<String> add(@RequestBody @Valid OAuth2ClientReq clientReq) {
        OAuth2Client client = BeanUtil.convert(clientReq, OAuth2Client.class);

        String secret = RandomUtil.randomString(16);
        client.setClientSecret(passwordEncoder.encode(secret));
        oauth2ClientMapper.insert(client);
        return ok(secret);
    }

    @Operation(summary = "删除客户端")
    @OperateLog(title = "OAuth2客户端管理", subTitle = "删除客户端", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckSystemPerms("oauth2_client:remove")
    public Result<Void> remove(@RequestBody Long id) {
        return toRes(oauth2ClientMapper.deleteById(id));
    }

    @Operation(summary = "导出")
    @OperateLog(title = "OAuth2客户端管理", subTitle = "导出客户端数据", operateType = OperateType.EXPORT)
    @GetMapping("/export")
    @CheckSystemPerms("oauth2_client:export")
    public void export(HttpServletResponse response, OAuth2ClientQueryReq queryReq) {
        List<OAuth2Client> list = oauth2ClientMapper.selectList(this.buildQueryWrapper(queryReq));
        List<OAuth2ClientExport> convert = BeanUtil.convert(list, OAuth2ClientExport.class);
        ExcelUtil.exportExcel(convert, "客户端数据", OAuth2ClientExport.class, response);
    }
}
