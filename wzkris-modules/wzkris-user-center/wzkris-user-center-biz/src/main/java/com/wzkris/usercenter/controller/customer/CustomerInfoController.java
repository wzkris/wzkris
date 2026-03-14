package com.wzkris.usercenter.controller.customer;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.usercenter.domain.CustomerInfoDO;
import com.wzkris.usercenter.domain.req.customer.CustomerInfoReq;
import com.wzkris.usercenter.domain.resp.customer.CustomerInfoResp;
import com.wzkris.usercenter.mapper.CustomerInfoMapper;
import com.wzkris.usercenter.service.CustomerInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = info_prefix + "#3_600_000", key = "@su.getUid()", sync = true)
    public Result<CustomerInfoResp> customerInfo() {
        CustomerInfoDO customerInfoDO = customerInfoMapper.selectById(SecurityUtil.getUid());

        CustomerInfoResp customerInfoVO = new CustomerInfoResp();
        customerInfoVO.setNickname(customerInfoDO.getNickname());
        customerInfoVO.setPhoneNumber(customerInfoDO.getPhoneNumber());
        customerInfoVO.setGender(customerInfoDO.getGender());
        customerInfoVO.setAvatar(customerInfoDO.getAvatar());
        customerInfoVO.setLoginDate(customerInfoDO.getLoginDate());

        return ok(customerInfoVO);
    }

    @Operation(summary = "修改信息")
    @PostMapping
    @CacheEvict(value = info_prefix, key = "@su.getUid()")
    public Result<?> editInfo(@RequestBody CustomerInfoReq req) {
        CustomerInfoDO customer = new CustomerInfoDO(SecurityUtil.getUid());
        customer.setNickname(req.getNickname());
        customer.setGender(req.getGender());
        return toRes(customerInfoMapper.updateById(customer));
    }

    @Operation(summary = "更新头像")
    @PostMapping("/edit-avatar")
    @CacheEvict(value = info_prefix, key = "@su.getUid()")
    public Result<?> editAvatar(@RequestBody String url) {
        CustomerInfoDO customerInfoDO = new CustomerInfoDO(SecurityUtil.getUid());
        customerInfoDO.setAvatar(url);
        return toRes(customerInfoMapper.updateById(customerInfoDO));
    }

}
