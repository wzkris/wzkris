package com.wzkris.common.log.event;

import com.wzkris.message.feign.userlog.req.UserOperateLogReq;
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
     * 操作人员ID
     */
    private Long userId;

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

    public UserOperateLogReq toOperateLogReq() {
        UserOperateLogReq userOperateLogReq = new UserOperateLogReq();
        userOperateLogReq.setTitle(this.getTitle());
        userOperateLogReq.setSubTitle(this.getSubTitle());
        userOperateLogReq.setOperType(this.getOperType());
        userOperateLogReq.setMethod(this.getMethod());
        userOperateLogReq.setRequestMethod(this.getRequestMethod());
        userOperateLogReq.setUserId(this.getUserId());
        userOperateLogReq.setOperName(this.getOperName());
        userOperateLogReq.setOperUrl(this.getOperUrl());
        userOperateLogReq.setOperIp(this.getOperIp());
        userOperateLogReq.setOperParam(this.getOperParam());
        userOperateLogReq.setJsonResult(this.getJsonResult());
        userOperateLogReq.setOperLocation(this.getOperLocation());
        userOperateLogReq.setSuccess(this.isSuccess());
        userOperateLogReq.setErrorMsg(this.getErrorMsg());
        userOperateLogReq.setOperTime(this.getOperTime());
        return userOperateLogReq;
    }

}
