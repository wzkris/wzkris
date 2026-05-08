package com.wzkris.usercenter.request.member;

import com.wzkris.common.validator.annotation.PhoneNumber;
import com.wzkris.common.validator.annotation.Xss;
import com.wzkris.usercenter.enums.member.MemberStatusEnum;
import com.wzkris.usercenter.enums.user.GenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "修改租户成员参数体")
public class MemberMngUpdateRequest {

    @NotNull(message = "{invalidParameter.id.invalid}")
    private Long memberId;

    @Pattern(regexp = "^[a-z0-9_]+$", message = "{invalidParameter.username.invalid}")// 用户名只能为小写英文、数字和下划线
    @Xss
    @Size(min = 6, max = 30, message = "{invalidParameter.username.invalid}")
    @Schema(description = "用户名")
    private String username;

    @PhoneNumber
    @Schema(description = "手机号码")
    private String phoneNumber;

    @Schema(description = "状态")
    private MemberStatusEnum status;

    @Schema(description = "性别")
    private GenderEnum gender;

    @Schema(description = "用户额外信息")
    private String remark;

    @Schema(description = "职位组")
    private List<Long> postIds;

}

