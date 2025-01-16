package com.wzkris.equipment.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.equipment.domain.Device;
import com.wzkris.equipment.domain.Product;
import com.wzkris.equipment.domain.ThingsModel;
import com.wzkris.equipment.mapper.DeviceMapper;
import com.wzkris.equipment.mapper.ProductMapper;
import com.wzkris.equipment.mapper.ThingsModelMapper;
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

    private final ProductMapper productMapper;

    private final ThingsModelMapper thingsModelMapper;

    @Override
    public boolean insertProduct(Product product) {
        if (StringUtil.isBlank(product.getPdtKey())) {
            product.setPdtKey(RandomUtil.randomString(16));
        }
        return productMapper.insert(product) > 0;
    }

    @Override
    public boolean checkDeviceUsed(Long pdtId) {
        return deviceMapper.exists(Wrappers.lambdaQuery(Device.class)
                .eq(Device::getPdtId, pdtId));
    }

    @Override
    public boolean checkThingsModelUsed(Long pdtId) {
        return thingsModelMapper.exists(Wrappers.lambdaQuery(ThingsModel.class)
                .eq(ThingsModel::getPdtId, pdtId));
    }
}
