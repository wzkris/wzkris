package com.wzkris.system.domain.req;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Schema(description = "筛选条件")
public class SysLoginLogQueryReq {

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "登录状态（0正常 1异常）")
    private String status;

    @Schema(description = "登录地址")
    private String loginLocation;

    @JsonIgnore
    @TableField(exist = false)
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>(2);
        }
        return params;
    }
}
