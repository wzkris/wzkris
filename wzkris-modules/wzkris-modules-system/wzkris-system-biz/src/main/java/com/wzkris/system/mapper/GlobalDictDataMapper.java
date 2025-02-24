package com.wzkris.system.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.system.domain.GlobalDictData;
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
public interface GlobalDictDataMapper extends BaseMapperPlus<GlobalDictData> {

    @Select("""
            <script>
                SELECT * FROM biz_sys.global_dict_data WHERE dict_type IN
                <foreach collection="dictTypes" item="dictType" open="(" close=")" separator=",">
             	    #{dictType}
                </foreach>
                ORDER BY dict_sort
            </script>
            """)
    List<GlobalDictData> listByTypes(@Param("dictTypes") List<String> dictTypes);

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @Select("SELECT * FROM biz_sys.global_dict_data WHERE dict_type = #{dictType} ORDER BY dict_sort")
    List<GlobalDictData> listByType(String dictType);

    /**
     * 同步修改字典类型
     *
     * @param oldDictType 旧字典类型
     * @param newDictType 新旧字典类型
     * @return 结果
     */
    @Update("UPDATE biz_sys.global_dict_data SET dict_type = #{newDictType} WHERE dict_type = #{oldDictType}")
    int updateDictByType(@Param("oldDictType") String oldDictType, @Param("newDictType") String newDictType);

}
