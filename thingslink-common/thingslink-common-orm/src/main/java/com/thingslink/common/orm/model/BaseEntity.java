package com.thingslink.common.orm.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

/**
 * Entity基类 审计字段
 *
 * @author wzkris
 */
@Data
@Accessors(chain = true)
@FieldNameConstants
public class BaseEntity implements Serializable {


    /**
     * 创建时间
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @TableField(fill = FieldFill.INSERT)
    private Long createAt;

    /**
     * 创建者
     */
    @JsonIgnore
    @TableField(fill = FieldFill.INSERT)
    private Long createId;

    /**
     * 更新时间
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateAt;

    /**
     * 更新者
     */
    @JsonIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateId;
}
