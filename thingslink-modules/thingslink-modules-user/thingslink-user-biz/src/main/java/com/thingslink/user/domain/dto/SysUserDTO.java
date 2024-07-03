package com.thingslink.user.domain.dto;

import com.thingslink.user.domain.SysUser;
import lombok.Data;

import java.util.List;

/**
 * @description dto
 * @author  wzkris
 * @version  V1.0.0
 * @date  2023/1/14 16:20
 */
@Data
public class SysUserDTO extends SysUser {

    /** 角色组 */
    private List<Long> roleIds;

    /** 岗位组 */
    private List<Long> postIds;
}
