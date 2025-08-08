package com.wzkris.common.web.utils;

import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 * ua解析
 *
 * @author wzkris
 */
public class UserAgentUtil implements ApplicationRunner {

    public static final UserAgentAnalyzer INSTANCE = UserAgentAnalyzer
            .newBuilder()
            .hideMatcherLoadStats()
            .withCache(10000)
            .build();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        INSTANCE.parse("");// 提前加载
    }

}
