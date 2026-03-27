package com.wzkris.gateway.domain;

/**
 * 统计键
 */
@lombok.Data
@lombok.Builder
public class StatisticsKey {

    private String authType;

    private Long userId;

    private String path;

    private String date;

    private String hour;

}
