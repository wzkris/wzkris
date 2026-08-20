package com.wzkris.common.orm.plus.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.wzkris.common.core.support.UserContextHelper;
import com.wzkris.common.orm.plus.extension.ExtenseSqlInjector;
import com.wzkris.common.orm.plus.handler.BaseFieldFillHandler;
import com.wzkris.common.orm.plus.interceptor.DataPermissionHandler;
import com.wzkris.common.orm.plus.interceptor.TenantLineHandlerImpl;
import com.wzkris.common.orm.rule.DataPermissionRule;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 配置类
 * @date : 2024/1/11 14:54
 */
@AutoConfiguration
public class MybatisPlusConfig {

    private final TenantProperties tenantProperties;

    private final List<DataPermissionRule> dataPermissionRules;

    private final UserContextHelper userContextHelper;

    public MybatisPlusConfig(TenantProperties tenantProperties,
                             List<DataPermissionRule> dataPermissionRules,
                             UserContextHelper userContextHelper) {
        this.tenantProperties = tenantProperties;
        this.dataPermissionRules = dataPermissionRules;
        this.userContextHelper = userContextHelper;
    }

    /**
     * MP插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 数据权限处理
        interceptor.addInnerInterceptor(new DataPermissionInterceptor(
                new DataPermissionHandler(dataPermissionRules, userContextHelper)));
        // 多租户
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(
                new TenantLineHandlerImpl(tenantProperties, userContextHelper)));
        // 分页插件
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        paginationInterceptor.setMaxLimit(500L); // 单页限制条数
        paginationInterceptor.setOverflow(true); // 分页溢出
        paginationInterceptor.setOptimizeJoin(false); // 不优化join
        interceptor.addInnerInterceptor(paginationInterceptor);
        return interceptor;
    }

    /**
     * 扩展SQL方法
     */
    @Bean
    public ExtenseSqlInjector sqlInjector() {
        return new ExtenseSqlInjector();
    }

    /**
     * 元对象字段填充控制器
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new BaseFieldFillHandler(userContextHelper);
    }

    /**
     * 使用网卡信息绑定雪花生成器
     * 防止集群雪花ID重复
     */
    @Bean
    public IdentifierGenerator idGenerator() throws UnknownHostException {
        return new DefaultIdentifierGenerator(InetAddress.getLocalHost());
    }

}
