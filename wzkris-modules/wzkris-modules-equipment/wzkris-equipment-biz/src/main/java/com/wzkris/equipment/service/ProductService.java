package com.wzkris.equipment.service;

import com.wzkris.equipment.domain.Product;

/**
 * (product)表控制层
 *
 * @author wzkris
 * @since 2024-12-04 14:40:00
 */
public interface ProductService {

    /**
     * 添加设备
     *
     * @param product 参数
     * @return 是否成功
     */
    boolean insertProduct(Product product);

    /**
     * @param pdtId 产品ID
     * @return 是否被设备使用
     */
    boolean checkDeviceUsed(Long pdtId);

    /**
     * @param pdtId 产品ID
     * @return 是否被物模型使用
     */
    boolean checkThingsModelUsed(Long pdtId);

}
