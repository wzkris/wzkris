package com.wzkris.usercenter.api.tenantlog.operate.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Schema(description = "租户个人操作日志信息")
public class TenantOperateLogInfoResponse {

    private Long id;

    @Schema(description = "操作模块")
    private String title;

    @Schema(description = "子模块")
    private String subTitle;

    @Schema(description = "操作类型（0其它 1新增 2修改 3删除 4授权 5导入导出）")
    private String operType;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "操作ip地址")
    private String operIp;

    @Schema(description = "操作地点")
    private String operLocation;

    @Schema(description = "操作状态")
    private Boolean success;

    @Schema(description = "错误消息")
    private String errorMsg;

    @Schema(description = "操作时间")
    private OffsetDateTime operTime;

    @Schema(description = "耗时（毫秒）")
    private Long costTime;

    @Schema(description = "链路追踪ID")
    private String traceId;

}
