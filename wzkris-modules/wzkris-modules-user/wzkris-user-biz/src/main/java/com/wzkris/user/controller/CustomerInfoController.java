package com.wzkris.user.controller;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.redis.annotation.GlobalCache;
import com.wzkris.common.security.utils.LoginCustomerUtil;
import com.wzkris.user.domain.CustomerInfoDO;
import com.wzkris.user.domain.req.CustomerInfoReq;
import com.wzkris.user.domain.vo.CustomerInfoVO;
import com.wzkris.user.mapper.CustomerInfoMapper;
import com.wzkris.user.service.CustomerInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
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

    private final String profile_prefix = "customer_info:profile";

    private final CustomerInfoMapper customerInfoMapper;

    private final CustomerInfoService customerInfoService;

    @Operation(summary = "获取信息")
    @GetMapping
    @GlobalCache(keyPrefix = profile_prefix, key = "@cu.getId()", ttl = 3_600_000, sync = true)
    public Result<CustomerInfoVO> profile() {
        CustomerInfoDO user = customerInfoMapper.selectById(LoginCustomerUtil.getId());

        CustomerInfoVO profileVO = new CustomerInfoVO();
        profileVO.setNickname(user.getNickname());
        profileVO.setPhoneNumber(user.getPhoneNumber());
        profileVO.setGender(user.getGender());
        profileVO.setAvatar(user.getAvatar());

        return ok(profileVO);
    }

    @Operation(summary = "修改信息")
    @PostMapping
    @CacheEvict(cacheNames = profile_prefix, key = "@cu.id()")
    public Result<?> appuserInfo(@RequestBody CustomerInfoReq profileReq) {
        CustomerInfoDO user = new CustomerInfoDO(LoginCustomerUtil.getId());
        user.setNickname(profileReq.getNickname());
        user.setGender(profileReq.getGender());
        return toRes(customerInfoMapper.updateById(user));
    }

    @Operation(summary = "更新头像")
    @PostMapping("/edit-avatar")
    @CacheEvict(cacheNames = profile_prefix, key = "@cu.id()")
    public Result<?> updateAvatar(@RequestBody String url) {
        CustomerInfoDO customerInfoDO = new CustomerInfoDO(LoginCustomerUtil.getId());
        customerInfoDO.setAvatar(url);
        return toRes(customerInfoMapper.updateById(customerInfoDO));
    }

}
