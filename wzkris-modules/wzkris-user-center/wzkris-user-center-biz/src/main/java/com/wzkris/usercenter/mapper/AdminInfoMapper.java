package com.wzkris.usercenter.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wzkris.common.orm.annotation.DataPermission;
import com.wzkris.common.orm.annotation.DataScope;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.usercenter.api.admin.response.AdminMngResponse;
import com.wzkris.usercenter.domain.AdminInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 用户表 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface AdminInfoMapper extends BaseMapperPlus<AdminInfoDO> {

    /**
     * 带权限查询分页数据
     */
    @DataScope(@DataPermission(alias = "d", column = "dept_id"))
    @Select("""
            SELECT u.*, d.dept_name, d.status AS deptStatus
            FROM biz.admin_info u LEFT JOIN biz.dept_info d ON u.dept_id = d.dept_id
            ${ew.customSqlSegment}
            """)
    List<AdminMngResponse> selectVOList(@Param(Constants.WRAPPER) Wrapper<AdminInfoDO> queryWrapper);

    /**
     * 带权限查询分页数据（分页）
     */
    @DataScope(@DataPermission(alias = "d", column = "dept_id"))
    @Select("""
            SELECT u.*, d.dept_name, d.status AS deptStatus
            FROM biz.admin_info u LEFT JOIN biz.dept_info d ON u.dept_id = d.dept_id
            ${ew.customSqlSegment}
            """)
    IPage<AdminMngResponse> selectVOPage(IPage<AdminMngResponse> page, @Param(Constants.WRAPPER) Wrapper<AdminInfoDO> queryWrapper);

    /**
     * 检验权限
     *
     * @param adminIds 待操作管理员 id
     * @return 返回是否
     */
    @DataScope(@DataPermission(alias = "ai", column = "dept_id"))
    @Select("""
            <script>
                SELECT CASE WHEN COUNT(DISTINCT ai.admin_id) = ${adminIds.size()} THEN true ELSE false END
                FROM biz.admin_info ai
                WHERE ai.deleted = false AND ai.admin_id IN
                <foreach collection="collection" item="adminId" open="(" separator="," close=")">
                    #{adminId}
                </foreach>
            </script>
            """)
    boolean checkDataScopes(Collection<Long> adminIds);

    /**
     * 检验权限（单个 ID）
     *
     * @param adminId 待操作管理员 id
     * @return 返回是否
     */
    default boolean checkDataScopes(Long adminId) {
        return adminId == null || this.checkDataScopes(Collections.singleton(adminId));
    }

}

