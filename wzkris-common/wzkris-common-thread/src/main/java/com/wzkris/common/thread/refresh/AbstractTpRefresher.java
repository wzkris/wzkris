/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wzkris.common.thread.refresh;

import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.thread.properties.ExecutorProperties;
import com.wzkris.common.thread.properties.TpProperties;
import com.wzkris.common.thread.refresh.adapter.WebServerTpAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Abstract class for refreshing properties in a Spring environment.
 *
 * @author wzkris
 * @date 2025/8/8
 */
@Slf4j
public abstract class AbstractTpRefresher {

    protected final TpProperties tpProperties;

    private final WebServerTpAdapter webServerTpRefresher;

    protected AbstractTpRefresher(TpProperties tpProperties, WebServerTpAdapter webServerTpAdapter) {
        this.tpProperties = tpProperties;
        this.webServerTpRefresher = webServerTpAdapter;
    }

    public final void refreshTp() {
        if (tpProperties.getTpExecutors() == null) {
            return;
        }
        for (ExecutorProperties tpExecutor : tpProperties.getTpExecutors()) {
            try {
                Object bean = SpringUtil.getContext().getBean(tpExecutor.getThreadPoolName());
                ThreadPoolExecutor executor = (ThreadPoolExecutor) bean;
                executor.setCorePoolSize(tpExecutor.getCorePoolSize());
                executor.setMaximumPoolSize(tpExecutor.getMaximumPoolSize());
                executor.setKeepAliveTime(tpExecutor.getKeepAliveTime(), tpExecutor.getUnit());
                log.info("更新线程池'{}'配置：{}", tpExecutor.getThreadPoolName(), tpExecutor);
            } catch (BeansException e) {
                log.error("无此名称‘{}’的线程池", tpExecutor.getThreadPoolName());
            }
        }
        webServerTpRefresher.refreshWeb(tpProperties.getTomcatExecutor());
    }

}
