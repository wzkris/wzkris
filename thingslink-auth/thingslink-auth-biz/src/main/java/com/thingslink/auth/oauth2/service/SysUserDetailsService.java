package com.thingslink.auth.oauth2.service;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.thingslink.auth.domain.SysUser;
import com.thingslink.auth.domain.bo.PermissionBO;
import com.thingslink.auth.mapper.SysUserMapper;
import com.thingslink.auth.service.PermissionService;
import com.thingslink.common.core.constant.CommonConstants;
import com.thingslink.common.core.utils.MapstructUtil;
import com.thingslink.common.security.model.LoginUser;
import com.thingslink.common.security.utils.OAuth2EndpointUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Service;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 查询系统用户信息
 * @date : 2024/2/26 10:03
 */
@Service
@AllArgsConstructor
public class SysUserDetailsService implements UserDetailsService {
    private final SysUserMapper sysUserMapper;
    private final PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = sysUserMapper.selectByUsername(username);
        return this.checkAndBuildLoginUser(user);
    }

    public UserDetails loadUserByPhoneNumber(String phoneNumber) throws UsernameNotFoundException {
        SysUser user = sysUserMapper.selectByPhoneNumber(phoneNumber);
        return this.checkAndBuildLoginUser(user);
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        SysUser user = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("email", email));
        return this.checkAndBuildLoginUser(user);
    }

    /**
     * 构建登录用户
     */
    private UserDetails checkAndBuildLoginUser(SysUser user) {
        // 校验用户状态
        this.checkAccount(user);
        // 获取权限信息
        PermissionBO permissions = permissionService.getPermission(user);

        LoginUser loginUser = MapstructUtil.convert(user, LoginUser.class);

        loginUser.setIsAdmin(permissions.getIsAdmin());
        loginUser.setAuthorities(AuthorityUtils.createAuthorityList(permissions.getGrantedAuthority()));
        loginUser.setDeptScopes(permissions.getDeptScopes());
        return loginUser;
    }

    /**
     * 校验用户账号
     */
    private void checkAccount(SysUser sysUser) {
        if (sysUser == null) {
            OAuth2EndpointUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.passlogin.fail");// 不能明说账号不存在
        }
        if (ObjUtil.equals(sysUser.getStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2EndpointUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");
        }
    }
}
