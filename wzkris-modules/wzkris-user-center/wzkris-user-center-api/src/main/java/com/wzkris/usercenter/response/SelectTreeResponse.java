package com.wzkris.usercenter.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.List;

/**
 * SelectTree树结构实体类
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class SelectTreeResponse extends SelectResponse {

    @Serial
    private static final long serialVersionUID = -3890661499660226052L;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Schema(description = "子节点")
    private List<SelectTreeResponse> children;

}

