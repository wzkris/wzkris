package com.wzkris.payment.api.channelconfig.request;

import com.wzkris.common.orm.request.PagingRequest;
import com.wzkris.payment.enums.channel.ChannelStatusEnum;
import com.wzkris.payment.enums.channel.PayChannelEnum;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 渠道配置分页查询参数体
 *
 * @author wzkris
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "渠道配置分页查询参数体")
public class PayChannelConfigMngPageRequest extends PagingRequest {

    @Parameter(description = "支付渠道")
    private PayChannelEnum channel;

    @Parameter(description = "状态")
    private ChannelStatusEnum status;
}
