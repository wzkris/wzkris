package com.wzkris.equipment.service;

/**
 * (product)表控制层
 *
 * @author wzkris
 * @since 2024-12-04 14:40:00
 */
public interface ProductService {

    /**
     * @param pdtId 产品ID
     * @return 是否使用
     */
    boolean checkProductUsed(Long pdtId);
}
