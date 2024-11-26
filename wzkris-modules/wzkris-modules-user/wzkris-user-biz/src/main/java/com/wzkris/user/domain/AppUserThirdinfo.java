package com.wzkris.user.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 顾客第三方信息
 *
 * @author wzkris
 */
@Data
public class AppUserThirdinfo {

    @TableId
    private Long userId;

    private String openid;

    private String appId;

    @Schema(description = "渠道")
    private String channel;

    @Getter
    @AllArgsConstructor
    public enum Channel {

        WX_XCX("wx_xcx"),

        WX_GZH("wx_gzh");

        private final String value;
    }
}
