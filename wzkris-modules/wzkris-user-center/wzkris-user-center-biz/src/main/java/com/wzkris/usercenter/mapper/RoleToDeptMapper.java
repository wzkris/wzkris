package com.wzkris.usercenter.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.usercenter.domain.RoleToDeptDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色与部门关联表 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface RoleToDeptMapper extends BaseMapperPlus<RoleToDeptDO> {

    /**
     * 根据角色 ID 查询关联部门 id 集合
     *
     * @param roleIds 角色 id 集合
     * @return 部门 id 集合
     */
    @Select("""
            <script>
                SELECT dept_id FROM biz.role_to_dept WHERE role_id IN
                    <foreach collection="roleIds" item="roleId" open="(" separator="," close=")">
                        #{roleId}
                    </foreach>
                    AND deleted = false
            </script>
            """)
    List<Long> listDeptIdByRoleIds(List<Long> roleIds);

}
