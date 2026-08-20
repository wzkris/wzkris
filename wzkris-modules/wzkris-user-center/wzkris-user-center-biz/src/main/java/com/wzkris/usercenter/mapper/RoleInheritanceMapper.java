package com.wzkris.usercenter.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.usercenter.domain.RoleInheritanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色继承关系关联表 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface RoleInheritanceMapper extends BaseMapperPlus<RoleInheritanceDO> {

    /**
     * 批量查询角色的所有子角色 ID（包括递归）
     *
     * @param roleIds 角色 ID 列表
     * @return 子角色 ID 列表
     */
    @Select("""
            <script>
                WITH RECURSIVE child_roles AS (
                    SELECT child_id FROM biz.role_inheritance WHERE role_id IN
                        <foreach collection="list" item="roleId" separator="," open="(" close=")">
                            #{roleId}
                        </foreach>
                        AND deleted = false
                    UNION
                    SELECT ri.child_id FROM biz.role_inheritance ri
                    INNER JOIN child_roles cr ON ri.role_id = cr.child_id
                    WHERE ri.deleted = false
                )
                SELECT DISTINCT child_id FROM child_roles
            </script>
            """)
    List<Long> listChildIdsRecursive(List<Long> roleIds);

}
