package com.wzkris.common.log.event;

import com.wzkris.message.feign.stafflog.req.OperateLogReq;
import lombok.Data;

import java.util.Date;

/**
 * 操作事件
 *
 * @author wzkris
 * @date 2025/10/13
 */
@Data
public class OperateEvent {

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
    private boolean success;

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

    public com.wzkris.message.feign.adminlog.req.OperateLogReq toUserOperateLogReq() {
        com.wzkris.message.feign.adminlog.req.OperateLogReq operateLogReq = new com.wzkris.message.feign.adminlog.req.OperateLogReq();
        operateLogReq.setTitle(this.getTitle());
        operateLogReq.setSubTitle(this.getSubTitle());
        operateLogReq.setOperType(this.getOperType());
        operateLogReq.setMethod(this.getMethod());
        operateLogReq.setRequestMethod(this.getRequestMethod());
        operateLogReq.setUserId(this.getOperatorId());
        operateLogReq.setUsername(this.getOperName());
        operateLogReq.setOperUrl(this.getOperUrl());
        operateLogReq.setOperIp(this.getOperIp());
        operateLogReq.setOperParam(this.getOperParam());
        operateLogReq.setJsonResult(this.getJsonResult());
        operateLogReq.setOperLocation(this.getOperLocation());
        operateLogReq.setSuccess(this.isSuccess());
        operateLogReq.setErrorMsg(this.getErrorMsg());
        operateLogReq.setOperTime(this.getOperTime());
        return operateLogReq;
    }

    public OperateLogReq toStaffOperateLogReq() {
        OperateLogReq operateLogReq = new OperateLogReq();
        operateLogReq.setTitle(this.getTitle());
        operateLogReq.setSubTitle(this.getSubTitle());
        operateLogReq.setOperType(this.getOperType());
        operateLogReq.setMethod(this.getMethod());
        operateLogReq.setRequestMethod(this.getRequestMethod());
        operateLogReq.setStaffId(this.getOperatorId());
        operateLogReq.setUsername(this.getOperName());
        operateLogReq.setOperUrl(this.getOperUrl());
        operateLogReq.setOperIp(this.getOperIp());
        operateLogReq.setOperParam(this.getOperParam());
        operateLogReq.setJsonResult(this.getJsonResult());
        operateLogReq.setOperLocation(this.getOperLocation());
        operateLogReq.setSuccess(this.isSuccess());
        operateLogReq.setErrorMsg(this.getErrorMsg());
        operateLogReq.setOperTime(this.getOperTime());
        operateLogReq.setTenantId(this.getTenantId());
        return operateLogReq;
    }

}
