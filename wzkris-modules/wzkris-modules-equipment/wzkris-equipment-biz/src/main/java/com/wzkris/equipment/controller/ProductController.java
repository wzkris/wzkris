package com.wzkris.equipment.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.BeanUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.security.oauth2.annotation.CheckPerms;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.equipment.domain.Product;
import com.wzkris.equipment.domain.req.ProductQueryReq;
import com.wzkris.equipment.domain.req.ProductReq;
import com.wzkris.equipment.mapper.ProductMapper;
import com.wzkris.equipment.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @CheckPerms("product:list")
    public Result<Page<Product>> listPage(ProductQueryReq queryReq) {
        startPage();
        List<Product> list = productMapper.selectList(this.buildQueryWrapper(queryReq));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<Product> buildQueryWrapper(ProductQueryReq queryReq) {
        return new LambdaQueryWrapper<Product>()
                .like(StringUtil.isNotBlank(queryReq.getPdtName()), Product::getPdtName, queryReq.getPdtName())
                .eq(StringUtil.isNotBlank(queryReq.getPdtType()), Product::getPdtType, queryReq.getPdtType())
                .orderByDesc(Product::getPdtId);
    }

    @Operation(summary = "产品选择列表(不带分页)")
    @GetMapping("/selectlist")
    @CheckPerms(value = {"device:add", "device:edit"}, mode = CheckPerms.Mode.OR)// 设备添加修改时使用
    public Result<List<Product>> selectList(ProductQueryReq queryReq) {
        LambdaQueryWrapper<Product> lqw = this.buildQueryWrapper(queryReq)
                .select(Product::getPdtId, Product::getPdtName);
        List<Product> list = productMapper.selectList(lqw);
        return ok(list);
    }

    @Operation(summary = "id查询信息")
    @GetMapping("/{pdtId}")
    @CheckPerms("product:query")
    public Result<Product> queryById(@PathVariable Long pdtId) {
        return ok(productMapper.selectById(pdtId));
    }

    @Operation(summary = "添加产品")
    @OperateLog(title = "产品管理", subTitle = "添加产品", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckPerms("product:add")
    public Result<Void> add(@RequestBody ProductReq req) {
        return toRes(productService.insertProduct(BeanUtil.convert(req, Product.class)));
    }

    @Operation(summary = "修改产品")
    @OperateLog(title = "产品管理", subTitle = "修改产品", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckPerms("product:edit")
    public Result<Void> edit(@RequestBody ProductReq req) {
        return toRes(productMapper.updateById(BeanUtil.convert(req, Product.class)));
    }

    @Operation(summary = "删除产品")
    @OperateLog(title = "产品管理", subTitle = "删除产品", operateType = OperateType.UPDATE)
    @PostMapping("/remove")
    @CheckPerms("product:remove")
    public Result<Void> deleteById(@RequestBody Long pdtId) {
        if (productService.checkDeviceUsed(pdtId)) {
            return error412("删除失败，该产品与设备关联使用中");
        } else if (productService.checkThingsModelUsed(pdtId)) {
            return error412("删除失败，该产品与物模型关联使用中");
        }
        return toRes(productMapper.deleteById(pdtId));
    }

}
