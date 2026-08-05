package com.wzkris.usercenter.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.usercenter.domain.AdminToRoleDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 管理员和角色关联表 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface AdminToRoleMapper extends BaseMapperPlus<AdminToRoleDO> {

}
