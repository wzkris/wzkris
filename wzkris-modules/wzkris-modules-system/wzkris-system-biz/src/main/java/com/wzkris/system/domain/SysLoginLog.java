package com.wzkris.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.wzkris.system.api.domain.LoginLogDTO;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 后台登录日志
 * @date : 2023/8/26 14:35
 */
@Data
@AutoMapper(target = LoginLogDTO.class)
@Accessors(chain = true)
public class SysLoginLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 5062210547731436343L;

    @TableId
    private Long logId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "登录ip")
    private String loginIp;

    @Schema(description = "登录地址")
    private String loginLocation;

    @Schema(description = "登录状态（0正常 1异常）")
    private String status;

    @Schema(description = "浏览器类型")
    private String browser;

    @Schema(description = "操作系统")
    private String os;

    @Schema(description = "登录时间")
    private Long loginTime;

}
