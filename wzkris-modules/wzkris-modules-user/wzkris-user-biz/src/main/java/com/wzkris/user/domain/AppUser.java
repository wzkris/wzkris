package com.wzkris.user.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.core.annotation.Xss;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.user.rmi.domain.resp.AppUserResp;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 顾客 app_user
 *
 * @author wzkris
 */
@Data
@AutoMappers({@AutoMapper(target = AppUserResp.class)})
@NoArgsConstructor
@TableName(schema = "biz")
public class AppUser extends BaseEntity {

    @TableId
    private Long userId;

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

    public AppUser(Long userId) {
        this.userId = userId;
    }

}
