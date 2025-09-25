package com.wzkris.user.controller.customer;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.redis.annotation.GlobalCache;
import com.wzkris.common.redis.annotation.GlobalCacheEvict;
import com.wzkris.common.security.utils.LoginCustomerUtil;
import com.wzkris.user.domain.CustomerInfoDO;
import com.wzkris.user.domain.req.customer.CustomerInfoReq;
import com.wzkris.user.domain.vo.customer.CustomerInfoVO;
import com.wzkris.user.mapper.CustomerInfoMapper;
import com.wzkris.user.service.CustomerInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户个人信息
 *
 * @author wzkris
 */
@Tag(name = "客户信息")
@Slf4j
@Validated
@RestController
@RequestMapping("/customer-info")
@RequiredArgsConstructor
public class CustomerInfoController extends BaseController {

    private final String info_prefix = "customer-info";

    private final CustomerInfoMapper customerInfoMapper;

    private final CustomerInfoService customerInfoService;

    @Operation(summary = "获取信息")
    @GetMapping
    @GlobalCache(keyPrefix = info_prefix, key = "@cu.getId()", ttl = 3_600_000, sync = true)
    public Result<CustomerInfoVO> customerInfo() {
        CustomerInfoDO customerInfoDO = customerInfoMapper.selectById(LoginCustomerUtil.getId());

        CustomerInfoVO customerInfoVO = new CustomerInfoVO();
        customerInfoVO.setNickname(customerInfoDO.getNickname());
        customerInfoVO.setPhoneNumber(customerInfoDO.getPhoneNumber());
        customerInfoVO.setGender(customerInfoDO.getGender());
        customerInfoVO.setAvatar(customerInfoDO.getAvatar());

        return ok(customerInfoVO);
    }

    @Operation(summary = "修改信息")
    @PostMapping
    @GlobalCacheEvict(keyPrefix = info_prefix, key = "@cu.getId()")
    public Result<?> editInfo(@RequestBody CustomerInfoReq req) {
        CustomerInfoDO user = new CustomerInfoDO(LoginCustomerUtil.getId());
        user.setNickname(req.getNickname());
        user.setGender(req.getGender());
        return toRes(customerInfoMapper.updateById(user));
    }

    @Operation(summary = "更新头像")
    @PostMapping("/edit-avatar")
    @GlobalCacheEvict(keyPrefix = info_prefix, key = "@cu.getId()")
    public Result<?> editAvatar(@RequestBody String url) {
        CustomerInfoDO customerInfoDO = new CustomerInfoDO(LoginCustomerUtil.getId());
        customerInfoDO.setAvatar(url);
        return toRes(customerInfoMapper.updateById(customerInfoDO));
    }

}
