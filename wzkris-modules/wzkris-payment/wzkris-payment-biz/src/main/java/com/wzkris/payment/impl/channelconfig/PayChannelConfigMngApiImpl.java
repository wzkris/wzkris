package com.wzkris.payment.impl.channelconfig;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.BeanCopierUtil;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.payment.api.channelconfig.PayChannelConfigMngApi;
import com.wzkris.payment.api.channelconfig.request.PayChannelConfigMngPageRequest;
import com.wzkris.payment.api.channelconfig.request.PayChannelConfigSaveRequest;
import com.wzkris.payment.api.channelconfig.request.PayChannelConfigUpdateRequest;
import com.wzkris.payment.api.channelconfig.response.PayChannelConfigResponse;
import com.wzkris.payment.domain.PayChannelConfigDO;
import com.wzkris.payment.enums.channel.ChannelStatusEnum;
import com.wzkris.payment.event.PayChannelConfigChangedEvent;
import com.wzkris.payment.service.PayChannelConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * 渠道配置管理
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class PayChannelConfigMngApiImpl extends AbstractApi implements PayChannelConfigMngApi {

    private final PayChannelConfigService configService;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Result<Page<PayChannelConfigResponse>> queryPage(PayChannelConfigMngPageRequest request) {
        IPage<PayChannelConfigDO> page = configService.page(request.buildPage(), buildQueryWrapper(request));
        return ok(Page.of(page, BeanCopierUtil.copyList(page.getRecords(), PayChannelConfigResponse.class)));
    }

    private LambdaQueryWrapper<PayChannelConfigDO> buildQueryWrapper(PayChannelConfigMngPageRequest request) {
        return new LambdaQueryWrapper<PayChannelConfigDO>()
                .eq(request.getChannel() != null, PayChannelConfigDO::getChannel, request.getChannel())
                .eq(request.getStatus() != null, PayChannelConfigDO::getStatus, request.getStatus())
                .orderByDesc(PayChannelConfigDO::getConfigId);
    }

    @Override
    public Result<PayChannelConfigResponse> queryInfo(IdRequest request) {
        return ok(BeanCopierUtil.copy(configService.getById(request.getId()), PayChannelConfigResponse.class));
    }

    @Override
    public Result<Void> save(PayChannelConfigSaveRequest request) {
        PayChannelConfigDO config = BeanCopierUtil.copy(request, PayChannelConfigDO.class);
        config.setChannel(request.getChannel());
        config.setStatus(ChannelStatusEnum.ENABLED);
        boolean ok = configService.save(config);
        if (ok) {
            eventPublisher.publishEvent(new PayChannelConfigChangedEvent(config.getConfigId()));
        }
        return toRes(ok);
    }

    @Override
    public Result<Void> update(PayChannelConfigUpdateRequest request) {
        PayChannelConfigDO config = BeanCopierUtil.copy(request, PayChannelConfigDO.class);
        boolean ok = configService.updateById(config);
        if (ok) {
            eventPublisher.publishEvent(new PayChannelConfigChangedEvent(request.getConfigId()));
        }
        return toRes(ok);
    }

    @Override
    public Result<Void> remove(IdListRequest request) {
        boolean ok = configService.removeByIds(request.getIdList());
        if (ok) {
            for (Long configId : request.getIdList()) {
                eventPublisher.publishEvent(new PayChannelConfigChangedEvent(configId));
            }
        }
        return toRes(ok);
    }
}
