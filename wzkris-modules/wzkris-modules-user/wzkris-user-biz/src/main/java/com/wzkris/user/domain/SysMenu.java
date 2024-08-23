package com.wzkris.user.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单权限表 sys_menu
 *
 * @author wzkris
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class SysMenu extends BaseEntity {

    @TableId
    private Long menuId;

    @NotBlank(message = "[menuName] {validate.notnull}")
    @Size(min = 0, max = 30, message = "[menuName] {validate.size.illegal}")
    @Schema(description = "菜单名称")
    private String menuName;

    @Schema(description = "父菜单ID")
    private Long parentId;

    @NotNull(message = "[menuSort] {validate.notnull}")
    @Schema(description = "显示顺序")
    private Integer menuSort;

    @Size(min = 0, max = 50, message = "[path] {validate.size.illegal}")
    @Schema(description = "路由地址(地址栏展示的地址)")
    private String path;

    @Size(min = 0, max = 50, message = "[component] {validate.size.illegal}")
    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "路由参数")
    private String query;

    @Schema(description = "是否外链")
    private Boolean isFrame;

    @Schema(description = "是否缓存")
    private Boolean isCache;

    @Schema(description = "是否显示")
    private Boolean isVisible;

    @Schema(description = "是否租户专用")
    private Boolean isTenant;

    @NotBlank(message = "[menuType] {validate.notnull}")
    @Schema(description = "菜单类型（M目录 C菜单 F按钮）")
    private String menuType;

    @Schema(description = "菜单状态（0正常 1停用）")
    private String status;

    @Size(min = 0, max = 30, message = "[perms] {validate.size.illegal}")
    @Schema(description = "权限字符串")
    private String perms;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "子菜单")
    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<SysMenu>();

    public SysMenu(Long menuId) {
        this.menuId = menuId;
    }

    public SysMenu(String status) {
        this.status = status;
    }
}
