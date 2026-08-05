package com.wzkris.usercenter.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.usercenter.domain.NotificationToAdminDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 通知发送表 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface NotificationToAdminMapper extends BaseMapperPlus<NotificationToAdminDO> {

}
