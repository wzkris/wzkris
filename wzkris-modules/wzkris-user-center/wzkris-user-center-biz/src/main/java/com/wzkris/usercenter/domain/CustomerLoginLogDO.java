package com.wzkris.usercenter.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.OffsetDateTime;

/**
 * 用户登录日志
 *
 * @author wzkris
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(schema = "biz", value = "customer_login_log")
public class CustomerLoginLogDO extends BaseEntity {

    @Schema(description = "用户ID")
    private Long customerId;

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
