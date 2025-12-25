package com.wzkris.usercenter.service;

import com.wzkris.usercenter.domain.DeptInfoDO;
import com.wzkris.usercenter.domain.vo.SelectTreeVO;

import java.util.List;

/**
 * 部门管理 服务层
 *
 * @author wzkris
 */
public interface DeptInfoService {

    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     */
    boolean saveDept(DeptInfoDO dept);

    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     */
    boolean modifyDept(DeptInfoDO dept);

    /**
     * 删除部门信息
     *
     * @param deptId 部门ID
     */
    boolean removeById(Long deptId);

    /**
     * 构建选择树结构
     *
     * @param depts 部门列表
     * @return 选择树结构列表
     */
    List<SelectTreeVO> buildSelectTree(List<DeptInfoDO> depts);

}
