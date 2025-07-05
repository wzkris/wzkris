package com.wzkris.user.service;

import com.wzkris.user.domain.SysDept;
import com.wzkris.user.domain.vo.SelectTreeVO;

import java.util.List;

/**
 * 部门管理 服务层
 *
 * @author wzkris
 */
public interface SysDeptService {

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
     * 构建选择树结构
     *
     * @param depts 部门列表
     * @return 选择树结构列表
     */
    List<SelectTreeVO> buildSelectTree(List<SysDept> depts);

}
