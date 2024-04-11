package com.thingslink.system.mapper;

import com.thingslink.common.orm.plus.BaseMapperPlus;
import com.thingslink.system.domain.Config;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 参数配置 数据层
 *
 * @author wzkris
 */
@Repository
public interface ConfigMapper extends BaseMapperPlus<Config> {

    @Select("SELECT * FROM config WHERE config_key = #{configKey}")
    Config selectConfig(String configKey);

}
