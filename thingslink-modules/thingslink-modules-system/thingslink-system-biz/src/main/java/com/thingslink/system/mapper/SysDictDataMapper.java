package com.thingslink.system.mapper;

import com.thingslink.common.orm.plus.BaseMapperPlus;
import com.thingslink.system.domain.SysDictData;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 字典表 数据层
 *
 * @author wzkris
 */
@Repository
public interface SysDictDataMapper extends BaseMapperPlus<SysDictData> {

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @Select("SELECT * FROM sys_dict_data WHERE dict_type = #{dictType} ORDER BY dict_sort")
    List<SysDictData> listByType(String dictType);

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    @Select("SELECT dict_label FROM sys_dict_data WHERE dict_type = #{dictType} AND dict_value = #{dictValue}")
    String getDictLabel(@Param("dictType") String dictType, @Param("dictValue") String dictValue);

    /**
     * 查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据
     */
    @Select("SELECT COUNT(*) FROM sys_dict_data WHERE dict_type = #{dictType}")
    int countByType(String dictType);

    /**
     * 同步修改字典类型
     *
     * @param oldDictType 旧字典类型
     * @param newDictType 新旧字典类型
     * @return 结果
     */
    @Update("UPDATE sys_dict_data SET dict_type = #{newDictType} WHERE dict_type = #{oldDictType}")
    int updateDictDataType(@Param("oldDictType") String oldDictType, @Param("newDictType") String newDictType);
}
