package com.wzkris.usercenter.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.wzkris.common.orm.annotation.DataPermission;
import com.wzkris.common.orm.annotation.DataScope;
import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.usercenter.domain.DeptInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
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
    @Select("SELECT * FROM biz.dept_info WHERE deleted = false AND #{parentId} = ANY(ancestors) ORDER BY dept_sort, dept_id DESC")
    List<DeptInfoDO> listSubsByParentId(Long parentId);

    /**
     * 根据ID查询所有子部门id（包括自身）
     *
     * @param deptId 部门ID
     * @return 部门列表
     */
    @Select("SELECT dept_id FROM biz.dept_info WHERE deleted = false AND (#{deptId} = ANY(ancestors) OR dept_id = #{deptId})")
    List<Long> listSubDeptIdById(Long deptId);

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门 ID
     * @return 结果
     */
    @Select("SELECT EXISTS(SELECT dept_id FROM biz.admin_info WHERE deleted = false AND dept_id = #{deptId})")
    boolean existAdmin(Long deptId);

    /**
     * 带权限查询列表
     */
    @DataScope(@DataPermission(column = "dept_id"))
    default List<DeptInfoDO> selectLists(Wrapper<DeptInfoDO> queryWrapper) {
        return this.selectList(queryWrapper);
    }

    /**
     * 查看当前部门是否有待操作部门的操作权限
     *
     * @param deptIds 待操作的部门 id
     * @return 是否
     */
    @DataScope(@DataPermission(column = "dept_id"))
    @Select("""
            <script>
                SELECT CASE WHEN COUNT(DISTINCT dept_id) = ${deptIds.size()} THEN true ELSE false END
                FROM biz.dept_info
                WHERE deleted = false AND dept_id IN
                <foreach collection="collection" item="deptId" open="(" separator="," close=")">
                    #{deptId}
                </foreach>
            </script>
            """)
    boolean checkDataScopes(Collection<Long> deptIds);

    /**
     * 查看当前部门是否有待操作部门的操作权限（单个 ID）
     *
     * @param deptId 待操作的部门 id
     * @return 是否
     */
    default boolean checkDataScopes(Long deptId) {
        return deptId == null || this.checkDataScopes(Collections.singleton(deptId));
    }

}
