package com.thingslink.user.domain.dto;

import com.thingslink.user.domain.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description dto
 * @author  wzkris
 * @version  V1.0.0
 * @date  2023/1/14 16:20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserDTO extends SysUser {

    /** 角色组 */
    private Long[] roleIds;

    /** 岗位组 */
    private Long[] postIds;
}
