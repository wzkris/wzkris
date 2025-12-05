package com.wzkris.message.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 租户登录日志
 * @date : 2023/8/26 14:35
 */
@Data
@TableName(schema = "biz", value = "tenant_login_log")
public class TenantLoginLogDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 5062210547731436343L;

    @TableId
    private Long logId;

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

    @Schema(description = "登录状态")
    private Boolean success;

    @Schema(description = "失败信息")
    private String errorMsg;

    @Schema(description = "浏览器类型")
    private String browser;

    @Schema(description = "操作系统")
    private String os;

    @Schema(description = "登录时间")
    private Date loginTime;

    @Schema(description = "租户ID")
    private Long tenantId;

}
