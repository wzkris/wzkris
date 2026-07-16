package com.wzkris.common.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 用户角色视图（跨模块传递的轻量角色信息）
 * <p>
 * 业务层负责填充 dataScope 和 dataIdentities，通用层只读取不做业务判断。
 * <ul>
 *   <li>dataScope: "1"=ALL "2"=CUSTOM "3"=DEPT "4"=DEPT_AND_CHILD "5"=ONLY_SELF</li>
 *   <li>dataIdentities: 预计算的可访问数据标识列表，ALL 时为空列表</li>
 * </ul>
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRole implements Serializable {

    /**
     * 角色 ID
     */
    private Long id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 数据权限范围
     */
    private String dataScope;

    /**
     * 该角色预计算的可访问数据标识列表
     */
    private List<DataIdentity> dataIdentityList;

}
