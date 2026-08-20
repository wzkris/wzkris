package com.wzkris.payment.api.channelconfig;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.payment.api.channelconfig.request.PayChannelConfigMngPageRequest;
import com.wzkris.payment.api.channelconfig.request.PayChannelConfigSaveRequest;
import com.wzkris.payment.api.channelconfig.request.PayChannelConfigUpdateRequest;
import com.wzkris.payment.api.channelconfig.response.PayChannelConfigResponse;

/**
 * 渠道配置管理接口（后台）
 *
 * @author wzkris
 */
public interface PayChannelConfigMngApi {

    Result<Page<PayChannelConfigResponse>> queryPage(PayChannelConfigMngPageRequest request);

    Result<PayChannelConfigResponse> queryById(IdRequest request);

    Result<Void> save(PayChannelConfigSaveRequest request);

    Result<Void> update(PayChannelConfigUpdateRequest request);

    Result<Void> remove(IdListRequest request);

}
