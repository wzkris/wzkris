package com.wzkris.user.mapper.datascope;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.wzkris.common.orm.annotation.DataColumn;
import com.wzkris.common.orm.annotation.DataScope;
import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.user.domain.SysDept;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * 部门管理 数据权限层
 *
 * @author wzkris
 */
@DataScope(value = {@DataColumn(column = "dept_id")})
@Repository
public interface SysDeptDscMapper extends BaseMapperPlus<SysDept> {

    /**
     * 带权限查询列表
     */
    default List<SysDept> selectLists(Wrapper<SysDept> queryWrapper) {
        return this.selectList(queryWrapper);
    }

    /**
     * 根据角色ID查询关联部门id集合
     *
     * @param roleIds 角色id集合
     * @return 部门id集合
     */
    @Select("""
            <script>
                SELECT dept_id FROM biz.sys_role_dept WHERE role_id IN
                    <foreach collection="roleIds" item="roleId" open="(" separator="," close=")">
                        #{roleId}
                    </foreach>
            </script>
            """)
    List<Long> listDeptIdByRoleIds(List<Long> roleIds);

    /**
     * 查看当前部门是否有待操作部门的操作权限
     *
     * @param deptIds 待操作的部门id
     * @return 是否
     */
    @Select("""
            <script>
                SELECT CASE WHEN COUNT(DISTINCT dept_id) = ${deptIds.size()} THEN true ELSE false END
                    FROM biz.sys_dept WHERE dept_id IN
                    <foreach collection="collection" item="deptId" open="(" separator="," close=")">
                        <if test="deptId != null and deptId != ''">
                            #{deptId}
                        </if>
                    </foreach>
            </script>
            """)
    boolean checkDataScopes(Collection<Long> deptIds);

}
