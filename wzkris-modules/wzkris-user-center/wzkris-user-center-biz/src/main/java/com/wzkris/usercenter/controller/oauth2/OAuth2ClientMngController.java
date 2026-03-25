package com.wzkris.usercenter.controller.oauth2;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckAdminPerms;
import com.wzkris.usercenter.api.oauth2.OAuth2ClientMngApi;
import com.wzkris.usercenter.request.StatusUpdateRequest;
import com.wzkris.usercenter.request.oauth2.ClientSecretUpdateRequest;
import com.wzkris.usercenter.request.oauth2.OAuth2ClientMngQueryRequest;
import com.wzkris.usercenter.request.oauth2.OAuth2ClientMngSaveRequest;
import com.wzkris.usercenter.request.oauth2.OAuth2ClientMngUpdateRequest;
import com.wzkris.usercenter.response.oauth2.OAuth2ClientMngResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    private final OAuth2ClientMngApi oAuth2ClientMngApi;

    @Operation(summary = "分页")
    @GetMapping("/query-page")
    @CheckAdminPerms("user-mod:oauth2client-mng:page")
    public Result<Page<OAuth2ClientMngResponse>> queryPage(OAuth2ClientMngQueryRequest request) {
        return oAuth2ClientMngApi.queryPage(request);
    }

    @Operation(summary = "根据id查详情")
    @GetMapping("/query-info/{id}")
    @CheckAdminPerms("user-mod:oauth2client-mng:query")
    public Result<OAuth2ClientMngResponse> queryInfo(@PathVariable Long id) {
        return oAuth2ClientMngApi.queryInfo(id);
    }

    @Operation(summary = "根据id修改客户端")
    @OperateLog(title = "OAuth2客户端管理", subTitle = "修改客户端", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update")
    @CheckAdminPerms("user-mod:oauth2client-mng:edit")
    public Result<Void> update(@RequestBody @Valid OAuth2ClientMngUpdateRequest request) {
        return oAuth2ClientMngApi.update(request);
    }

    @Operation(summary = "修改密钥")
    @OperateLog(title = "OAuth2客户端管理", subTitle = "修改密钥", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update-secret")
    @CheckAdminPerms("user-mod:oauth2client-mng:edit-secret")
    public Result<Void> updateSecret(@RequestBody @Valid ClientSecretUpdateRequest request) {
        return oAuth2ClientMngApi.updateSecret(request);
    }

    @Operation(summary = "状态修改")
    @OperateLog(title = "OAuth2客户端管理", subTitle = "状态修改", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update-status")
    @CheckAdminPerms("user-mod:oauth2client-mng:edit")
    public Result<Void> updateStatus(@RequestBody StatusUpdateRequest request) {
        return oAuth2ClientMngApi.updateStatus(request);
    }

    @Operation(summary = "添加客户端")
    @OperateLog(title = "OAuth2客户端管理", subTitle = "添加客户端", type = OperateTypeEnum.INSERT)
    @PostMapping("/save")
    @CheckAdminPerms("user-mod:oauth2client-mng:add")
    public Result<String> save(@RequestBody @Valid OAuth2ClientMngSaveRequest request) {
        return oAuth2ClientMngApi.save(request);
    }

    @Operation(summary = "删除客户端")
    @OperateLog(title = "OAuth2客户端管理", subTitle = "删除客户端", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckAdminPerms("user-mod:oauth2client-mng:remove")
    public Result<Void> remove(@RequestBody Long id) {
        return oAuth2ClientMngApi.remove(id);
    }

}

