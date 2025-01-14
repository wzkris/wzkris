package com.wzkris.user.domain.req;

import com.wzkris.common.core.annotation.EnumsCheck;
import com.wzkris.common.core.annotation.PhoneNumber;
import com.wzkris.common.core.annotation.Xss;
import com.wzkris.common.core.annotation.group.ValidationGroups;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.user.constant.UserConstants;
import com.wzkris.user.domain.SysUser;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@AutoMappers({@AutoMapper(target = SysUser.class)})
@Schema(description = "系统用户添加修改参数体")
public class SysUserReq {

    private Long userId;

    @Schema(description = "部门ID")
    private Long deptId;

    @Pattern(regexp = "^[a-z0-9_]+$", message = "{desc.username}{validate.illegal}")// 用户名只能为小写英文、数字和下划线
    @Xss(message = "{desc.user}{desc.name}" + "{validate.xss.forbid}")
    @NotBlank(message = "{desc.user}" + "{validate.notnull}", groups = ValidationGroups.Insert.class)
    @Size(min = 6, max = 30, message = "{desc.user}" + "{validate.size.illegal}")
    @Schema(description = "用户名")
    private String username;

    @Xss(message = "{desc.user}{desc.name}" + "{validate.xss.forbid}")
    @Schema(description = "用户昵称")
    private String nickname;

    @Email(message = "{desc.user}" + "{validate.email.illegal}")
    @Schema(description = "用户邮箱")
    private String email;

    @PhoneNumber
    @Schema(description = "手机号码")
    private String phoneNumber;

    @EnumsCheck(values = {CommonConstants.STATUS_DISABLE, CommonConstants.STATUS_ENABLE})
    @Schema(description = "用户状态")
    private String status;

    @EnumsCheck(values = {UserConstants.GENDER_MALE, UserConstants.GENDER_FEMALE, UserConstants.GENDER_UNKNOWN}
            , message = "{desc.gender}{validate.enums.illegal}")
    @Schema(description = "用户性别")
    private String gender;

    @Schema(description = "用户额外信息")
    private String remark;

    @Schema(description = "角色组")
    private List<Long> roleIds;

    @Schema(description = "岗位组")
    private List<Long> postIds;
}
