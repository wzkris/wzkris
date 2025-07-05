package com.wzkris.user.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.user.domain.SysDept;
import com.wzkris.user.domain.req.SysDeptQueryReq;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 部门管理 数据层
 *
 * @author wzkris
 */
@Repository
public interface SysDeptMapper extends BaseMapperPlus<SysDept> {

    /**
     * 根据条件查询所有子部门（包括自身）
     *
     * @param queryReq 查询条件
     * @return 部门列表
     */
    List<SysDept> listSubDept(SysDeptQueryReq queryReq);

    /**
     * 根据部门ID查询名称
     */
    @Select("SELECT dept_name FROM biz.sys_dept WHERE dept_id = #{deptId}")
    String selectDeptNameById(Long deptId);

    /**
     * 根据ID查询所有子部门id（包括自身）
     *
     * @param deptId 部门ID
     * @return 部门列表
     */
    @Select("SELECT dept_id FROM biz.sys_dept WHERE #{deptId} = ANY(ancestors) OR dept_id = #{deptId}")
    List<Long> listSubDeptIdById(Long deptId);

    /**
     * 根据ID查询所有子部门（正常状态）(不包括自身)
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    @Select("SELECT EXISTS(SELECT dept_id FROM biz.sys_dept WHERE #{deptId} = ANY(ancestors) AND status = '0')")
    boolean existNormalSubDept(Long deptId);

    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Select("SELECT EXISTS(SELECT dept_id FROM biz.sys_dept WHERE parent_id = #{deptId})")
    boolean existSubDept(Long deptId);

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Select("SELECT EXISTS(SELECT dept_id FROM biz.sys_user WHERE dept_id = #{deptId})")
    boolean existUser(Long deptId);

}
