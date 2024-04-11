package com.thingslink.system.mapper;


import com.thingslink.common.orm.plus.BaseMapperPlus;
import com.thingslink.system.domain.Job;

import java.util.List;

/**
 * 调度任务信息 数据层
 *
 * @author wzkris
 */
public interface JobMapper extends BaseMapperPlus<Job> {
    /**
     * 查询调度任务日志集合
     *
     * @param job 调度信息
     * @return 操作日志集合
     */
    List<Job> list(Job job);

}
