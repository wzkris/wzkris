package com.wzkris.common.weixin.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaRedissonConfigImpl;
import com.wzkris.common.weixin.properties.WxMaProperties;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxRuntimeException;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Slf4j
@EnableConfigurationProperties(WxMaProperties.class)
@ConditionalOnProperty(value = "weixin.miniapp.enable")
public class WxMaConfiguration {

    private final WxMaProperties properties;

    public WxMaConfiguration(WxMaProperties properties) {
        this.properties = properties;
    }

    @Bean
    public WxMaService wxMaService(RedissonClient redissonClient) {
        List<WxMaProperties.Config> configs = this.properties.getConfigs();
        if (configs == null) {
            throw new WxRuntimeException("大哥，拜托先看下项目首页的说明（readme文件），添加下相关配置，注意别配错了！");
        }
        WxMaService maService = new WxMaServiceImpl();
        maService.setMultiConfigs(configs.stream()
                .map(a -> {
                    WxMaDefaultConfigImpl config;
                    if (this.properties.isUseRedis()) {
                        config = new WxMaRedissonConfigImpl(redissonClient, a.getAppid());
                    } else {
                        config = new WxMaDefaultConfigImpl();
                    }

                    config.setAppid(a.getAppid());
                    config.setSecret(a.getSecret());
                    config.setToken(a.getToken());
                    config.setAesKey(a.getAesKey());
                    config.setMsgDataFormat(a.getMsgDataFormat());
                    return config;
                })
                .collect(Collectors.toMap(WxMaDefaultConfigImpl::getAppid, a -> a, (o, n) -> o)));
        return maService;
    }

    /**
     *  private final WxMaMessageHandler subscribeMsgHandler = (wxMessage, context, service, sessionManager) -> {
     *         service.getMsgService().sendSubscribeMsg(WxMaSubscribeMessage.builder()
     *                 .templateId("此处更换为自己的模板id")
     *                 .data(Lists.newArrayList(
     *                         new WxMaSubscribeMessage.MsgData("keyword1", "339208499")))
     *                 .toUser(wxMessage.getFromUser())
     *                 .build());
     *         return null;
     *     };
     *     private final WxMaMessageHandler logHandler = (wxMessage, context, service, sessionManager) -> {
     *         log.info("收到消息：" + wxMessage.toString());
     *         service.getMsgService().sendKefuMsg(WxMaKefuMessage.newTextBuilder().content("收到信息为：" + wxMessage.toJson())
     *                 .toUser(wxMessage.getFromUser()).build());
     *         return null;
     *     };
     *     private final WxMaMessageHandler textHandler = (wxMessage, context, service, sessionManager) -> {
     *         service.getMsgService().sendKefuMsg(WxMaKefuMessage.newTextBuilder().content("回复文本消息")
     *                 .toUser(wxMessage.getFromUser()).build());
     *         return null;
     *     };
     *     private final WxMaMessageHandler picHandler = (wxMessage, context, service, sessionManager) -> {
     *         try {
     *             WxMediaUploadResult uploadResult = service.getMediaService()
     *                     .uploadMedia("image", "png",
     *                             ClassLoader.getSystemResourceAsStream("tmp.png"));
     *             service.getMsgService().sendKefuMsg(
     *                     WxMaKefuMessage
     *                             .newImageBuilder()
     *                             .mediaId(uploadResult.getMediaId())
     *                             .toUser(wxMessage.getFromUser())
     *                             .build());
     *         }
     *         catch (WxErrorException e) {
     *             e.printStackTrace();
     *         }
     *
     *         return null;
     *     };
     *     private final WxMaMessageHandler qrcodeHandler = (wxMessage, context, service, sessionManager) -> {
     *         try {
     *             final File file = service.getQrcodeService().createQrcode("123", 430);
     *             WxMediaUploadResult uploadResult = service.getMediaService().uploadMedia("image", file);
     *             service.getMsgService().sendKefuMsg(
     *                     WxMaKefuMessage
     *                             .newImageBuilder()
     *                             .mediaId(uploadResult.getMediaId())
     *                             .toUser(wxMessage.getFromUser())
     *                             .build());
     *         }
     *         catch (WxErrorException e) {
     *             e.printStackTrace();
     *         }
     *
     *         return null;
     *     };
     *
     *     @Bean
     *     public WxMaMessageRouter wxMaMessageRouter(WxMaService wxMaService) {
     *         final WxMaMessageRouter router = new WxMaMessageRouter(wxMaService);
     *         router
     *                 .rule().handler(logHandler).next()
     *                 .rule().async(false).content("订阅消息").handler(subscribeMsgHandler).end()
     *                 .rule().async(false).content("文本").handler(textHandler).end()
     *                 .rule().async(false).content("图片").handler(picHandler).end()
     *                 .rule().async(false).content("二维码").handler(qrcodeHandler).end();
     *         return router;
     *     }
     */
}
