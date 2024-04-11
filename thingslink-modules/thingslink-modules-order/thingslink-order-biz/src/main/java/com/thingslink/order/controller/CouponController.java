package com.thingslink.order.controller;

import com.thingslink.common.core.domain.Result;
import com.thingslink.common.orm.page.Page;
import com.thingslink.common.orm.model.BaseController;
import com.thingslink.order.domain.Coupon;
import com.thingslink.order.mapper.CouponMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 优惠券信息(Coupon)表控制层
 *
 * @author wzkris
 * @since 2023-04-17 16:35:17
 */
@Tag(name = "优惠券管理")
@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController extends BaseController {

    private final CouponMapper couponMapper;

    /**
     * 分页查询
     *
     * @param coupon 筛选条件
     * @return 查询结果
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('coupon:list')")
    public Result<Page<Coupon>> listPage(Coupon coupon) {
        startPage();
        List<Coupon> list = couponMapper.list(coupon);
        return getDataTable(list);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param couponId 主键
     * @return 单条数据
     */
    @GetMapping("/{couponId}")
    @PreAuthorize("hasAuthority('coupon:query')")
    public Result<Coupon> query(@PathVariable Long couponId) {
        return success(couponMapper.getById(couponId));
    }

    /**
     * 新增数据
     *
     * @param coupon 实体
     * @return 新增结果
     */
    @PostMapping
    @PreAuthorize("hasAuthority('coupon:add')")
    public Result<?> add(@RequestBody Coupon coupon) {
        return toRes(couponMapper.insert(coupon));
    }

    /**
     * 编辑数据
     *
     * @param coupon 实体
     * @return 编辑结果
     */
    @PutMapping
    @PreAuthorize("hasAuthority('coupon:edit')")
    public Result<?> edit(@RequestBody Coupon coupon) {
        return toRes(couponMapper.updateById(coupon));
    }

    /**
     * 删除数据
     *
     * @param couponId 主键
     * @return 删除是否成功
     */
    @DeleteMapping("/{couponId}")
    @PreAuthorize("hasAuthority('coupon:remove')")
    public Result<?> deleteById(@PathVariable Long couponId) {
        return toRes(couponMapper.deleteById(couponId));
    }
}
