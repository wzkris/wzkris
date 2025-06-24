package com.wzkris.auth.controller;

import com.wzkris.auth.domain.resp.AppUserinfo;
import com.wzkris.auth.domain.resp.SysUserinfo;
import com.wzkris.common.core.constant.HeaderConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.security.utils.ClientUserUtil;
import com.wzkris.common.security.utils.SystemUserUtil;
import com.wzkris.common.web.model.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户信息端点")
@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserinfoEndpoint extends BaseController {

    @Operation(summary = "用户信息")
    @GetMapping(value = "/user_info", headers = HeaderConstants.X_TENANT_TOKEN)
    public Result<SysUserinfo> systemUser() {
        SysUserinfo userinfo = new SysUserinfo();
        userinfo.setAdmin(SystemUserUtil.isAdmin());
        userinfo.setSuperTenant(SystemUserUtil.isSuperTenant());
        userinfo.setUsername(SystemUserUtil.getUsername());
        userinfo.setAuthorities(SystemUserUtil.getAuthorities());
        return ok(userinfo);
    }

    @Operation(summary = "用户信息")
    @GetMapping(value = "/user_info", headers = HeaderConstants.X_USER_TOKEN)
    public Result<AppUserinfo> clientUser() {
        AppUserinfo userinfo = new AppUserinfo();
        userinfo.setPhoneNumber(ClientUserUtil.getPhoneNumber());
        return ok(userinfo);
    }

    @GetMapping("/oauth2/authorization_code_callback")
    public ResponseEntity<?> callback(String code, String error, String error_description) {
        if (code == null || code.isEmpty()) {
            log.info("error code:{}, desc: {}", error, error_description);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } else {
            log.info("code: {} ", code);
            return new ResponseEntity<>(code, HttpStatus.OK);
        }
    }

}
