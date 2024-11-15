package com.wzkris.user.service;

import com.wzkris.user.domain.SysDept;
import com.wzkris.user.domain.vo.SelectTreeVO;

import java.util.Collections;
import java.util.List;

/**
 * 部门管理 服务层
 *
 * @author wzkris
 */
public interface SysDeptService {

    /**
     * 查询部门树结构信息
     *
     * @param dept 部门信息
     * @return 部门树信息集合
     */
    List<SelectTreeVO> listDeptTree(SysDept dept);

    /**
     * 构建树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    List<SysDept> buildDeptTree(List<SysDept> depts);

    /**
     * 构建下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    List<SelectTreeVO> buildDeptTreeSelect(List<SysDept> depts);

    /**
     * 是否存在部门子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    boolean hasChildByDeptId(Long deptId);

    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    int insertDept(SysDept dept);

    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    int updateDept(SysDept dept);

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    boolean checkDeptExistUser(Long deptId);

    /**
     * 校验部门是否有数据权限
     *
     * @param deptIds 部门id
     */
    void checkDataScopes(List<Long> deptIds);

    default void checkDataScopes(Long deptId) {
        this.checkDataScopes(Collections.singletonList(deptId));
    }
}
