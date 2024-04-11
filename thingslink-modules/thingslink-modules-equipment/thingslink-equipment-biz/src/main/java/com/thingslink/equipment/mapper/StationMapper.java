package com.thingslink.equipment.mapper;

import com.thingslink.equipment.domain.Station;
import com.thingslink.equipment.domain.dto.LocationDTO;
import com.thingslink.equipment.domain.vo.StationVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wzkris
 * @description 针对表【station】的数据库操作Mapper
 * @since 2023-06-05 15:32:07
 */
@Repository
public interface StationMapper {
    /**
     * 根据id查询
     *
     * @param stationId 电站id
     * @return 电站
     */
    Station getById(Long stationId);

    /**
     * 查询指定行数据
     *
     * @param station 查询条件
     * @return 对象列表
     */
    List<Station> list(Station station);

    /**
     * 根据电站id查询指定数据
     *
     * @param stationIds 电站id集合
     * @return 对象列表
     */
    List<Station> listByIds(@Param("stationIds") List<Long> stationIds);

    /**
     * 根据距离筛选站点
     *
     * @param locationDTO 筛选条件
     * @return 对象列表
     */
    List<StationVO> listVOByLocation(LocationDTO locationDTO);

    /**
     * 新增数据
     *
     * @param station 实例对象
     * @return 影响行数
     */
    int insert(Station station);

    /**
     * 修改数据
     *
     * @param station 实例对象
     * @return 影响行数
     */
    int updateById(Station station);

    /**
     * 通过主键删除数据
     *
     * @param stationId 主键
     * @return 影响行数
     */
    int deleteById(Long stationId);
}
