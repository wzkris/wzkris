package com.wzkris.payment.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.payment.enums.channel.ChannelStatusEnum;
import com.wzkris.payment.enums.channel.PayChannelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 渠道商户配置
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@TableName(schema = "biz", value = "pay_channel_config")
public class PayChannelConfigDO extends BaseEntity {

    @TableId
    private Long configId;

    @Schema(description = "支付渠道")
    private PayChannelEnum channel;

    @Schema(description = "配置名称(人工识别)")
    private String name;

    @Schema(description = "应用ID")
    private String appId;

    @Schema(description = "商户号")
    private String mchId;

    @Schema(description = "子应用ID")
    private String subAppId;

    @Schema(description = "子商户号")
    private String subMchId;

    @Schema(description = "API密钥(加密存储)")
    private String apiKey;

    @Schema(description = "商户私钥(加密存储)")
    private String privateKey;

    @Schema(description = "平台公钥/证书")
    private String publicCert;

    @Schema(description = "商户证书序列号(微信v3签名必需)")
    private String certSerialNo;

    @Schema(description = "异步回调地址")
    private String notifyUrl;

    @Schema(description = "支持的支付方式,逗号分隔")
    private String payModes;

    @Schema(description = "状态")
    private ChannelStatusEnum status;

    @Schema(description = "备注")
    private String remark;
}
