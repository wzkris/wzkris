package com.wzkris.usercenter.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.usercenter.domain.AnnouncementInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 系统消息表 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface AnnouncementInfoMapper extends BaseMapperPlus<AnnouncementInfoDO> {

}
