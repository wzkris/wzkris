package com.thingslink.system.mapper;

import com.thingslink.common.orm.plus.BaseMapperPlus;
import com.thingslink.system.domain.SysConfig;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 参数配置 数据层
 *
 * @author wzkris
 */
@Repository
public interface SysConfigMapper extends BaseMapperPlus<SysConfig> {

    @Select("SELECT * FROM sys_config WHERE config_key = #{configKey}")
    SysConfig selectConfig(String configKey);

}
