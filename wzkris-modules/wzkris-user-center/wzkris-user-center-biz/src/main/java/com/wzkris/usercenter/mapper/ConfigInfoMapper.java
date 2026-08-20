package com.wzkris.usercenter.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.usercenter.domain.ConfigInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 参数配置 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface ConfigInfoMapper extends BaseMapperPlus<ConfigInfoDO> {

}
