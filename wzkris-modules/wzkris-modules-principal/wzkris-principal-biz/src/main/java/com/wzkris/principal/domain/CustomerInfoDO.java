package com.wzkris.principal.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.common.validator.annotation.Xss;
import com.wzkris.principal.httpservice.customer.resp.CustomerResp;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 顾客信息
 *
 * @author wzkris
 */
@Data
@AutoMappers({@AutoMapper(target = CustomerResp.class)})
@NoArgsConstructor
@TableName(schema = "biz", value = "customer_info")
public class CustomerInfoDO extends BaseEntity {

    @TableId
    private Long customerId;

    @Xss
    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "手机号码")
    private String phoneNumber;

    @Schema(description = "用户状态")
    private String status;

    @Schema(description = "用户性别")
    private String gender;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "最近登录ip")
    private String loginIp;

    @Schema(description = "最近登录日期")
    private Date loginDate;

    public CustomerInfoDO(Long customerId) {
        this.customerId = customerId;
    }

}
