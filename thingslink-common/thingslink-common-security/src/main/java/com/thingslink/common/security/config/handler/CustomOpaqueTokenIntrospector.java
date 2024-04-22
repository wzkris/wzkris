package com.thingslink.common.security.config.handler;

import com.thingslink.auth.api.RemoteTokenApi;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.core.utils.json.JsonUtil;
import com.thingslink.common.security.model.AbstractUser;
import com.thingslink.common.security.model.AppUser;
import com.thingslink.common.security.model.LoginUser;
import com.thingslink.common.security.utils.CurrentUserHolder;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import java.util.LinkedHashMap;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 自定义token自省
 * @date : 2024/3/8 14:34.
 */
@AllArgsConstructor
public class CustomOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final RemoteTokenApi remoteTokenApi;

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        // 已认证则直接返回
        if (CurrentUserHolder.isAuthenticated()) {
            return CurrentUserHolder.getPrincipal();
        }

        // 否则校验token
        Result<Object> objectResult = remoteTokenApi.findByToken(token);
        LinkedHashMap<String, Object> resMap = (LinkedHashMap<String, Object>) objectResult.checkData();

        return buildAuthenticatedUser(resMap);
    }

    /**
     * 构建登录用户信息
     */
    public static AbstractUser buildAuthenticatedUser(LinkedHashMap<String, Object> resMap) {
        if (resMap == null || resMap.get("userType") == null) {
            throw new InvalidBearerTokenException(OAuth2ErrorCodes.INVALID_TOKEN);
        }

        String userType = resMap.get("userType").toString();

        if (StringUtil.equals(userType, AppUser.USER_TYPE)) {
            return JsonUtil.parseObject(resMap, AppUser.class);
        }

        if (StringUtil.equals(userType, LoginUser.USER_TYPE)) {
            return JsonUtil.parseObject(resMap, LoginUser.class);
        }

        // 返回空即为未认证
        return null;
    }

}
