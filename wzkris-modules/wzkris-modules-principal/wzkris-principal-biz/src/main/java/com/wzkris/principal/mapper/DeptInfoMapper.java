package com.wzkris.principal.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.principal.domain.DeptInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 部门管理 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface DeptInfoMapper extends BaseMapperPlus<DeptInfoDO> {

    /**
     * 查询所有子级
     *
     * @param parentId 父ID
     * @return 部门列表
     */
    @Select("SELECT * FROM biz.dept_info WHERE #{parentId} = ANY(ancestors) ORDER BY dept_sort, dept_id DESC")
    List<DeptInfoDO> listSubsByParentId(Long parentId);

    /**
     * 根据部门ID查询名称
     */
    @Select("SELECT dept_name FROM biz.dept_info WHERE dept_id = #{deptId}")
    String selectDeptNameById(Long deptId);

    /**
     * 根据ID查询所有子部门id（包括自身）
     *
     * @param deptId 部门ID
     * @return 部门列表
     */
    @Select("SELECT dept_id FROM biz.dept_info WHERE #{deptId} = ANY(ancestors) OR dept_id = #{deptId}")
    List<Long> listSubDeptIdById(Long deptId);

    /**
     * 根据ID查询所有子部门（正常状态）(不包括自身)
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    @Select("SELECT EXISTS(SELECT dept_id FROM biz.dept_info WHERE #{deptId} = ANY(ancestors) AND status = '0')")
    boolean existNormalSubDept(Long deptId);

    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Select("SELECT EXISTS(SELECT dept_id FROM biz.dept_info WHERE parent_id = #{deptId})")
    boolean existSubDept(Long deptId);

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Select("SELECT EXISTS(SELECT dept_id FROM biz.admin_info WHERE dept_id = #{deptId})")
    boolean existAdmin(Long deptId);

}
