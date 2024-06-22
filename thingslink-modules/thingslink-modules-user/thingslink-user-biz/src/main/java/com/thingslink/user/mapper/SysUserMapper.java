package com.thingslink.user.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.thingslink.common.orm.annotation.DeptScope;
import com.thingslink.common.orm.plus.BaseMapperPlus;
import com.thingslink.user.domain.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

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
     * 带权限查询列表
     */
    @DeptScope
    default List<SysUser> selectListInScope(Wrapper<SysUser> queryWrapper) {
        return this.selectList(queryWrapper);
    }

    /**
     * 通过手机号查询用户
     *
     * @param phoneNumber 手机号
     * @return 用户对象信息
     */
    @Select("SELECT * FROM sys_user WHERE phone_number = #{phoneNumber}")
    SysUser selectByPhoneNumber(String phoneNumber);

    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return 用户对象信息
     */
    @Select("SELECT * FROM sys_user WHERE username = #{username}")
    SysUser selectByUsername(String username);

    /**
     * 根据用户名获取密码
     */
    @Select("select password from sys_user where username = #{username}")
    String selectPwdByUserName(String username);

    /**
     * 根据用户id获取手机号
     */
    @Select("select phone_number from sys_user where user_id = #{userId}")
    String selectPhoneNumberById(Long userId);

    /**
     * 修改用户头像
     *
     * @param username 用户名
     * @param avatar   头像地址
     * @return 结果
     */
    @Update("UPDATE sys_user SET avatar = #{avatar} WHERE username = #{username}")
    int updateAvatar(@Param("username") String username, @Param("avatar") String avatar);

    /**
     * 批量真实删除用户
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    int realDeleteBatchById(Long... userIds);

    /**
     * 检验权限
     *
     * @param userIds 待操作管理员id
     * @return 返回可操作数量
     */
    @DeptScope
    @Select("""
            <script>
                SELECT COUNT(*) FROM sys_user WHERE user_id IN
                    <foreach collection="userIds" item="userId" open="(" separator="," close=")">
                        #{userId}
                    </foreach>
            </script>
            """)
    int checkDataScopes(@Param("userIds") List<Long> userIds);
}
