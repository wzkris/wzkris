package com.wzkris.payment.api.channelconfig.request;

import com.wzkris.payment.enums.channel.ChannelStatusEnum;
import com.wzkris.payment.enums.channel.PayChannelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 修改渠道配置参数体
 *
 * @author wzkris
 */
@Data
@Schema(description = "修改渠道配置参数体")
public class PayChannelConfigUpdateRequest {

    @NotNull(message = "配置ID不能为空")
    private Long id;

    @Schema(description = "支付渠道")
    private PayChannelEnum channel;

    @Schema(description = "配置名称(人工识别)")
    private String name;

    @Schema(description = "应用ID")
    private String appId;

    @Schema(description = "商户号")
    private String mchId;

    @Schema(description = "子应用ID(服务商模式)")
    private String subAppId;

    @Schema(description = "子商户号(服务商模式)")
    private String subMchId;

    @Schema(description = "API密钥(微信APIv3密钥apiV3Key)")
    private String apiKey;

    @Schema(description = "商户私钥PEM(微信apiclient_key.pem)")
    private String privateKey;

    @Schema(description = "平台证书/公钥(微信v3可留空)")
    private String publicCert;

    @Schema(description = "商户证书序列号(微信v3签名必需)")
    private String certSerialNo;

    @Schema(description = "异步回调地址")
    private String notifyUrl;

    @Schema(description = "退款异步回调地址")
    private String refundNotifyUrl;

    @Schema(description = "支持的支付方式,逗号分隔")
    private String payModes;

    @Schema(description = "状态")
    private ChannelStatusEnum status;

    @Schema(description = "备注")
    private String remark;
}
