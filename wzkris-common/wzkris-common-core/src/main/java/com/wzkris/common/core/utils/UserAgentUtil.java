package com.wzkris.common.core.utils;

import nl.basjes.parse.useragent.UserAgentAnalyzer;

/**
 * ua解析
 *
 * @author wzkris
 */
public class UserAgentUtil {

    public static final UserAgentAnalyzer INSTANCE = UserAgentAnalyzer
            .newBuilder()
            .hideMatcherLoadStats()
            .withCache(10000)
            .build();

}
