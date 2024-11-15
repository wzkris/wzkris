package com.wzkris.system.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.system.domain.SysDictType;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 字典表 数据层
 *
 * @author wzkris
 */
@Repository
public interface SysDictTypeMapper extends BaseMapperPlus<SysDictType> {

    /**
     * 根据字典类型查询信息
     *
     * @param dictType 字典类型
     * @return 字典类型
     */
    @Select("SELECT * FROM sys_dict_type WHERE dict_type = #{dictType}")
    SysDictType selectByType(String dictType);

}
