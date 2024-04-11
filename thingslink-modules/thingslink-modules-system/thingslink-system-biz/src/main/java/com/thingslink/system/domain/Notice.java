package com.thingslink.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.thingslink.common.orm.model.BaseEntity;
import com.thingslink.common.core.validate.Xss;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 通知公告表 notice
 *
 * @author wzkris
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Notice extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 公告ID
     */
    @TableId
    private Long noticeId;

    /**
     * 公告标题
     */
    @Xss(message = "公告标题不能包含脚本字符")
    @NotBlank(message = "公告标题不能为空")
    @Size(min = 0, max = 50, message = "公告标题不能超过50个字符")
    private String noticeTitle;

    /**
     * 公告类型（1通知 2公告）
     */
    private String noticeType;

    /**
     * 公告内容
     */
    private String noticeContent;

    /**
     * 公告状态（0正常 1关闭）
     */
    private String status;

    private String messageId;

}
