package com.wzkris.usercenter.controller.oauth2;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.excel.utils.ExcelUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckAdminPerms;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.domain.OAuth2ClientDO;
import com.wzkris.usercenter.domain.export.oauth2.OAuth2ClientExport;
import com.wzkris.usercenter.domain.req.EditStatusReq;
import com.wzkris.usercenter.domain.req.oauth2.EditClientSecretReq;
import com.wzkris.usercenter.domain.req.oauth2.OAuth2ClientMngQueryReq;
import com.wzkris.usercenter.domain.req.oauth2.OAuth2ClientMngReq;
import com.wzkris.usercenter.mapper.OAuth2ClientMapper;
import com.wzkris.usercenter.service.OAuth2ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
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
@RequiredArgsConstructor
public class OAuth2ClientMngController extends BaseController {

    private final PasswordEncoder passwordEncoder;

    private final OAuth2ClientMapper oauth2ClientMapper;

    private final OAuth2ClientService oAuth2ClientService;

    @Operation(summary = "分页")
    @GetMapping("/page")
    @CheckAdminPerms("user-mod:oauth2client-mng:page")
    public Result<Page<OAuth2ClientDO>> page(OAuth2ClientMngQueryReq req) {
        startPage();
        List<OAuth2ClientDO> list = oauth2ClientMapper.selectList(this.buildQueryWrapper(req));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<OAuth2ClientDO> buildQueryWrapper(OAuth2ClientMngQueryReq req) {
        return new LambdaQueryWrapper<OAuth2ClientDO>()
                .eq(StringUtil.isNotEmpty(req.getStatus()), OAuth2ClientDO::getStatus, req.getStatus())
                .like(StringUtil.isNotEmpty(req.getClientId()), OAuth2ClientDO::getClientId, req.getClientId());
    }

    @Operation(summary = "根据id查详情")
    @GetMapping("/{id}")
    @CheckAdminPerms("user-mod:oauth2client-mng:query")
    public Result<OAuth2ClientDO> query(@PathVariable Long id) {
        return ok(oauth2ClientMapper.selectById(id));
    }

    @Operation(summary = "根据id修改客户端")
    @OperateLog(title = "OAuth2客户端管理", subTitle = "修改客户端", type = OperateTypeEnum.UPDATE)
    @PostMapping("/edit")
    @CheckAdminPerms("user-mod:oauth2client-mng:edit")
    public Result<Void> edit(@RequestBody OAuth2ClientMngReq clientReq) {
        return toRes(oauth2ClientMapper.updateById(BeanUtil.convert(clientReq, OAuth2ClientDO.class)));
    }

    @Operation(summary = "修改密钥")
    @OperateLog(title = "OAuth2客户端管理", subTitle = "修改密钥", type = OperateTypeEnum.UPDATE)
    @PostMapping("/edit-secret")
    @CheckAdminPerms("user-mod:oauth2client-mng:edit-secret")
    public Result<Void> editSecret(@RequestBody @Valid EditClientSecretReq req) {
        OAuth2ClientDO update = new OAuth2ClientDO();
        update.setId(req.getId());
        update.setClientSecret(passwordEncoder.encode(req.getSecret()));
        return toRes(oauth2ClientMapper.updateById(update));
    }

    @Operation(summary = "状态修改")
    @OperateLog(title = "OAuth2客户端管理", subTitle = "状态修改", type = OperateTypeEnum.UPDATE)
    @PostMapping("/edit-status")
    @CheckAdminPerms("user-mod:oauth2client-mng:edit")
    public Result<Void> editStatus(@RequestBody EditStatusReq statusReq) {
        // 校验权限
        OAuth2ClientDO update = new OAuth2ClientDO();
        update.setId(statusReq.getId());
        update.setStatus(statusReq.getStatus());
        return toRes(oauth2ClientMapper.updateById(update));
    }

    @Operation(summary = "添加客户端")
    @OperateLog(title = "OAuth2客户端管理", subTitle = "添加客户端", type = OperateTypeEnum.INSERT)
    @PostMapping("/add")
    @CheckAdminPerms("user-mod:oauth2client-mng:add")
    public Result<String> add(@RequestBody @Valid OAuth2ClientMngReq clientReq) {
        OAuth2ClientDO client = BeanUtil.convert(clientReq, OAuth2ClientDO.class);

        String secret = RandomStringUtils.secure().nextAlphabetic(16);
        client.setClientSecret(passwordEncoder.encode(secret));
        oauth2ClientMapper.insert(client);
        return ok(secret);
    }

    @Operation(summary = "删除客户端")
    @OperateLog(title = "OAuth2客户端管理", subTitle = "删除客户端", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckAdminPerms("user-mod:oauth2client-mng:remove")
    public Result<Void> remove(@RequestBody Long id) {
        return toRes(oauth2ClientMapper.deleteById(id));
    }

    @Operation(summary = "导出")
    @OperateLog(title = "OAuth2客户端管理", subTitle = "导出客户端数据", type = OperateTypeEnum.EXPORT)
    @GetMapping("/export")
    @CheckAdminPerms("user-mod:oauth2client-mng:export")
    public void export(HttpServletResponse response, OAuth2ClientMngQueryReq queryReq) {
        List<OAuth2ClientDO> list = oauth2ClientMapper.selectList(this.buildQueryWrapper(queryReq));
        List<OAuth2ClientExport> convert = BeanUtil.convert(list, OAuth2ClientExport.class);
        ExcelUtil.exportExcel(convert, "客户端数据", OAuth2ClientExport.class, response);
    }

}
