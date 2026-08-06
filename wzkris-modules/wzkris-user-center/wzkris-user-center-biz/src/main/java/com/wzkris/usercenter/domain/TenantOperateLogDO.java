package com.wzkris.usercenter.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseTenantEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.OffsetDateTime;

/**
 * 租户操作日志
 *
 * @author wzkris
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(schema = "biz", value = "tenant_operate_log")
public class TenantOperateLogDO extends BaseTenantEntity {

    @Schema(description = "操作模块")
    private String title;

    @Schema(description = "子模块")
    private String subTitle;

    @Schema(description = "操作类型（0其它 1新增 2修改 3删除 4授权 5导入导出）")
    private String operType;

    @Schema(description = "请求方法")
    private String method;

    @Schema(description = "请求方式")
    private String httpMethod;

    @Schema(description = "职工ID")
    private Long memberId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "请求url")
    private String httpUrl;

    @Schema(description = "操作ip地址")
    private String operIp;

    @Schema(description = "请求参数")
    private String operParam;

    @Schema(description = "返回参数")
    private String jsonResult;

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
