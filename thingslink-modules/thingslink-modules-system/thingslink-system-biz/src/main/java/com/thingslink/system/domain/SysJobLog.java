package com.thingslink.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 定时任务调度日志表 sys_job_log
 *
 * @author wzkris
 */
@Data
@Accessors(chain = true)
public class SysJobLog implements Serializable {
    

    /**
     * ID
     */
    @TableId
    private Long jobLogId;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务组名
     */
    private String jobGroup;

    /**
     * 调用目标字符串
     */
    private String invokeTarget;

    /**
     * 日志信息
     */
    private String jobMessage;

    /**
     * 执行状态（0正常 1失败）
     */
    private String status;

    /**
     * 异常信息
     */
    private String exceptionInfo;

    /**
     * 创建时间
     */
    private LocalDateTime createAt;

    /**
     * 请求参数
     */
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
