package com.thingslink.system.mapper;

import com.thingslink.common.orm.plus.BaseMapperPlus;
import com.thingslink.system.domain.DictType;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 字典表 数据层
 *
 * @author wzkris
 */
@Repository
public interface DictTypeMapper extends BaseMapperPlus<DictType> {

    /**
     * 根据字典类型查询信息
     *
     * @param dictType 字典类型
     * @return 字典类型
     */
    @Select("SELECT * FROM dict_type WHERE dict_type = #{dictType}")
    DictType selectByType(String dictType);

}
