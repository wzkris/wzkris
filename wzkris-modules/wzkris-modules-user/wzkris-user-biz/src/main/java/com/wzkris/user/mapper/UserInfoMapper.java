package com.wzkris.user.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.user.domain.UserInfoDO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户表 数据层
 *
 * @author wzkris
 * @description 该接口全部为单表查询
 */
@Repository
public interface UserInfoMapper extends BaseMapperPlus<UserInfoDO> {

    /**
     * 通过手机号查询用户
     *
     * @param phoneNumber 手机号
     * @return 用户对象信息
     */
    @Select("SELECT * FROM biz.user_info WHERE phone_number = #{phoneNumber}")
    UserInfoDO selectByPhoneNumber(String phoneNumber);

    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return 用户对象信息
     */
    @Select("SELECT * FROM biz.user_info WHERE username = #{username}")
    UserInfoDO selectByUsername(String username);

    /**
     * 根据ID获取密码
     */
    @Select("select password from biz.user_info where user_id = #{userId}")
    String selectPwdById(Long userId);

    /**
     * 根据用户id获取手机号
     */
    @Select("select phone_number from biz.user_info where user_id = #{userId}")
    String selectPhoneNumberById(Long userId);

    /**
     * 根据租户ID删除用户
     *
     * @param tenantIds 租户ID
     * @return 结果
     */
    int deleteByTenantIds(List<Long> tenantIds);

}
