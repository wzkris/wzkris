package com.wzkris.equipment.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.equipment.domain.Device;
import com.wzkris.equipment.mapper.DeviceMapper;
import com.wzkris.equipment.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * (product)表控制层
 *
 * @author wzkris
 * @since 2024-12-04 14:40:00
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final DeviceMapper deviceMapper;

    @Override
    public boolean checkProductUsed(Long pdtId) {
        return deviceMapper.exists(Wrappers.lambdaQuery(Device.class)
                .eq(Device::getPdtId, pdtId));
    }
}
