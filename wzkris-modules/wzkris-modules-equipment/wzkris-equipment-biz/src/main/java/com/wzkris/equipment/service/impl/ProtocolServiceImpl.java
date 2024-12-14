package com.wzkris.equipment.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.equipment.domain.Product;
import com.wzkris.equipment.mapper.ProductMapper;
import com.wzkris.equipment.service.ProtocolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProtocolServiceImpl implements ProtocolService {
    private final ProductMapper productMapper;

    @Override
    public boolean checkProtocolUsed(Long ptcId) {
        return productMapper.exists(Wrappers.lambdaQuery(Product.class)
                .eq(Product::getPtcId, ptcId));
    }
}
