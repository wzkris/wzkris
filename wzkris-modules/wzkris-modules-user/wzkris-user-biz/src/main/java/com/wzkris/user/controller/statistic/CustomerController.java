package com.wzkris.user.controller.statistic;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.domain.req.AppUserIncryQueryReq;
import com.wzkris.user.domain.vo.AppUserIncryVO;
import com.wzkris.user.mapper.AppUserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统计
 *
 * @author wzkris
 */
@Tag(name = "用户统计")
@Validated
@RestController
@RequestMapping("/statistic")
@RequiredArgsConstructor
public class CustomerController extends BaseController {

    private final AppUserMapper appUserMapper;

    @Operation(summary = "查询增长速度")
    @GetMapping("/customer/increase_rate")
    public Result<List<AppUserIncryVO>> increase_rate_per_day(@Valid AppUserIncryQueryReq queryReq) {
        return ok(appUserMapper.listIncryVO(queryReq));
    }
}
