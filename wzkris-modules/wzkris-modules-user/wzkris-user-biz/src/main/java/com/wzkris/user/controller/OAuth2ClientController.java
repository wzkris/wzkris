package com.wzkris.user.controller;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.page.Page;
import com.wzkris.user.domain.OAuth2Client;
import com.wzkris.user.mapper.OAuth2ClientMapper;
import com.wzkris.user.service.OAuth2ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    private final OAuth2ClientMapper oauth2ClientMapper;
    private final OAuth2ClientService oAuth2ClientService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('oauth2:client:list')")
    public Result<Page<OAuth2Client>> list(OAuth2Client client) {
        return success(oAuth2ClientService.listPage(client));
    }
}
