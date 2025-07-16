package com.wzkris.user.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.user.domain.SysRole;
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

    @Select("""
            <script>
                SELECT CASE WHEN COUNT(DISTINCT r.role_id) = ${roleIds.size()} THEN true ELSE false END
                FROM biz.sys_role r 
                WHERE r.role_id IN
                    <foreach collection="roleIds" item="roleId" open="(" separator="," close=")">
                        #{roleId}
                    </foreach>
                AND r.inherited = #{inherited}
            </script>
            """)
    boolean checkInherited(List<Long> roleIds, boolean inherited);

}
