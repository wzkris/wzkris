package com.wzkris.user.mapper;

import com.wzkris.user.domain.SysRoleDept;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色与部门关联表 数据层
 *
 * @author wzkris
 */
@Repository
public interface SysRoleDeptMapper {

    /**
     * 通过角色ID删除角色和部门关联
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Delete("DELETE FROM biz.sys_role_dept WHERE role_id = #{roleId}")
    int deleteByRoleId(Long roleId);

    /**
     * 批量删除角色部门关联信息
     *
     * @param roleIds 角色id集合
     * @return 结果
     */
    @Delete("""
            <script>
                DELETE FROM biz.sys_role_dept WHERE role_id IN
                    <foreach collection="roleIds" item="roleId" open="(" separator="," close=")">
                        #{roleId}
                    </foreach>
            </script>
            """)
    int deleteByRoleIds(List<Long> roleIds);

    /**
     * 通过部门ID删除角色和部门关联
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Delete("DELETE FROM biz.sys_role_dept WHERE dept_id = #{deptId}")
    int deleteByDeptId(Long deptId);

    /**
     * 批量新增角色部门信息
     *
     * @param list 角色部门列表
     * @return 结果
     */
    @Insert("""
            <script>
                INSERT INTO biz.sys_role_dept(role_id, dept_id) VALUES
                    <foreach item="item" index="index" collection="list" separator=",">
                        (#{item.roleId}, #{item.deptId})
                    </foreach>
            </script>
            """)
    int insertBatch(List<SysRoleDept> list);

}
