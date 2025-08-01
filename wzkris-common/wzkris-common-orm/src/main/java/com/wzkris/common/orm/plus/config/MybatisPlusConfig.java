package com.wzkris.common.orm.plus.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.wzkris.common.orm.plus.extension.ExtenseSqlInjector;
import com.wzkris.common.orm.plus.handler.BaseFieldFillHandler;
import com.wzkris.common.orm.plus.interceptor.DataPermissionHandler;
import com.wzkris.common.orm.plus.interceptor.PageInterceptor;
import com.wzkris.common.orm.plus.interceptor.TenantLineHandlerImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 配置类
 * @date : 2024/1/11 14:54
 */
// 指定要扫描的Mapper类的包的路径
@MapperScan("com.wzkris.**.mapper")
@Configuration
public class MybatisPlusConfig {

    private final TenantProperties tenantProperties;

    public MybatisPlusConfig(TenantProperties tenantProperties) {
        this.tenantProperties = tenantProperties;
    }

    /**
     * MP插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 数据权限处理
        interceptor.addInnerInterceptor(new DataPermissionInterceptor(new DataPermissionHandler()));
        // 多租户
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandlerImpl(tenantProperties)));
        // 自定义分页插件
        PageInterceptor pageInterceptor = new PageInterceptor();
        pageInterceptor.setMaxLimit(500L); // 单页限制条数
        pageInterceptor.setOverflow(true); // 分页溢出
        pageInterceptor.setOptimizeJoin(false); // 不优化join
        interceptor.addInnerInterceptor(pageInterceptor);
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
        return new BaseFieldFillHandler();
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
