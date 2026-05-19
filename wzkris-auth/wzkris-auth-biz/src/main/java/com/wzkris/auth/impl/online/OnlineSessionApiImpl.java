package com.wzkris.auth.impl.online;

import com.wzkris.auth.api.online.OnlineSessionApi;
import com.wzkris.auth.api.online.request.SidRequest;
import com.wzkris.auth.api.online.response.OnlineSessionResponse;
import com.wzkris.auth.domain.OnlineSession;
import com.wzkris.auth.domain.TokenClaims;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.utils.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.wzkris.common.core.model.Result.ok;

@Service
@RequiredArgsConstructor
public class OnlineSessionApiImpl implements OnlineSessionApi {

    private final TokenService tokenService;

    @Override
    public Result<Collection<OnlineSessionResponse>> queryList() {
        AuthTypeEnum authType = SecurityUtil.getAuthType();
        Map<String, OnlineSession> onlineCache = tokenService.loadSessionCache(authType.getValue(), SecurityUtil.getUid());

        TokenClaims claims = tokenService.parseJwt(SecurityUtil.getTokenValue());
        String sid = claims.getSid();

        List<OnlineSessionResponse> list = new ArrayList<>();
        for (Map.Entry<String, OnlineSession> entry : onlineCache.entrySet()) {
            String sessionSid = entry.getKey();
            OnlineSessionResponse sessionResp = new OnlineSessionResponse();
            BeanUtil.convert(entry.getValue(), sessionResp);
            sessionResp.setSid(sessionSid);
            if (StringUtil.equals(sid, sessionSid)) {
                sessionResp.setCurrent(true);
            }
            list.add(sessionResp);
        }

        return ok(list);
    }

    @Override
    public Result<Void> kickout(SidRequest request) {
        String sid = request.getSid();
        BaseLoginUser loginUser = SecurityUtil.getLoginUser();
        tokenService.revoke(loginUser.getAuthType().getValue(), loginUser.getUid(), sid);
        return ok();
    }

}
