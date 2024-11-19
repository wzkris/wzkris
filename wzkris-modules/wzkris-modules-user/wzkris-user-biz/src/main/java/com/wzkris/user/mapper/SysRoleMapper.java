package com.wzkris.user.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wzkris.common.orm.annotation.DeptScope;
import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.user.domain.SysRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色表 数据层
 *
 * @author wzkris
 */
@Repository
public interface SysRoleMapper extends BaseMapperPlus<SysRole> {

    /**
     * 带权限查询列表
     */
    @Select("""
            SELECT DISTINCT r.* FROM sys_role r LEFT JOIN sys_role_dept rd ON r.role_id = rd.role_id
            ${ew.customSqlSegment}
            """)
    @DeptScope(tableAlias = "rd")
    List<SysRole> selectListInScope(@Param(Constants.WRAPPER) Wrapper<SysRole> queryWrapper);

    /**
     * 校验是否有该角色操作权限
     *
     * @param roleIds 待操作的角色id
     * @return 可访问权限数量
     */
    @Select("""
            <script>
                SELECT COUNT(*) FROM sys_role WHERE role_id IN
                    <foreach collection="roleIds" item="roleId" open="(" separator="," close=")">
                        #{roleId}
                    </foreach>
            </script>
            """)
    int checkDataScopes(@Param("roleIds") List<Long> roleIds);
}
