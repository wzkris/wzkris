package com.thingslink.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 后台登录日志
 * @date : 2023/8/26 14:35
 */
@Data
@Accessors(chain = true)
public class LoginLog {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId
    private Long logId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 地址
     */
    private String address;

    /**
     * ip地址
     */
    private String ipAddr;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;

    /**
     * 请求参数
     */
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
