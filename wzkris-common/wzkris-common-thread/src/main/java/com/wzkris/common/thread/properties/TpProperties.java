package com.wzkris.common.thread.properties;

import lombok.Data;

import java.util.List;

/**
 * 线程池配置
 *
 * @author wzkris
 * @date 2025/8/8
 */
@Data
public class TpProperties {

    private List<ExecutorProperties> tpExecutors;

}
