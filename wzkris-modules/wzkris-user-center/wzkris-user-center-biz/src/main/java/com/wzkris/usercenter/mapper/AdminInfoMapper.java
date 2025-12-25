package com.wzkris.usercenter.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.usercenter.domain.AdminInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

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
     * 根据ID获取密码
     */
    @Select("select password from biz.admin_info where user_id = #{adminId}")
    String selectPwdById(Long adminId);

    /**
     * 根据用户id获取手机号
     */
    @Select("select phone_number from biz.admin_info where user_id = #{adminId}")
    String selectPhoneNumberById(Long adminId);

    /**
     * 根据租户ID删除用户
     *
     * @param tenantIds 租户ID
     * @return 结果
     */
    int deleteByTenantIds(List<Long> tenantIds);

}
