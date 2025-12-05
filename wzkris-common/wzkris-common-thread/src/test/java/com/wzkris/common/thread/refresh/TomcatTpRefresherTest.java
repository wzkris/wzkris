package com.wzkris.common.thread.refresh;

import com.wzkris.common.thread.adapter.WebServerTpAdapter;
import com.wzkris.common.thread.properties.TpProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * TomcatTpRefresher 测试类
 *
 * @author wzkris
 */
@DisplayName("Tomcat线程池刷新器测试")
class TomcatTpRefresherTest {

    private TomcatTpRefresher refresher;

    @Mock
    private TpProperties tpProperties;

    @Mock
    private WebServerTpAdapter webServerTpAdapter;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        refresher = new TomcatTpRefresher(tpProperties, webServerTpAdapter);
    }

    @Test
    @DisplayName("测试刷新 - Tomcat配置为空")
    void testRefresh_EmptyTomcatProperties() {
        TpProperties.TomcatThreadProperties emptyProps = new TpProperties.TomcatThreadProperties();
        when(tpProperties.getTomcatExecutor()).thenReturn(emptyProps);

        refresher.refresh();

        verify(webServerTpAdapter, never()).refreshWeb(any());
    }

    @Test
    @DisplayName("测试刷新 - Tomcat配置为null")
    void testRefresh_NullTomcatProperties() {
        when(tpProperties.getTomcatExecutor()).thenReturn(null);

        refresher.refresh();

        verify(webServerTpAdapter, never()).refreshWeb(any());
    }

    @Test
    @DisplayName("测试刷新 - 首次刷新")
    void testRefresh_FirstTime() {
        TpProperties.TomcatThreadProperties props = createTomcatProperties();
        when(tpProperties.getTomcatExecutor()).thenReturn(props);

        refresher.refresh();

        verify(webServerTpAdapter, times(1)).refreshWeb(props);
    }

    @Test
    @DisplayName("测试刷新 - 配置有变化")
    void testRefresh_WithChanges() {
        TpProperties.TomcatThreadProperties props1 = createTomcatProperties();
        props1.setMaxThreads(200);
        when(tpProperties.getTomcatExecutor()).thenReturn(props1);

        // 第一次刷新
        refresher.refresh();
        verify(webServerTpAdapter, times(1)).refreshWeb(props1);

        // 配置变化
        TpProperties.TomcatThreadProperties props2 = createTomcatProperties();
        props2.setMaxThreads(300);
        when(tpProperties.getTomcatExecutor()).thenReturn(props2);

        // 第二次刷新
        refresher.refresh();
        verify(webServerTpAdapter, times(1)).refreshWeb(props2);
    }

    @Test
    @DisplayName("测试刷新 - 配置无变化")
    void testRefresh_NoChanges() {
        TpProperties.TomcatThreadProperties props = createTomcatProperties();
        when(tpProperties.getTomcatExecutor()).thenReturn(props);

        // 第一次刷新
        refresher.refresh();
        verify(webServerTpAdapter, times(1)).refreshWeb(props);

        // 第二次刷新 - 配置相同
        refresher.refresh();
        // 应该只调用一次，因为配置没有变化
        verify(webServerTpAdapter, times(1)).refreshWeb(props);
    }

    @Test
    @DisplayName("测试刷新 - 部分属性变化")
    void testRefresh_PartialChanges() {
        TpProperties.TomcatThreadProperties props1 = createTomcatProperties();
        props1.setMaxThreads(200);
        props1.setMinSpareThreads(10);
        when(tpProperties.getTomcatExecutor()).thenReturn(props1);

        refresher.refresh();
        verify(webServerTpAdapter, times(1)).refreshWeb(props1);

        // 只改变一个属性
        TpProperties.TomcatThreadProperties props2 = createTomcatProperties();
        props2.setMaxThreads(200);
        props2.setMinSpareThreads(20); // 改变这个
        when(tpProperties.getTomcatExecutor()).thenReturn(props2);

        refresher.refresh();
        verify(webServerTpAdapter, times(1)).refreshWeb(props2);
    }

    @Test
    @DisplayName("测试刷新 - 所有属性变化")
    void testRefresh_AllPropertiesChanged() {
        TpProperties.TomcatThreadProperties props1 = createTomcatProperties();
        when(tpProperties.getTomcatExecutor()).thenReturn(props1);

        refresher.refresh();
        verify(webServerTpAdapter, times(1)).refreshWeb(props1);

        TpProperties.TomcatThreadProperties props2 = new TpProperties.TomcatThreadProperties();
        props2.setMaxThreads(300);
        props2.setMinSpareThreads(20);
        props2.setMaxConnections(2000);
        props2.setAcceptCount(200);
        props2.setKeepAliveTimeout(120000);
        when(tpProperties.getTomcatExecutor()).thenReturn(props2);

        refresher.refresh();
        verify(webServerTpAdapter, times(1)).refreshWeb(props2);
    }

    @Test
    @DisplayName("测试刷新 - 边界值")
    void testRefresh_BoundaryValues() {
        TpProperties.TomcatThreadProperties props = new TpProperties.TomcatThreadProperties();
        props.setMaxThreads(0);
        props.setMinSpareThreads(0);
        props.setMaxConnections(0);
        props.setAcceptCount(0);
        props.setKeepAliveTimeout(0);
        when(tpProperties.getTomcatExecutor()).thenReturn(props);

        refresher.refresh();

        verify(webServerTpAdapter, times(1)).refreshWeb(props);
    }

    // 辅助方法
    private TpProperties.TomcatThreadProperties createTomcatProperties() {
        TpProperties.TomcatThreadProperties props = new TpProperties.TomcatThreadProperties();
        props.setMaxThreads(200);
        props.setMinSpareThreads(10);
        props.setMaxConnections(1000);
        props.setAcceptCount(100);
        props.setKeepAliveTimeout(60000);
        return props;
    }
}

