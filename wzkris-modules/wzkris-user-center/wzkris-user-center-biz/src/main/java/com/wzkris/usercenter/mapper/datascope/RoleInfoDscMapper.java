package com.wzkris.usercenter.mapper.datascope;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wzkris.common.orm.annotation.DataColumn;
import com.wzkris.common.orm.annotation.DataScope;
import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.usercenter.domain.RoleInfoDO;
import org.apache.ibatis.annotations.Mapper;
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
@Mapper
@Repository
public interface RoleInfoDscMapper extends BaseMapperPlus<RoleInfoDO> {

    /**
     * 带权限查询列表
     */
    @Select("""
            SELECT DISTINCT r.* FROM biz.role_info r LEFT JOIN biz.role_to_dept rd ON r.role_id = rd.role_id
            ${ew.customSqlSegment}
            """)
    List<RoleInfoDO> selectLists(@Param(Constants.WRAPPER) Wrapper<RoleInfoDO> queryWrapper);

    /**
     * 带权限查询角色信息（用于获取 childrenId 字段）
     */
    @Select("""
            SELECT DISTINCT unnest(r.children_id) FROM biz.role_info r 
            LEFT JOIN biz.role_to_dept rd ON r.role_id = rd.role_id
            WHERE r.role_id = #{roleId}
            """)
    List<Long> listChildrenIdByRoleId(Long roleId);

    /**
     * 校验是否有该角色操作权限
     *
     * @param roleIds 待操作的角色id
     * @return 是否
     */
    @Select("""
            <script>
                SELECT CASE WHEN COUNT(DISTINCT r.role_id) = ${roleIds.size()} THEN true ELSE false END
                        FROM biz.role_info r LEFT JOIN biz.role_to_dept rd ON r.role_id = rd.role_id WHERE r.role_id IN
                    <foreach collection="collection" item="roleId" open="(" separator="," close=")">
                        <if test="roleId != null and roleId != ''">
                            #{roleId}
                        </if>
                    </foreach>
            </script>
            """)
    boolean checkDataScopes(Collection<Long> roleIds);

}
