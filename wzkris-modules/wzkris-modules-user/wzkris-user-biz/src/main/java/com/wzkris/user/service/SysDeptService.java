package com.wzkris.user.service;

import com.wzkris.user.domain.SysDept;
import com.wzkris.user.domain.vo.SelectTreeVO;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 部门管理 服务层
 *
 * @author wzkris
 */
public interface SysDeptService {

    /**
     * 查询部门选择树
     *
     * @param deptName 筛选条件
     * @return 部门树信息集合
     */
    List<SelectTreeVO> listSelectTree(String deptName);

    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     */
    boolean insertDept(SysDept dept);

    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     */
    boolean updateDept(SysDept dept);

    /**
     * 删除部门信息
     *
     * @param deptId 部门ID
     */
    boolean deleteById(Long deptId);

    /**
     * 校验部门是否有数据权限
     *
     * @param deptIds 部门id
     */
    void checkDataScopes(Collection<Long> deptIds);

    default void checkDataScopes(Long deptId) {
        if (deptId != null) {
            this.checkDataScopes(Collections.singleton(deptId));
        }
    }

}
