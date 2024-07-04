package com.wzkris.equipment.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.equipment.domain.Station;
import com.wzkris.equipment.domain.dto.LocationDTO;
import com.wzkris.equipment.domain.vo.StationVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wzkris
 * @description 针对表【station】的数据库操作Mapper
 * @since 2023-06-05 15:32:07
 */
@Repository
public interface StationMapper extends BaseMapperPlus<Station> {

    /**
     * 根据距离筛选站点
     *
     * @param locationDTO 筛选条件
     * @return 对象列表
     */
    List<StationVO> listVOByLocation(LocationDTO locationDTO);

}
