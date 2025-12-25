package com.wzkris.usercenter.controller.customer.statistic;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.usercenter.domain.req.customer.CustomerIncryQueryReq;
import com.wzkris.usercenter.domain.vo.customer.CustomerIncryVO;
import com.wzkris.usercenter.mapper.CustomerInfoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 统计
 *
 * @author wzkris
 */
@Tag(name = "客户统计")
@Validated
@RestController
@RequestMapping("/statistic")
@RequiredArgsConstructor
public class CustomerStatisticController extends BaseController {

    private final CustomerInfoMapper customerInfoMapper;

    @Operation(summary = "查询增长速度")
    @GetMapping("/customer/increase_rate")
    public Result<List<CustomerIncryVO>> increase_rate_per_day(@Valid CustomerIncryQueryReq queryReq) {
        return ok(customerInfoMapper.listIncryVO(queryReq));
    }

}
