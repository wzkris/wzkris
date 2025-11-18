package com.wzkris.message.feign.operatelog.req;

import lombok.Data;

import java.util.Date;

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
     * 请求方法
     */
    private String method;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 认证类型
     */
    private String authType;

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
    private String operUrl;

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
    private Date operTime;

    /**
     * 租户ID
     */
    private Long tenantId;

}
