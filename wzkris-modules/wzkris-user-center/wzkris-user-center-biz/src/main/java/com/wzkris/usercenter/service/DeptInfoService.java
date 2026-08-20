package com.wzkris.usercenter.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.wzkris.common.orm.annotation.DataPermission;
import com.wzkris.common.orm.annotation.DataScope;
import com.wzkris.common.orm.plus.IServicePlus;
import com.wzkris.usercenter.domain.DeptInfoDO;
import com.wzkris.usercenter.response.SelectTreeResponse;

import java.util.List;

/**
 * 部门管理 服务层
 *
 * @author wzkris
 */
public interface DeptInfoService extends IServicePlus<DeptInfoDO> {

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
    boolean updateDept(DeptInfoDO dept);

    /**
     * 删除部门信息
     *
     * @param deptId 部门ID
     */
    boolean removeDept(Long deptId);

    /**
     * 根据名称过滤后构建选择树
     *
     * @param deptName 部门名称（可为空，为空时返回所有部门）
     * @return 选择树结构列表
     */
    List<SelectTreeResponse> listSelectTree(String deptName);

    /**
     * 查看当前部门是否有待操作部门的操作权限
     *
     * @param deptId 待操作的部门 id
     * @return 是否
     */
    boolean checkDataScopes(Long deptId);

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门 ID
     * @return 结果
     */
    boolean existAdmin(Long deptId);

    /**
     * 带数据权限查询列表
     *
     * @param queryWrapper 查询条件
     * @return 部门列表
     */
    @DataScope(@DataPermission(column = "id"))
    List<DeptInfoDO> selectLists(Wrapper<DeptInfoDO> queryWrapper);

}

