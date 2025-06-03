package com.wzkris.user.domain.vo;

import java.io.Serializable;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 路由元数据
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class MetaVO implements Serializable {

    /**
     * 设置该路由在侧边栏和面包屑中展示的名字
     */
    private String title;

    /**
     * 设置该路由的图标
     */
    private String icon;

    /**
     * 是否隐藏路由
     */
    private boolean hidden;

    /**
     * <keep-alive>缓存
     */
    private boolean keepAlive;

    /**
     * 路由参数：如 {"id": 1, "name": "ry"}
     */
    private Map<String, String> query;

    /**
     * 外部链接
     */
    private String link;

    public MetaVO(String title, String icon, boolean hidden, boolean keepAlive, Map<String, String> query) {
        this.title = title;
        this.icon = icon;
        this.hidden = hidden;
        this.keepAlive = keepAlive;
        this.query = query;
    }
}
