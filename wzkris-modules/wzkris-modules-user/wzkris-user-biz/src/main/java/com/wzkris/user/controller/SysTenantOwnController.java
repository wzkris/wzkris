package com.wzkris.user.controller;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.MapstructUtil;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.security.utils.SysUtil;
import com.wzkris.user.domain.SysTenant;
import com.wzkris.user.domain.vo.SysTenantVO;
import com.wzkris.user.mapper.SysTenantMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 自身租户信息
 *
 * @author wzkris
 */
@Tag(name = "租户信息")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/tenant")
public class SysTenantOwnController extends BaseController {
    private final SysTenantMapper sysTenantMapper;

    @Operation(summary = "获取自身租户")
    @GetMapping("/getinfo")
    @PreAuthorize("@ps.hasPerms('tenant:getinfo')")
    public Result<SysTenantVO> getInfo() {
        Long tenantId = SysUtil.getTenantId();
        SysTenantVO tenantVO;
        if (SysTenant.isSuperTenant(tenantId)) {
            tenantVO = new SysTenantVO(true);// 超级租户不存在信息
        }
        else {
            SysTenant tenant = sysTenantMapper.selectById(tenantId);
            tenantVO = MapstructUtil.convert(tenant, SysTenantVO.class);
            tenantVO.setSuperT(false);
        }
        return success(tenantVO);
    }
}
