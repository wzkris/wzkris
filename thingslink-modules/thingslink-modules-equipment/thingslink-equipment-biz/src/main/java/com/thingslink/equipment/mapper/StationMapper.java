package com.thingslink.equipment.mapper;

import com.thingslink.common.orm.plus.BaseMapperPlus;
import com.thingslink.equipment.domain.Station;
import com.thingslink.equipment.domain.dto.LocationDTO;
import com.thingslink.equipment.domain.vo.StationVO;
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
