package com.thingslink.order.service.impl;

import com.thingslink.order.mapper.CouponMapper;
import com.thingslink.order.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 优惠券信息(Coupon)表服务实现类
 *
 * @author wzkris
 * @since 2023-04-17 16:35:18
 */
@RequiredArgsConstructor
@Service("couponService")
public class CouponServiceImpl implements CouponService {

    private final CouponMapper couponMapper;

}
