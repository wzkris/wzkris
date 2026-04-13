package com.wzkris.usercenter.controller.tenantpackage;

import com.wzkris.usercenter.api.tenantpackage.TenantPackageMngApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "租户套餐信息")
@Validated
@RestController
@RequestMapping("/tenant-package-info")
@RequiredArgsConstructor
public class TenantPackageInfoController {

    private final TenantPackageMngApi tenantPackageMngApi;

}

