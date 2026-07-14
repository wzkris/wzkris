package com.wzkris.common.orm.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * Entity基类 审计字段
 *
 * @author wzkris
 */
@Data
@FieldNameConstants
public class BaseEntity implements Serializable {

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private OffsetDateTime createAt;

    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private OffsetDateTime updateAt;

    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updaterId;

    /**
     * 标签
     */
    @TableField(fill = FieldFill.INSERT)
    private String hint;

    /**
     * 逻辑删除标志
     */
    @TableLogic
    private Boolean deleted;

}
