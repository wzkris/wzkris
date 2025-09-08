package com.wzkris.common.thread.config;

import com.wzkris.common.thread.properties.TpProperties;
import com.wzkris.common.thread.refresh.NacosTpRefresher;
import com.wzkris.common.thread.refresh.adapter.TomcatTpAdapter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@Import({TpProperties.class, NacosTpRefresher.class, TomcatTpAdapter.class})
@AutoConfiguration
public class DynamicThreadAutoConfiguration {

}
