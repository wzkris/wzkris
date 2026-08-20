package com.wzkris.usercenter.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.usercenter.enums.menu.MenuScopeEnum;
import com.wzkris.usercenter.enums.menu.MenuStatusEnum;
import com.wzkris.usercenter.enums.menu.MenuTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 菜单权限表
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@TableName(schema = "biz", value = "menu_info")
public class MenuInfoDO extends BaseEntity {

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
    private Boolean cacheable;

    @Schema(description = "是否显示")
    private Boolean visible;

    @Schema(description = "菜单类型")
    private MenuTypeEnum menuType;

    @Schema(description = "菜单状态（0正常 1停用）") // 停用状态在选择框无法显示，不显示的可以在选择框显示 路由不显示
    private MenuStatusEnum status;

    @Schema(description = "权限字符串")
    private String perms;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "菜单域")
    private MenuScopeEnum scope;

    public MenuInfoDO(Long id) {
        this.setId(id);
    }

    public MenuInfoDO(MenuStatusEnum status) {
        this.status = status;
    }

}

