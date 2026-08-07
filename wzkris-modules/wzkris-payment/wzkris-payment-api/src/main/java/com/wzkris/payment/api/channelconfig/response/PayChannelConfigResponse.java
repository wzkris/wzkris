package com.wzkris.payment.api.channelconfig.response;

import com.wzkris.payment.enums.channel.ChannelStatusEnum;
import com.wzkris.payment.enums.channel.PayChannelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 渠道配置响应（密钥字段不回显）
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class PayChannelConfigResponse {

    private Long id;

    private PayChannelEnum channel;

    @Schema(description = "配置名称")
    private String name;

    private String appId;

    private String mchId;

    private String subAppId;

    private String subMchId;

    @Schema(description = "商户证书序列号")
    private String certSerialNo;

    @Schema(description = "异步回调地址")
    private String notifyUrl;

    @Schema(description = "退款异步回调地址")
    private String refundNotifyUrl;

    @Schema(description = "支持的支付方式")
    private String payModes;

    @Schema(description = "状态")
    private ChannelStatusEnum status;

    @Schema(description = "备注")
    private String remark;
}
