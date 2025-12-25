package com.wzkris.system.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.system.domain.ConfigInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 参数配置 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface ConfigInfoMapper extends BaseMapperPlus<ConfigInfoDO> {

    @Select("SELECT * FROM biz.config_info WHERE config_key = #{configKey}")
    ConfigInfoDO selectByKey(String configKey);

    @Select("SELECT config_value FROM biz.config_info WHERE config_key = #{configKey}")
    String selectValueByKey(String configKey);

}
