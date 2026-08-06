package com.wzkris.usercenter.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseTenantEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.OffsetDateTime;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 租户登录日志
 * @date : 2023/8/26 14:35
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(schema = "biz", value = "tenant_login_log")
public class TenantLoginLogDO extends BaseTenantEntity {

    @Schema(description = "用户ID")
    private Long memberId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "登录类型")
    private String loginType;

    @Schema(description = "登录ip")
    private String loginIp;

    @Schema(description = "登录地址")
    private String loginLocation;

    @Schema(description = "链路追踪ID")
    private String traceId;

    @Schema(description = "原始UA")
    private String userAgent;

    @Schema(description = "登录状态")
    private Boolean success;

    @Schema(description = "失败信息")
    private String errorMsg;

    @Schema(description = "登录时间")
    private OffsetDateTime loginTime;

}
