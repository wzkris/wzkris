package com.wzkris.message.feign.userlog.req;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志记录
 *
 * @author wzkris
 */
@Data
public class OperateLogReq implements Serializable {

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

}
