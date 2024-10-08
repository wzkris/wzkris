package com.wzkris.common.security.oauth2.domain.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 登录用户信息
 * @date : 2023/8/7 16:38
 * @UPDATE： 2024/4/9 09:29
 */
@Data
@Accessors(chain = true)
public class LoginSyser implements Serializable {

    // 登录id
    private Long userId;
    // 部门id
    private Long deptId;
    // 租户id
    private Long tenantId;
    // 是否当前租户下的最高管理员
    private boolean administrator;
    // 用户名
    private String username;
    // 密码
    private String password;
    // 部门数据权限， 最终会拼接到SQL中
    private List<Long> deptScopes;

}