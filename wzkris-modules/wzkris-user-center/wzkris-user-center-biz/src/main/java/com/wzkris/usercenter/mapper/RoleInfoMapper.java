package com.wzkris.usercenter.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.usercenter.domain.RoleInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 角色表 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface RoleInfoMapper extends BaseMapperPlus<RoleInfoDO> {

}
