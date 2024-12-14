package com.wzkris.equipment.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.equipment.domain.Product;
import org.springframework.stereotype.Repository;


/**
 * (Product)表数据库访问层
 *
 * @author wzkris
 * @since 2024-12-04 14:40:00
 */
@Repository
public interface ProductMapper extends BaseMapperPlus<Product> {
}
