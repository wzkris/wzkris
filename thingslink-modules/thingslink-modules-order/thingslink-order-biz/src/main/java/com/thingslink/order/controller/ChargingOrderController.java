package com.thingslink.order.controller;

import com.thingslink.common.core.domain.Result;
import com.thingslink.common.orm.page.Page;
import com.thingslink.common.web.controller.BaseController;
import com.thingslink.order.domain.ChargingOrder;
import com.thingslink.order.mapper.ChargingOrderMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单信息(ChargingOrder)表控制层
 *
 * @author wzkris
 * @since 2023-04-17 16:32:37
 */
@Tag(name = "充电订单管理")
@RestController
@RequestMapping("/charging_order")
@RequiredArgsConstructor
public class ChargingOrderController extends BaseController {
    private final ChargingOrderMapper chargingOrderMapper;

    /**
     * 分页查询
     *
     * @param order 筛选条件
     * @return 查询结果
     */
    @Operation(summary = "分页")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('order:list')")
    public Result<Page<ChargingOrder>> listPage(ChargingOrder order) {
        startPage();
        List<ChargingOrder> list = chargingOrderMapper.list(order);
        return getDataTable(list);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param chargingId 主键
     * @return 单条数据
     */
    @GetMapping("/{chargingId}")
    @PreAuthorize("hasAuthority('order:query')")
    public Result<ChargingOrder> query(@PathVariable Long chargingId) {
        return success(chargingOrderMapper.getById(chargingId));
    }

    /**
     * 删除数据
     *
     * @param chargingId 主键
     * @return 删除是否成功
     */
    @DeleteMapping("/{chargingId}")
    @PreAuthorize("hasAuthority('order:remove')")
    public Result<?> deleteById(@PathVariable Long chargingId) {
        return toRes(chargingOrderMapper.deleteById(chargingId));
    }
}
