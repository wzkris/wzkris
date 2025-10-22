package com.wzkris.principal.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * 路由元数据
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class MetaVO implements Serializable {

    @Schema(description = "路由在侧边栏和面包屑中展示的名字")
    private String title;

    @Schema(description = "路由类型")
    private String type;

    @Schema(description = "路由图标")
    private String icon;

    @Schema(description = "是否隐藏")
    private boolean hidden;

    @Schema(description = "<keep-alive>缓存")
    private boolean keepAlive;

    @Schema(description = "路由参数：如 {\"id\": 1, \"name\": \"zhang3\"}")
    private Map<String, String> query;

    @Schema(description = "外部链接, http或https开头")
    private String link;

    public MetaVO(String title, String type, String icon,
                  boolean hidden, boolean keepAlive,
                  Map<String, String> query) {
        this.title = title;
        this.type = type;
        this.icon = icon;
        this.hidden = hidden;
        this.keepAlive = keepAlive;
        this.query = query;
    }

}
