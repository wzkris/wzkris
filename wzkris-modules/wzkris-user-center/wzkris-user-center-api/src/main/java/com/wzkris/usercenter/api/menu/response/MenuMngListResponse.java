package com.wzkris.usercenter.api.menu.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单列表元素响应（Mng 轨集合元素 -> {域}MngListResponse）
 *
 * <p>继承 {@link MenuMngQueryResponse}，补充树形结构的子菜单字段。
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MenuMngListResponse extends MenuMngQueryResponse {

    @Schema(description = "子菜单")
    private List<MenuMngListResponse> children = new ArrayList<>();

}
