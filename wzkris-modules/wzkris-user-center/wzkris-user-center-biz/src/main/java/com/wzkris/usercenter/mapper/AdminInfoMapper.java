package com.wzkris.usercenter.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wzkris.common.orm.annotation.DataColumn;
import com.wzkris.common.orm.annotation.DataScope;
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
 * @description 该接口全部为单表查询
 */
@Mapper
@Repository
public interface AdminInfoMapper extends BaseMapperPlus<AdminInfoDO> {

    /**
     * 通过手机号查询用户
     *
     * @param phoneNumber 手机号
     * @return 用户对象信息
     */
    @Select("SELECT * FROM biz.admin_info WHERE phone_number = #{phoneNumber}")
    AdminInfoDO selectByPhoneNumber(String phoneNumber);

    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return 用户对象信息
     */
    @Select("SELECT * FROM biz.admin_info WHERE username = #{username}")
    AdminInfoDO selectByUsername(String username);

    /**
     * 带权限查询分页数据
     */
    @DataScope(value = {@DataColumn(alias = "d", column = "dept_id")})
    @Select("""
            SELECT u.*, d.dept_name, d.status AS deptStatus
                    		FROM biz.admin_info u LEFT JOIN biz.dept_info d ON u.dept_id = d.dept_id
                    ${ew.customSqlSegment}
            """)
    List<AdminMngResponse> selectVOList(@Param(Constants.WRAPPER) Wrapper<AdminInfoDO> queryWrapper);

    /**
     * 带权限查询列表
     */
    @DataScope(value = {@DataColumn(column = "dept_id")})
    default List<AdminInfoDO> selectLists(Wrapper<AdminInfoDO> queryWrapper) {
        return this.selectList(queryWrapper);
    }

    /**
     * 检验权限
     *
     * @param adminIds 待操作管理员 id
     * @return 返回是否
     */
    @Select("""
            <script>
                SELECT CASE WHEN COUNT(DISTINCT admin_id) = ${adminIds.size()} THEN true ELSE false END
                    FROM biz.admin_info WHERE admin_id IN
                    <foreach collection="collection" item="adminId" open="(" separator="," close=")">
                        <if test="adminId != null and adminId != ''">
                            #{adminId}
                        </if>
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

