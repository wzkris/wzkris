package com.thingslink.user.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.thingslink.common.orm.annotation.DeptScope;
import com.thingslink.common.orm.plus.BaseMapperPlus;
import com.thingslink.user.domain.SysRole;
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
    @DeptScope
    default List<SysRole> selectListInScope(Wrapper<SysRole> queryWrapper) {
        return this.selectList(queryWrapper);
    }

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
    @DeptScope
    int checkDataScopes(@Param("roleIds") List<Long> roleIds);
}
