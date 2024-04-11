package com.thingslink.auth.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.thingslink.auth.domain.SysRole;
import com.thingslink.common.orm.annotation.DeptScope;
import com.thingslink.common.orm.plus.BaseMapperPlus;
import org.apache.ibatis.annotations.Delete;
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
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    @Delete("""
            <script>
                DELETE FROM sys_role WHERE role_id IN
                    <foreach collection="array" item="roleId" open="(" separator="," close=")">
                        #{roleId}
                    </foreach>
            </script>
            """)
    int deleteByRoleIds(Long[] roleIds);

    /**
     * 校验是否有该角色操作权限
     *
     * @param roleIds 待操作的角色id
     * @return 可访问权限数量
     */
    @Select("""
            <script>
                SELECT COUNT(*) FROM sys_role WHERE role_id IN
                    <foreach collection="array" item="roleId" open="(" separator="," close=")">
                        #{roleId}
                    </foreach>
            </script>
            """)
    @DeptScope
    int checkDataScopes(Long... roleIds);
}
