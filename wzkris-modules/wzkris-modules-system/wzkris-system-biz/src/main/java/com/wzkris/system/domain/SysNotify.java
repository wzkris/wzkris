package com.wzkris.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.wzkris.common.core.annotation.Xss;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 系统通知公告表 SysNotify
 *
 * @author wzkris
 */
@Data
@Accessors(chain = true)
public class SysNotify extends BaseEntity {

    @TableId
    private Long notifyId;

    @Xss(message = "公告标题不能包含脚本字符")
    @NotBlank(message = "公告标题不能为空")
    @Size(min = 2, max = 50, message = "公告标题{validate.size.illegal}")
    @Schema(description = "标题")
    private String notifyTitle;

    @Schema(description = "公告类型（1通知 2公告）")
    private String notifyType;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "状态（0正常 1关闭）")
    private String status;

}
