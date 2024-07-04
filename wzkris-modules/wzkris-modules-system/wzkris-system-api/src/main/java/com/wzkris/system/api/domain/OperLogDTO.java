package com.wzkris.system.api.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 操作日志记录表 oper_log
 *
 * @author wzkris
 */
@Data
@Accessors(chain = true)
public class OperLogDTO implements Serializable {

    /**
     * 日志主键
     */
    private Long operId;
    /**
     * 操作模块
     */
    private String title;
    /**
     * 操作类型（0其它 1新增 2修改 3删除）
     */
    private String operType;
    /**
     * 操作类型数组
     */
    private String[] operTypes;
    /**
     * 请求方法
     */
    private String method;
    /**
     * 请求方式
     */
    private String requestMethod;
    /**
     * 操作类别（0其它 1后台用户 2手机端用户）
     */
    private String operatorType;
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
     * 操作状态（0正常 1异常）
     */
    private String status;
    /**
     * 错误消息
     */
    private String errorMsg;
    /**
     * 操作时间
     */
    private Long operTime;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>(2);
        }
        return params;
    }
}