package com.wzkris.user.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 路由配置信息
 *
 * @author wzkris
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouterVO implements Serializable {

    /**
     * 其他元素
     */
    private MetaVO meta;

    /**
     * 路由名字
     */
    private String name;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件地址
     */
    private String component;

    /**
     * 子路由
     */
    private List<RouterVO> children;
}
