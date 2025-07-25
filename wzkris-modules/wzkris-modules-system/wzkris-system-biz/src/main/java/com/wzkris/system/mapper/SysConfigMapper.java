package com.wzkris.system.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.system.domain.SysConfig;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 参数配置 数据层
 *
 * @author wzkris
 */
@Repository
public interface SysConfigMapper extends BaseMapperPlus<SysConfig> {

    @Select("SELECT * FROM biz.sys_config WHERE config_key = #{configKey}")
    SysConfig selectByKey(String configKey);

    @Select("SELECT config_value FROM biz.sys_config WHERE config_key = #{configKey}")
    String selectValueByKey(String configKey);

}
