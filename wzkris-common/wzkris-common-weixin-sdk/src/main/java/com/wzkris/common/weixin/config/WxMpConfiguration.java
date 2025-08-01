package com.wzkris.common.weixin.config;

import com.wzkris.common.redis.util.RedisUtil;
import com.wzkris.common.weixin.properties.WxMpProperties;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxRuntimeException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import me.chanjar.weixin.mp.config.impl.WxMpRedissonConfigImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * wechat mp configuration
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Slf4j
@ConditionalOnProperty(value = "weixin.mp.enable")
public class WxMpConfiguration {

    private final WxMpProperties properties;

    public WxMpConfiguration(WxMpProperties properties) {
        this.properties = properties;
    }

    @Bean
    public WxMpService wxMpService() {
        // 代码里 getConfigs()处报错的同学，请注意仔细阅读项目说明，你的IDE需要引入lombok插件！！！！
        final List<WxMpProperties.MpConfig> configs = this.properties.getConfigs();
        if (configs == null) {
            throw new WxRuntimeException("大哥，拜托先看下项目首页的说明（readme文件），添加下相关配置，注意别配错了！");
        }

        WxMpService service = new WxMpServiceImpl();
        service.setMultiConfigStorages(configs.stream()
                .map(a -> {
                    WxMpDefaultConfigImpl configStorage;
                    if (this.properties.isUseRedis()) {
                        configStorage = new WxMpRedissonConfigImpl(RedisUtil.getClient(), a.getAppId());
                    } else {
                        configStorage = new WxMpDefaultConfigImpl();
                    }

                    configStorage.setAppId(a.getAppId());
                    configStorage.setSecret(a.getSecret());
                    configStorage.setToken(a.getToken());
                    configStorage.setAesKey(a.getAesKey());
                    return configStorage;
                })
                .collect(Collectors.toMap(WxMpDefaultConfigImpl::getAppId, a -> a, (o, n) -> o)));
        return service;
    }

    /**
     *     @Bean
     *     public WxMpMessageRouter messageRouter(WxMpService wxMpService) {
     *         final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);
     *
     *         // 记录所有事件的日志 （异步执行）
     *         newRouter.rule().handler(this.logHandler).next();
     *
     *         // 接收客服会话管理事件
     *         newRouter.rule().async(false).msgType(EVENT).event(KF_CREATE_SESSION)
     *                 .handler(this.kfSessionHandler).end();
     *         newRouter.rule().async(false).msgType(EVENT).event(KF_CLOSE_SESSION)
     *                 .handler(this.kfSessionHandler).end();
     *         newRouter.rule().async(false).msgType(EVENT).event(KF_SWITCH_SESSION)
     *                 .handler(this.kfSessionHandler).end();
     *
     *         // 门店审核事件
     *         newRouter.rule().async(false).msgType(EVENT).event(POI_CHECK_NOTIFY).handler(this.storeCheckNotifyHandler).end();
     *
     *         // 自定义菜单事件
     *         newRouter.rule().async(false).msgType(EVENT).event(EventType.CLICK).handler(this.menuHandler).end();
     *
     *         // 点击菜单连接事件
     *         newRouter.rule().async(false).msgType(EVENT).event(EventType.VIEW).handler(this.nullHandler).end();
     *
     *         // 关注事件
     *         newRouter.rule().async(false).msgType(EVENT).event(SUBSCRIBE).handler(this.subscribeHandler).end();
     *
     *         // 取消关注事件
     *         newRouter.rule().async(false).msgType(EVENT).event(UNSUBSCRIBE).handler(this.unsubscribeHandler).end();
     *
     *         // 上报地理位置事件
     *         newRouter.rule().async(false).msgType(EVENT).event(EventType.LOCATION).handler(this.locationHandler).end();
     *
     *         // 接收地理位置消息
     *         newRouter.rule().async(false).msgType(XmlMsgType.LOCATION).handler(this.locationHandler).end();
     *
     *         // 扫码事件
     *         newRouter.rule().async(false).msgType(EVENT).event(EventType.SCAN).handler(this.scanHandler).end();
     *
     *         // 默认
     *         newRouter.rule().async(false).handler(this.msgHandler).end();
     *
     *         return newRouter;
     *     }
     */
}
