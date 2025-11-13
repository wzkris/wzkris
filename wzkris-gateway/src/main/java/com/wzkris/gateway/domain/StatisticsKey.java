package com.wzkris.gateway.domain;

import com.wzkris.common.core.enums.AuthTypeEnum;

/**
 * 统计键
 */
@lombok.Data
@lombok.Builder
public class StatisticsKey {

    private AuthTypeEnum authType;

    private Long userId;

    private String path;

    private String date;

    private String hour;

}
