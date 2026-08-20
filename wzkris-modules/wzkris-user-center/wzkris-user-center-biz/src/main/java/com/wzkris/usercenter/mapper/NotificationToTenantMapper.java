package com.wzkris.usercenter.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.usercenter.domain.NotificationToTenantDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 通知发送表 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface NotificationToTenantMapper extends BaseMapperPlus<NotificationToTenantDO> {

}
