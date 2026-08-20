package com.wzkris.usercenter.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.common.validator.annotation.Xss;
import com.wzkris.usercenter.enums.customer.CustomerStatusEnum;
import com.wzkris.usercenter.enums.user.GenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * 顾客信息
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@TableName(schema = "biz", value = "customer_info")
public class CustomerInfoDO extends BaseEntity {

    @Xss
    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "手机号码")
    private String phoneNumber;

    @Schema(description = "用户状态")
    private CustomerStatusEnum status;

    @Schema(description = "用户性别")
    private GenderEnum gender;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "最近登录ip")
    private String loginIp;

    @Schema(description = "最近登录日期")
    private OffsetDateTime loginDate;

    public CustomerInfoDO(Long id) {
        this.setId(id);
    }

}
