package com.wzkris.user.domain.vo;

import com.wzkris.user.domain.SysPost;
import com.wzkris.user.domain.SysRole;
import com.wzkris.user.domain.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 用户授权信息
 */
@Data
public class SysUserGrantVO {
    @Schema(description = "用户信息")
    private SysUser user;

    @Schema(description = "可授权角色")
    private List<SysRole> roles;

    @Schema(description = "可授权岗位")
    private List<SysPost> posts;

    @Schema(description = "可授权部门")
    private List<SelectTreeVO> depts;

    @Schema(description = "已授权部门")
    private List<Long> postIds;

    @Schema(description = "已授权角色")
    private List<Long> roleIds;
}
