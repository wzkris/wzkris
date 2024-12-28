package com.wzkris.equipment.service.impl;

import com.wzkris.equipment.mapper.ThingsModelMapper;
import com.wzkris.equipment.service.ThingsModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ThingsModelServiceImpl implements ThingsModelService {

    private final ThingsModelMapper thingsModelMapper;

    @Override
    public boolean insertModel() {
        return false;
    }
}
