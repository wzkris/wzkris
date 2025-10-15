package com.wzkris.message.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.message.feign.stafflog.req.StaffOperateLogReq;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志记录表 sys_oper_log
 *
 * @author wzkris
 */
@Data
@AutoMapper(target = StaffOperateLogReq.class)
@TableName(schema = "biz", value = "staff_operate_log")
public class StaffOperateLogDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 3562067975534801419L;

    @TableId
    private Long operId;

    @Schema(description = "操作模块")
    private String title;

    @Schema(description = "子模块")
    private String subTitle;

    @Schema(description = "操作类型（0其它 1新增 2修改 3删除）")
    private String operType;

    @Schema(description = "请求方法")
    private String method;

    @Schema(description = "请求方式")
    private String requestMethod;

    @Schema(description = "操作人员ID")
    private Long staffId;

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

    @Schema(description = "操作状态")
    private Boolean success;

    @Schema(description = "错误消息")
    private String errorMsg;

    @Schema(description = "操作时间")
    private Date operTime;

    @Schema(description = "租户ID")
    private Long tenantId;

}
