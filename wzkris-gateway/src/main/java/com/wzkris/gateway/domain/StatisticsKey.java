package com.wzkris.gateway.domain;

import com.wzkris.common.core.enums.AuthType;

/**
 * 统计键
 */
@lombok.Data
@lombok.Builder
public class StatisticsKey {

    private AuthType authType;

    private Long userId;

    private String path;

    private String date;

    private String hour;

}
