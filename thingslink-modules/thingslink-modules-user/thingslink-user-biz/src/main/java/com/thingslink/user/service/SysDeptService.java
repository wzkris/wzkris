package com.thingslink.user.service;

import com.thingslink.user.domain.SysDept;
import com.thingslink.user.domain.vo.SelectTree;

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
    List<SelectTree> listDeptTree(SysDept dept);

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
    List<SelectTree> buildDeptTreeSelect(List<SysDept> depts);

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
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    int deleteDeptById(Long deptId);

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

    /**
     * 校验相关参数的租户ID是否一致
     *
     * @param dept 部门参数
     */
    void checkTenantId(SysDept dept);
}
