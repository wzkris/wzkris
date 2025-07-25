package com.wzkris.user.mapper.datascope;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wzkris.common.orm.annotation.DataColumn;
import com.wzkris.common.orm.annotation.DataScope;
import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.user.domain.SysRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * 角色表 数据权限层
 *
 * @author wzkris
 */
@DataScope(value = {@DataColumn(alias = "rd", column = "dept_id")})
@Repository
public interface SysRoleDscMapper extends BaseMapperPlus<SysRole> {

    /**
     * 带权限查询列表
     */
    @Select("""
            SELECT DISTINCT r.* FROM biz.sys_role r LEFT JOIN biz.sys_role_dept rd ON r.role_id = rd.role_id
            ${ew.customSqlSegment}
            """)
    List<SysRole> selectLists(@Param(Constants.WRAPPER) Wrapper<SysRole> queryWrapper);

    /**
     * 带权限查询已继承的角色ID
     */
    @Select("""
            SELECT DISTINCT rh.inherited_id FROM biz.sys_role_hierarchy rh LEFT JOIN biz.sys_role_dept rd ON rh.role_id = rd.role_id
            WHERE rh.role_id = #{roleId}
            """)
    List<Long> listInheritedIdByRoleId(Long roleId);

    /**
     * 校验是否有该角色操作权限
     *
     * @param roleIds 待操作的角色id
     * @return 是否
     */
    @Select("""
            <script>
                SELECT CASE WHEN COUNT(DISTINCT r.role_id) = ${roleIds.size()} THEN true ELSE false END
                        FROM biz.sys_role r LEFT JOIN biz.sys_role_dept rd ON r.role_id = rd.role_id WHERE r.role_id IN
                    <foreach collection="collection" item="roleId" open="(" separator="," close=")">
                        <if test="roleId != null and roleId != ''">
                            #{roleId}
                        </if>
                    </foreach>
            </script>
            """)
    boolean checkDataScopes(Collection<Long> roleIds);

}
