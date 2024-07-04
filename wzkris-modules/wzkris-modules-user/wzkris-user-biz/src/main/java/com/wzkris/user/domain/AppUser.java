package com.wzkris.user.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wzkris.common.core.annotation.PhoneNumber;
import com.wzkris.common.core.annotation.Xss;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.common.security.oauth2.domain.model.LoginApper;
import com.wzkris.user.api.domain.dto.CustomerDTO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * 顾客 app_user
 *
 * @author wzkris
 */
@Data
@Accessors(chain = true)
@AutoMappers({
        @AutoMapper(target = LoginApper.class),
        @AutoMapper(target = CustomerDTO.class)
})
@NoArgsConstructor
@FieldNameConstants
public class AppUser extends BaseEntity {

    @TableId
    private Long userId;

    @Xss
    @Schema(description = "用户昵称")
    private String nickname;

    @PhoneNumber
    @Schema(description = "手机号码")
    private String phoneNumber;

    @Schema(description = "用户状态")
    private String status;

    @Schema(description = "用户性别")
    private String gender;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "删除标志")
    private Boolean isDeleted;

    @Schema(description = "最近登录ip")
    private String loginIp;

    @Schema(description = "最近登录日期")
    private Long loginDate;

    @Schema(description = "用户额外信息")
    private String remark;

    @JsonIgnore
    @TableField(exist = false)
    @Schema(description = "请求参数")
    private Map<String, Object> params;

    public AppUser(Long userId) {
        this.userId = userId;
    }

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>(2);
        }
        return params;
    }

}
