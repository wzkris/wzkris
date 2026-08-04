package com.wzkris.common.log.remote.request;

import com.wzkris.common.core.enums.AuthTypeEnum;
import lombok.Data;

import java.time.OffsetDateTime;

/**
 * 操作事件
 *
 * @author wzkris
 * @date 2025/10/13
 */
@Data
public class OperateLogEvent {

    /**
     * 操作模块
     */
    private String title;

    /**
     * 子模块
     */
    private String subTitle;

    /**
     * 操作类型
     */
    private String operType;

    /**
     * 方法
     */
    private String method;

    /**
     * http方法
     */
    private String httpMethod;

    /**
     * 认证类型
     */
    private AuthTypeEnum authType;

    /**
     * 操作人员ID
     */
    private Long operatorId;

    /**
     * 操作人员
     */
    private String operName;

    /**
     * 请求url
     */
    private String httpUrl;

    /**
     * 操作地址
     */
    private String operIp;

    /**
     * 请求参数
     */
    private String operParam;

    /**
     * 返回参数
     */
    private String jsonResult;

    /**
     * 操作地点
     */
    private String operLocation;

    /**
     * 操作状态
     */
    private Boolean success;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 操作时间
     */
    private OffsetDateTime operTime;

    /**
     * 耗时（毫秒）
     */
    private Long costTime;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 链路追踪ID
     */
    private String traceId;

}
