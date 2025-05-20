package com.wzkris.user.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单权限表 sys_menu
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@TableName(schema = "biz")
public class SysMenu extends BaseEntity {

    @TableId
    private Long menuId;

    @Schema(description = "菜单名称")
    private String menuName;

    @Schema(description = "父菜单ID")
    private Long parentId;

    @Schema(description = "显示顺序")
    private Integer menuSort;

    @Schema(description = "路由地址(地址栏展示的地址)")
    private String path;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "路由参数")
    private String query;

    @Schema(description = "是否缓存")
    private Boolean isCache;

    @Schema(description = "是否显示")
    private Boolean isVisible;

    @Schema(description = "菜单类型（D目录 M菜单 B按钮 I内链 O外链）")
    private String menuType;

    @Schema(description = "菜单状态（0正常 1停用）")// 停用状态在选择框无法显示，不显示的可以在选择框显示 路由不显示
    private String status;

    @Schema(description = "权限字符串")
    private String perms;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "子菜单")
    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();

    public SysMenu(Long menuId) {
        this.menuId = menuId;
    }

    public SysMenu(String status) {
        this.status = status;
    }
}
