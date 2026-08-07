package com.wzkris.usercenter.controller.oauth2;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.security.annotation.CheckPerms;
import com.wzkris.usercenter.api.oauth2.OAuth2ClientMngApi;
import com.wzkris.usercenter.api.oauth2.request.ClientSecretUpdateRequest;
import com.wzkris.usercenter.api.oauth2.request.OAuth2ClientMngPageRequest;
import com.wzkris.usercenter.api.oauth2.request.OAuth2ClientMngSaveRequest;
import com.wzkris.usercenter.api.oauth2.request.OAuth2ClientMngUpdateRequest;
import com.wzkris.usercenter.api.oauth2.response.OAuth2ClientMngQueryResponse;
import com.wzkris.usercenter.api.oauth2.response.OAuth2ClientMngPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@Tag(name = "OAuth2客户端管理")
@RestController
@RequestMapping("/oauth2client-manage")
@RequiredArgsConstructor
public class OAuth2ClientMngController {

    private static final String PERM_PREFIX = "user-mod:oauth2client-mng:";

    private final OAuth2ClientMngApi oAuth2ClientMngApi;

    @Operation(summary = "分页")
    @GetMapping("/query-page")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "page")
    public Result<Page<OAuth2ClientMngPageResponse>> queryPage(@ParameterObject OAuth2ClientMngPageRequest request) {
        return oAuth2ClientMngApi.queryPage(request);
    }

    @Operation(summary = "根据id查详情")
    @GetMapping("/query-id/{id}")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "query")
    public Result<OAuth2ClientMngQueryResponse> queryById(@ParameterObject IdRequest request) {
        return oAuth2ClientMngApi.queryById(request);
    }

    @Operation(summary = "根据id修改客户端")
    @OperateLog(title = "OAuth2客户端管理", subTitle = "修改客户端", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "edit")
    public Result<Void> update(@RequestBody @Valid OAuth2ClientMngUpdateRequest request) {
        return oAuth2ClientMngApi.update(request);
    }

    @Operation(summary = "修改密钥")
    @OperateLog(title = "OAuth2客户端管理", subTitle = "修改密钥", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update-secret")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "edit-secret")
    public Result<Void> updateSecret(@RequestBody @Valid ClientSecretUpdateRequest request) {
        return oAuth2ClientMngApi.updateSecret(request);
    }

    @Operation(summary = "添加客户端")
    @OperateLog(title = "OAuth2客户端管理", subTitle = "添加客户端", type = OperateTypeEnum.INSERT)
    @PostMapping("/save")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "add")
    public Result<String> save(@RequestBody @Valid OAuth2ClientMngSaveRequest request) {
        return oAuth2ClientMngApi.save(request);
    }

    @Operation(summary = "删除客户端")
    @OperateLog(title = "OAuth2客户端管理", subTitle = "删除客户端", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "remove")
    public Result<Void> remove(@RequestBody @Valid IdRequest request) {
        return oAuth2ClientMngApi.remove(request);
    }

}

