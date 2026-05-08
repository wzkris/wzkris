package com.wzkris.usercenter.request.menu;

import com.wzkris.usercenter.enums.menu.MenuScopeEnum;
import com.wzkris.usercenter.enums.menu.MenuStatusEnum;
import com.wzkris.usercenter.enums.menu.MenuTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
@Schema(description = "新增菜单参数体")
public class MenuMngSaveRequest {

    @NotBlank(message = "{invalidParameter.menuName.invalid}")
    @Size(min = 0, max = 30, message = "{invalidParameter.menuName.invalid}")
    @Schema(description = "菜单名称")
    private String menuName;

    @NotNull(message = "{invalidParameter.parentId.invalid}")
    @Schema(description = "父菜单 ID")
    private Long parentId;

    @NotNull(message = "{invalidParameter.sort.invalid}")
    @Range(max = Integer.MAX_VALUE, message = "{invalidParameter.sort.invalid}")
    @Schema(description = "显示顺序")
    private Integer menuSort;

    @Schema(description = "路由地址 (地址栏展示的地址)")
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
    @Schema(description = "菜单类型")
    private MenuTypeEnum menuType;

    @Schema(description = "菜单状态（0 正常 1 停用）")
    private MenuStatusEnum status;

    @Schema(description = "权限字符串")
    private String perms;

    @Schema(description = "菜单图标")
    private String icon;

    @NotBlank(message = "{invalidParameter.menuScope.invalid}")
    @Schema(description = "菜单域")
    private MenuScopeEnum scope;

}

