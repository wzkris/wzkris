package com.thingslink.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thingslink.system.api.domain.OperLogDTO;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 操作日志记录表 sys_oper_log
 *
 * @author wzkris
 */
@Data
@AutoMapper(target = OperLogDTO.class)
@Accessors(chain = true)
public class SysOperLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 3562067975534801419L;

    @TableId
    private Long operId;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "操作模块")
    private String title;

    @Schema(description = "操作类型（0其它 1新增 2修改 3删除）")
    private String operType;

    @Schema(description = "请求方法")
    private String method;

    @Schema(description = "请求方式")
    private String requestMethod;

    @Schema(description = "操作人员")
    private String operName;

    @Schema(description = "请求url")
    private String operUrl;

    @Schema(description = "操作地址")
    private String operIp;

    @Schema(description = "请求参数")
    private String operParam;

    @Schema(description = "返回参数")
    private String jsonResult;

    @Schema(description = "操作地点")
    private String operLocation;

    @Schema(description = "操作状态（0正常 1异常）")
    private String status;

    @Schema(description = "错误消息")
    private String errorMsg;

    @Schema(description = "操作时间")
    private Long operTime;

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
