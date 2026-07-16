package com.wzkris.common.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 数据权限标识（如部门、区域等）
 * <p>
 * 用于 {@link UserRole#getDataIdentityList()} 中携带可访问的数据实体信息。
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataIdentity implements Serializable {

    /**
     * 标识 ID（如部门 ID）
     */
    private Long id;

    /**
     * 标识名称（如部门名称）
     */
    private String name;

}
