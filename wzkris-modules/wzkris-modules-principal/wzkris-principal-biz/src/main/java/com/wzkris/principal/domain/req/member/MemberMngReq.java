package com.wzkris.principal.domain.req.member;

import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.validator.annotation.EnumsCheck;
import com.wzkris.common.validator.annotation.PhoneNumber;
import com.wzkris.common.validator.annotation.Xss;
import com.wzkris.common.validator.group.ValidationGroups;
import com.wzkris.principal.constant.UserConstants;
import com.wzkris.principal.domain.MemberInfoDO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@AutoMappers({@AutoMapper(target = MemberInfoDO.class)})
@Schema(description = "租户成员管理添加修改参数体")
public class MemberMngReq {

    private Long memberId;

    @Pattern(regexp = "^[a-z0-9_]+$", message = "{invalidParameter.username.invalid}")// 用户名只能为小写英文、数字和下划线
    @Xss
    @NotBlank(message = "{invalidParameter.username.invalid}", groups = ValidationGroups.Insert.class)
    @Size(min = 6, max = 30, message = "{invalidParameter.username.invalid}")
    @Schema(description = "用户名")
    private String username;

    @PhoneNumber
    @Schema(description = "手机号码")
    private String phoneNumber;

    @EnumsCheck(values = {CommonConstants.STATUS_DISABLE, CommonConstants.STATUS_ENABLE},
            message = "{invalidParameter.status.invalid}")
    @Schema(description = "状态")
    private String status;

    @EnumsCheck(values = {UserConstants.GENDER_MALE, UserConstants.GENDER_FEMALE, UserConstants.GENDER_UNKNOWN},
            message = "{invalidParameter.gender.invalid}")
    @Schema(description = "性别")
    private String gender;

    @Schema(description = "用户额外信息")
    private String remark;

    @Schema(description = "职位组")
    private List<Long> postIds;

}
