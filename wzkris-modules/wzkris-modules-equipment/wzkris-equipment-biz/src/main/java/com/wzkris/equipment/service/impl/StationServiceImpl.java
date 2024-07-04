package com.wzkris.equipment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.equipment.domain.Station;
import com.wzkris.equipment.mapper.StationMapper;
import com.wzkris.equipment.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StationServiceImpl implements StationService {

    private final StationMapper stationMapper;


    @Override
    public List<Station> list(Station station) {
        LambdaQueryWrapper<Station> lqw = this.buildQueryWrapper(station);

        return stationMapper.selectList(lqw);
    }

    private LambdaQueryWrapper<Station> buildQueryWrapper(Station station) {
        return new LambdaQueryWrapper<Station>()
                .like(StringUtil.isNotBlank(station.getStationName()), Station::getStationName, station.getStationName())
                ;
    }
}
