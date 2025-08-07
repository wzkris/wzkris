package com.wzkris.user.controller;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.security.utils.ClientUserUtil;
import com.wzkris.user.domain.AppUser;
import com.wzkris.user.domain.req.AppUserProfileReq;
import com.wzkris.user.domain.vo.AppUserProfileVO;
import com.wzkris.user.mapper.AppUserMapper;
import com.wzkris.user.service.AppUserService;
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
@Tag(name = "app个人页面信息")
@Slf4j
@Validated
@RestController
@RequestMapping("/app/user_profile")
@RequiredArgsConstructor
public class AppUserProfileController extends BaseController {

    private final String profile_prefix = "app_user:profile";

    private final AppUserMapper appUserMapper;

    private final AppUserService appUserService;

    @Operation(summary = "获取信息")
    @GetMapping
    @Cacheable(cacheNames = profile_prefix + "#3600#3600", key = "@cu.getUserId()")
    public Result<AppUserProfileVO> profile() {
        AppUser user = appUserMapper.selectById(ClientUserUtil.getUserId());

        AppUserProfileVO profileVO = new AppUserProfileVO();
        profileVO.setNickname(user.getNickname());
        profileVO.setPhoneNumber(user.getPhoneNumber());
        profileVO.setGender(user.getGender());
        profileVO.setAvatar(user.getAvatar());

        return ok(profileVO);
    }

    @Operation(summary = "修改信息")
    @PostMapping
    @CacheEvict(cacheNames = profile_prefix, key = "@cu.getUserId()")
    public Result<?> appuserInfo(@RequestBody AppUserProfileReq profileReq) {
        AppUser user = new AppUser(ClientUserUtil.getUserId());
        user.setNickname(profileReq.getNickname());
        user.setGender(profileReq.getGender());
        return toRes(appUserMapper.updateById(user));
    }

    @Operation(summary = "更新头像")
    @PostMapping("/edit_avatar")
    @CacheEvict(cacheNames = profile_prefix, key = "@cu.getUserId()")
    public Result<?> updateAvatar(@RequestBody String url) {
        AppUser appUser = new AppUser(ClientUserUtil.getUserId());
        appUser.setAvatar(url);
        return toRes(appUserMapper.updateById(appUser));
    }

}
