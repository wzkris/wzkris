package com.wzkris.payment.provider.wxpay;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.wzkris.common.core.exception.service.BusinessException;
import com.wzkris.payment.domain.PayChannelConfigDO;
import com.wzkris.payment.event.PayChannelConfigChangedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 微信支付 WxPayService 工厂：按 configId 缓存，配置变更时 evict。
 *
 * <p>支付为多商户、配置存 DB（pay_channel_config），不能用单例 Bean。
 * v3 平台证书由 WxPayConfig 的 apiV3Key 懒加载自动下载/轮转（getVerifier 触发 initApiV3HttpClient）。
 *
 * @author wzkris
 */
@Component
public class WxPayServiceFactory {

    private final ConcurrentHashMap<Long, WxPayService> cache = new ConcurrentHashMap<>();

    public WxPayService get(PayChannelConfigDO config) {
        return cache.computeIfAbsent(config.getId(), k -> build(config));
    }

    private WxPayService build(PayChannelConfigDO config) {
        if (config.getAppId() == null
                || config.getMchId() == null
                || config.getApiKey() == null
                || config.getPrivateKey() == null
                || config.getCertSerialNo() == null) {
            throw new BusinessException(99902, "微信支付配置不完整(appId/mchId/apiKey/privateKey/certSerialNo)");
        }
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(config.getAppId());
        payConfig.setMchId(config.getMchId());
        if (config.getSubAppId() != null) {
            payConfig.setSubAppId(config.getSubAppId());
        }
        if (config.getSubMchId() != null) {
            payConfig.setSubMchId(config.getSubMchId());
        }
        // apiKey 存放 APIv3 密钥；privateKey 存放商户私钥 PEM；certSerialNo 商户证书序列号
        payConfig.setApiV3Key(config.getApiKey());
        payConfig.setPrivateKeyString(config.getPrivateKey());
        payConfig.setCertSerialNo(config.getCertSerialNo());
        WxPayService service = new WxPayServiceImpl();
        service.setConfig(payConfig);
        return service;
    }

    /**
     * 配置变更/删除后清除缓存，避免使用旧证书
     */
    public void evict(Long configId) {
        if (configId != null) {
            cache.remove(configId);
        }
    }

    @EventListener
    public void onConfigChanged(PayChannelConfigChangedEvent event) {
        evict(event.getConfigId());
    }

}
