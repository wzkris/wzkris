package com.thingslink.auth.oauth2.service;

import cn.hutool.core.util.ObjUtil;
import com.thingslink.auth.config.TokenConfig;
import com.thingslink.auth.oauth2.model.UserModel;
import com.thingslink.auth.oauth2.redis.JdkRedisUtil;
import com.thingslink.common.core.constant.CommonConstants;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.core.utils.json.JsonUtil;
import com.thingslink.common.security.oauth2.model.LoginSysUser;
import com.thingslink.common.security.utils.OAuth2ExceptionUtil;
import com.thingslink.user.api.RemoteSysUserApi;
import com.thingslink.user.api.domain.dto.SysPermissionDTO;
import com.thingslink.user.api.domain.dto.SysUserDTO;
import com.thingslink.user.api.domain.vo.RouterVO;
import lombok.AllArgsConstructor;
import org.redisson.api.RBucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 查询系统用户信息
 * @date : 2024/2/26 10:03
 */
@Service
@AllArgsConstructor
public class SysUserDetailsService implements UserDetailsServicePlus {
    private static final String ROUTER_PREFIX = "router";
    private static final Logger log = LoggerFactory.getLogger(SysUserDetailsService.class);
    private final TokenConfig tokenConfig;
    private final RemoteSysUserApi remoteSysUserApi;

    @Override
    public UserModel loadUserByUsername(String username) throws UsernameNotFoundException {
        Result<SysUserDTO> result = remoteSysUserApi.getByUsername(username);
        SysUserDTO sysUserDTO = result.checkData();
        return this.checkAndBuildLoginUser(sysUserDTO);
    }

    /**
     * 获取前端路由，缓存路由结果
     */
    public List<RouterVO> getRouter(Long userId) {
        RBucket<List<RouterVO>> bucket = JdkRedisUtil.getRedissonClient().getBucket(this.buildRouterKey(userId));
        if (bucket.get() == null) {
            Result<List<RouterVO>> listResult = remoteSysUserApi.getRouter(userId);
            List<RouterVO> routerVOS = listResult.checkData();
            bucket.set(routerVOS, Duration.ofSeconds(tokenConfig.getAccessTokenTimeOut()));
        }
        return bucket.get();
    }

    public void setRouter(Long userId) {
        Result<List<RouterVO>> listResult = remoteSysUserApi.getRouter(userId);
        JdkRedisUtil.getRedissonClient().getBucket(this.buildRouterKey(userId)).set(listResult.checkData(), Duration.ofSeconds(tokenConfig.getAccessTokenTimeOut()));
    }

    /**
     * 构建登录用户
     */
    private UserModel checkAndBuildLoginUser(SysUserDTO sysUserDTO) {
        // 校验用户状态
        this.checkAccount(sysUserDTO);

        // 获取权限信息
        Result<SysPermissionDTO> permissionDTOResult = remoteSysUserApi.getPermission(sysUserDTO.getUserId(), sysUserDTO.getTenantId(), sysUserDTO.getDeptId());
        SysPermissionDTO permissions = permissionDTOResult.checkData();

        LoginSysUser loginUser = new LoginSysUser();
        loginUser.setUserId(sysUserDTO.getUserId());
        loginUser.setDeptId(sysUserDTO.getDeptId());
        loginUser.setTenantId(sysUserDTO.getTenantId());
        loginUser.setUsername(sysUserDTO.getUsername());
        loginUser.setPassword(sysUserDTO.getPassword());
        loginUser.setIsAdmin(permissions.getIsAdmin());
        loginUser.setDeptScopes(permissions.getDeptScopes());

        return new UserModel(sysUserDTO.getUsername(),
                sysUserDTO.getPassword(), permissions.getGrantedAuthority(), JsonUtil.parseObject(loginUser, HashMap.class));
    }

    /**
     * 校验用户账号
     */
    private void checkAccount(SysUserDTO sysUserDTO) {
        if (sysUserDTO == null) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");// 不能明说账号不存在
        }
        if (ObjUtil.equals(sysUserDTO.getStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");
        }
    }

    // 构建用户路由KEY
    private String buildRouterKey(Long userId) {
        return String.format("%s:%s", ROUTER_PREFIX, userId);
    }
}
