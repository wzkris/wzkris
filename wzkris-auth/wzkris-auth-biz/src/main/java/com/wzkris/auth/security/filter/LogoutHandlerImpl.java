package com.wzkris.auth.security.filter;

import com.wzkris.auth.domain.OnlineUser;
import com.wzkris.auth.utils.OnlineUserUtil;
import com.wzkris.common.core.domain.CorePrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.redisson.api.RMapCache;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

/**
 * 退出登录
 *
 * @author wzkris
 */
public class LogoutHandlerImpl implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof CorePrincipal principal) {
            RMapCache<String, OnlineUser> onlineCache = OnlineUserUtil.getOnlineCache(principal.getId());
            onlineCache.remove(principal.getId());
        }

    }

}
