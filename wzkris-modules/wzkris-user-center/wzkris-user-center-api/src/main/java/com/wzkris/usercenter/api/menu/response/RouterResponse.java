package com.wzkris.usercenter.api.menu.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class RouterResponse implements Serializable {

    @Schema(description = "路由元数据")
    private MetaResponse meta;

    @Schema(description = "路由地址")
    private String path;

    @Schema(description = "组件地址")
    private String component;

    @Schema(description = "子路由")
    private List<RouterResponse> children;

}

