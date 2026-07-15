package com.wzkris.usercenter.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wzkris.common.orm.annotation.DataColumn;
import com.wzkris.common.orm.annotation.DataScope;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.usercenter.domain.RoleInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;

/**
 * 角色表 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface RoleInfoMapper extends BaseMapperPlus<RoleInfoDO> {

    /**
     * 带权限查询列表
     */
    @DataScope(value = {@DataColumn(alias = "rd", column = "dept_id")})
    @Select("""
            SELECT DISTINCT r.* FROM biz.role_info r LEFT JOIN biz.role_to_dept rd ON r.role_id = rd.role_id
            ${ew.customSqlSegment}
            """)
    IPage<RoleInfoDO> selectPageList(IPage<RoleInfoDO> page, @Param(Constants.WRAPPER) Wrapper<RoleInfoDO> queryWrapper);

    /**
     * 校验是否有该角色操作权限
     *
     * @param roleIds 待操作的角色 id
     * @return 是否
     */
    @DataScope(value = {@DataColumn(column = "rd.dept_id")})
    @Select("""
            <script>
                SELECT CASE WHEN COUNT(DISTINCT r.role_id) = ${roleIds.size()} THEN true ELSE false END
                FROM biz.role_info r
                LEFT JOIN biz.role_to_dept rd ON r.role_id = rd.role_id
                WHERE r.deleted = false AND r.role_id IN
                <foreach collection="collection" item="roleId" open="(" separator="," close=")">
                    #{roleId}
                </foreach>
            </script>
            """)
    boolean checkDataScopes(Collection<Long> roleIds);

    /**
     * 校验是否有该角色操作权限（单个 ID）
     *
     * @param roleId 待操作的角色 id
     * @return 是否
     */
    default boolean checkDataScopes(Long roleId) {
        return roleId == null || this.checkDataScopes(Collections.singleton(roleId));
    }

}
