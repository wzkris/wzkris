package com.wzkris.common.thread.config;

import com.wzkris.common.thread.adapter.TomcatTpAdapter;
import com.wzkris.common.thread.adapter.WebServerTpAdapter;
import com.wzkris.common.thread.properties.TpProperties;
import com.wzkris.common.thread.refresh.BusinessTpRefresher;
import com.wzkris.common.thread.refresh.NacosTpRefresher;
import com.wzkris.common.thread.refresh.TomcatTpRefresher;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * 动态线程池自动配置
 *
 * @author wzkris
 * @date 2025/12/02
 */
@Import({NacosTpRefresher.class})
@EnableConfigurationProperties(TpProperties.class)
@AutoConfiguration
public class DynamicThreadAutoConfiguration {

    /**
     * 创建 Tomcat 线程池适配器
     */
    @Bean
    @ConditionalOnClass(TomcatWebServer.class)
    @ConditionalOnMissingBean
    public TomcatTpAdapter tomcatTpAdapter(ServletWebServerApplicationContext servletWebServerApplicationContext) {
        return new TomcatTpAdapter(servletWebServerApplicationContext);
    }

    /**
     * 创建 Tomcat 线程池刷新器
     */
    @Bean
    @ConditionalOnBean(WebServerTpAdapter.class)
    @ConditionalOnMissingBean
    public TomcatTpRefresher tomcatTpRefresher(TpProperties tpProperties, WebServerTpAdapter webServerTpAdapter) {
        return new TomcatTpRefresher(tpProperties, webServerTpAdapter);
    }

    /**
     * 创建业务线程池刷新器
     */
    @Bean
    @ConditionalOnMissingBean
    public BusinessTpRefresher businessTpRefresher(TpProperties tpProperties) {
        return new BusinessTpRefresher(tpProperties.getExecutors());
    }

}
