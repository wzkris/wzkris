package com.wzkris.user.controller;

import cn.hutool.core.map.MapUtil;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.security.utils.AppUtil;
import com.wzkris.user.api.domain.dto.AppUserDTO;
import com.wzkris.user.domain.AppUser;
import com.wzkris.user.mapper.AppUserMapper;
import com.wzkris.user.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户个人信息
 *
 * @author wzkris
 */
@Tag(name = "app个人中心")
@Validated
@RestController
@RequestMapping("/app_user/personal_center")
@RequiredArgsConstructor
public class AppProfileController extends BaseController {

    private final AppUserMapper appUserMapper;
    private final AppUserService appUserService;

    @Operation(summary = "个人信息")
    @GetMapping
    public Result<?> info() {
        AppUser appUser = appUserMapper.selectById(AppUtil.getUserId());
        Map<String, Object> userMap = MapUtil.newHashMap(4);
        userMap.put(AppUser.Fields.nickname, appUser.getNickname());
        userMap.put(AppUser.Fields.phoneNumber, appUser.getPhoneNumber());
        userMap.put(AppUser.Fields.gender, appUser.getGender());
        userMap.put(AppUser.Fields.avatar, appUser.getAvatar());
        // 返回的map
        Map<String, Object> res = MapUtil.newHashMap(4);
        res.put("user", userMap);
        return success(res);
    }

    @Operation(summary = "修改昵称、性别")
    @PostMapping
    public Result<?> editInfo(@RequestBody AppUserDTO appUserDTO) {
        AppUser user = new AppUser(AppUtil.getUserId());
        user.setNickname(appUserDTO.getNickname());
        user.setGender(appUserDTO.getGender());
        return toRes(appUserMapper.updateById(user));
    }

    @Operation(summary = "更新头像")
    @PostMapping("/edit_avatar")
    public Result<?> updateAvatar(@RequestBody String url) {
        AppUser appUser = new AppUser(AppUtil.getUserId());
        appUser.setAvatar(url);
        return toRes(appUserMapper.updateById(appUser));
    }
}
