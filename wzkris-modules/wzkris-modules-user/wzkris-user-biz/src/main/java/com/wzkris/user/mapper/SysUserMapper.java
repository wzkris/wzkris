package com.wzkris.user.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wzkris.common.orm.annotation.DeptScope;
import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.user.domain.SysUser;
import com.wzkris.user.domain.vo.SysUserVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * 用户表 数据层
 *
 * @author wzkris
 * @description 该接口全部为单表查询
 */
@Repository
public interface SysUserMapper extends BaseMapperPlus<SysUser> {

    /**
     * 带权限查询分页数据
     */
    @Select("""
            SELECT u.*, d.dept_name, d.status AS deptStatus
            		FROM biz_sys.sys_user u LEFT JOIN biz_sys.sys_dept d ON u.dept_id = d.dept_id
            ${ew.customSqlSegment}
            """)
    @DeptScope(tableAlias = "d")
    List<SysUserVO> selectVOList(@Param(Constants.WRAPPER) Wrapper<SysUser> queryWrapper);

    /**
     * 带权限查询列表
     */
    @DeptScope
    default List<SysUser> selectLists(Wrapper<SysUser> queryWrapper) {
        return this.selectList(queryWrapper);
    }

    /**
     * 通过手机号查询用户
     *
     * @param phoneNumber 手机号
     * @return 用户对象信息
     */
    @Select("SELECT * FROM biz_sys.sys_user WHERE phone_number = #{phoneNumber}")
    SysUser selectByPhoneNumber(String phoneNumber);

    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return 用户对象信息
     */
    @Select("SELECT * FROM biz_sys.sys_user WHERE username = #{username}")
    SysUser selectByUsername(String username);

    /**
     * 根据ID获取密码
     */
    @Select("select password from biz_sys.sys_user where user_id = #{userId}")
    String selectPwdById(Long userId);

    /**
     * 根据用户id获取手机号
     */
    @Select("select phone_number from biz_sys.sys_user where user_id = #{userId}")
    String selectPhoneNumberById(Long userId);

    /**
     * 硬删除用户
     *
     * @param tenantIds 租户ID
     * @return 结果
     */
    int deleteByTenantIds(@Param("tenantIds") List<Long> tenantIds);

    /**
     * 检验权限
     *
     * @param userIds 待操作管理员id
     * @return 返回是否
     */
    @DeptScope
    @Select("""
            <script>
                SELECT CASE WHEN COUNT(DISTINCT user_id) = ${userIds.size()} THEN 1 ELSE 0 END
                    FROM biz_sys.sys_user WHERE user_id IN
                    <foreach collection="userIds" item="userId" open="(" separator="," close=")">
                        <if test="userId != null and userId != ''">
                            #{userId}
                        </if>
                    </foreach>
            </script>
            """)
    boolean checkDataScopes(@Param("userIds") Collection<Long> userIds);

}
