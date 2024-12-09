package com.wzkris.equipment.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.equipment.domain.Product;
import com.wzkris.equipment.domain.req.EditStatusReq;
import com.wzkris.equipment.domain.req.ProductQueryReq;
import com.wzkris.equipment.mapper.ProductMapper;
import com.wzkris.equipment.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * (product)表控制层
 *
 * @author wzkris
 * @since 2024-12-04 14:40:00
 */
@Tag(name = "产品管理")
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController extends BaseController {
    private final ProductMapper productMapper;
    private final ProductService productService;

    @Operation(summary = "分页查询")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('product:list')")
    public Result<Page<Product>> listPage(ProductQueryReq req) {
        startPage();
        List<Product> list = productMapper.selectList(this.buildQueryWrapper(req));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<Product> buildQueryWrapper(ProductQueryReq req) {
        return new LambdaQueryWrapper<Product>()
                .like(StringUtil.isNotBlank(req.getPdtName()), Product::getPdtName, req.getPdtName())
                .eq(StringUtil.isNotBlank(req.getStatus()), Product::getStatus, req.getStatus())
                .eq(StringUtil.isNotBlank(req.getPdtType()), Product::getPdtType, req.getPdtType())
                .orderByDesc(Product::getPdtId);
    }

    @Operation(summary = "产品选择列表(不带分页)")
    @GetMapping("/selectlist")
    @PreAuthorize("@ps.hasPerms('product:list')")
    public Result<List<Product>> selectList(String pdtName) {
        LambdaQueryWrapper<Product> lqw = new LambdaQueryWrapper<Product>()
                .select(Product::getPdtId, Product::getPdtName)
                .eq(Product::getStatus, CommonConstants.STATUS_ENABLE)
                .like(StringUtil.isNotBlank(pdtName), Product::getPdtName, pdtName);
        List<Product> list = productMapper.selectList(lqw);
        return ok(list);
    }

    @Operation(summary = "产品模型选择")
    @GetMapping("/model_list")
    @PreAuthorize("@ps.hasPerms('product:list')")
    public Result<Map<String, Object>> modelList() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(Product.ChargePileModel.type, new Product.ChargePileModel());
        map.put(Product.MonitorModel.type, new Product.MonitorModel());
        return ok(map);
    }

    @Operation(summary = "id查询信息")
    @GetMapping("/{pdtId}")
    @PreAuthorize("@ps.hasPerms('product:query')")
    public Result<Product> queryById(@PathVariable Long pdtId) {
        return ok(productMapper.selectById(pdtId));
    }

    @Operation(summary = "添加产品")
    @OperateLog(title = "产品管理", subTitle = "添加产品", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('product:add')")
    public Result<Void> add(@RequestBody Product product) {
        return toRes(productMapper.insert(product));
    }

    @Operation(summary = "修改产品")
    @OperateLog(title = "产品管理", subTitle = "修改产品", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('product:edit')")
    public Result<Void> edit(@RequestBody Product product) {
        return toRes(productMapper.updateById(product));
    }

    @Operation(summary = "修改产品状态")
    @OperateLog(title = "产品管理", subTitle = "修改产品状态", operateType = OperateType.UPDATE)
    @PostMapping("/edit_status")
    @PreAuthorize("@ps.hasPerms('product:edit')")
    public Result<Void> editStatus(@RequestBody EditStatusReq statusReq) {
        Product update = new Product(statusReq.getId());
        update.setStatus(statusReq.getStatus());
        return toRes(productMapper.updateById(update));
    }

    @Operation(summary = "删除产品")
    @OperateLog(title = "产品管理", subTitle = "删除产品", operateType = OperateType.UPDATE)
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('product:remove')")
    public Result<Void> deleteById(@RequestBody Long pdtId) {
        if (productService.checkProductUsed(pdtId)) {
            return fail("删除失败，该产品与设备关联使用中");
        }
        return toRes(productMapper.deleteById(pdtId));
    }

}
