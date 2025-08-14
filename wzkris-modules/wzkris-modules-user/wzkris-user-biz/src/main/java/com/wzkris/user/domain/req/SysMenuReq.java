package com.wzkris.user.domain.req;

import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.validator.annotation.EnumsCheck;
import com.wzkris.user.constant.MenuConstants;
import com.wzkris.user.domain.SysMenu;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
@AutoMappers({@AutoMapper(target = SysMenu.class)})
@Schema(description = "系统菜单添加修改参数体")
public class SysMenuReq {

    private Long menuId;

    @NotBlank(message = "{invalidParameter.menuName.invalid}")
    @Size(min = 0, max = 30, message = "{invalidParameter.menuName.invalid}")
    @Schema(description = "菜单名称")
    private String menuName;

    @NotNull(message = "{invalidParameter.parentId.invalid}")
    @Schema(description = "父菜单ID")
    private Long parentId;

    @NotNull(message = "{invalidParameter.sort.invalid}")
    @Range(max = Integer.MAX_VALUE, message = "{invalidParameter.sort.invalid}")
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

    @NotBlank(message = "{invalidParameter.menuType.invalid}")
    @EnumsCheck(
            values = {
                    MenuConstants.TYPE_DIR,
                    MenuConstants.TYPE_MENU,
                    MenuConstants.TYPE_BUTTON,
                    MenuConstants.TYPE_INNERLINK,
                    MenuConstants.TYPE_OUTLINK
            }, message = "{invalidParameter.menuType.invalid}")
    @Schema(description = "菜单类型（D目录 M菜单 B按钮 I内链 O外链）")
    private String menuType;

    @EnumsCheck(values = {CommonConstants.STATUS_ENABLE, CommonConstants.STATUS_DISABLE},
            message = "{invalidParameter.status.invalid}")
    @Schema(description = "菜单状态（0正常 1停用）") // 停用状态在选择框无法显示，不显示的可以在选择框显示 路由不显示
    private String status;

    @Schema(description = "权限字符串")
    private String perms;

    @Schema(description = "菜单图标")
    private String icon;

}
