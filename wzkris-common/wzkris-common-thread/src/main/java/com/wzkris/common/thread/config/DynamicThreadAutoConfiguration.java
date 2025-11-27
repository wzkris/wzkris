package com.wzkris.common.thread.config;

import com.wzkris.common.thread.properties.TpProperties;
import com.wzkris.common.thread.refresh.NacosTpRefresher;
import com.wzkris.common.thread.refresh.adapter.TomcatTpAdapter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@Import({NacosTpRefresher.class, TomcatTpAdapter.class})
@EnableConfigurationProperties(TpProperties.class)
@AutoConfiguration
public class DynamicThreadAutoConfiguration {

}
