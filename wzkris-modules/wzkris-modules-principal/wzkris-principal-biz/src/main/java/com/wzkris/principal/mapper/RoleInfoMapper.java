package com.wzkris.principal.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.principal.domain.RoleInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色表 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface RoleInfoMapper extends BaseMapperPlus<RoleInfoDO> {

    @Select("""
            <script>
                SELECT CASE WHEN COUNT(DISTINCT r.role_id) = ${roleIds.size()} THEN true ELSE false END
                FROM biz.role_info r 
                WHERE r.role_id IN
                    <foreach collection="roleIds" item="roleId" open="(" separator="," close=")">
                        #{roleId}
                    </foreach>
                AND r.inherited = #{inherited}
            </script>
            """)
    boolean checkInherited(List<Long> roleIds, boolean inherited);

}
