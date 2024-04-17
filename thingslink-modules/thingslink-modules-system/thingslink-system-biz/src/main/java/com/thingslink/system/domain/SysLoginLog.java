package com.thingslink.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thingslink.system.api.domain.LoginLogDTO;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "IP实际地址")
    private String address;

    @Schema(description = "IP地址")
    private String ipAddr;

    @Schema(description = "浏览器类型")
    private String browser;

    @Schema(description = "操作系统")
    private String os;

    @Schema(description = "登录时间")
    private Long loginTime;

    @JsonIgnore
    @TableField(exist = false)
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>(2);
        }
        return params;
    }
}
