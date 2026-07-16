package com.wzkris.common.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 用户角色视图（跨模块传递的轻量角色信息）
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRole implements Serializable {

    private Long id;

    private String name;

    /**
     * 数据权限范围
     */
    private String dataScope;

    /**
     * 该角色预计算的可访问数据标识列表
     */
    private List<DataIdentity> dataIdentityList;

    /**
     * 该角色的功能权限码
     */
    private List<String> permissions;

}
