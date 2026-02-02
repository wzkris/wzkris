package com.wzkris.auth.controller;

import com.wzkris.auth.domain.OnlineSession;
import com.wzkris.auth.domain.vo.OnlineSessionVO;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.wzkris.common.core.model.Result.ok;

@Tag(name = "在线会话")
@Slf4j
@RestController
@RequestMapping("/online-session")
@RequiredArgsConstructor
public class OnlineSessionController {

    private final TokenService tokenService;

    /**
     * 获取在线会话列表
     *
     * @return 在线会话列表
     */
    @Operation(summary = "在线会话")
    @GetMapping
    public Result<Collection<OnlineSessionVO>> onlineSession() {
        AuthTypeEnum authType = SecurityUtil.getAuthType();
        Map<String, OnlineSession> onlineCache = tokenService.loadSessionCache(authType.getValue(), SecurityUtil.getUid());

        String sid = tokenService.parseJwt(SecurityUtil.getAuthentication().getCredentials().toString()).getSid();

        List<OnlineSessionVO> resps = new ArrayList<>();
        for (Map.Entry<String, OnlineSession> entry : onlineCache.entrySet()) {
            String sessionSid = entry.getKey();
            OnlineSessionVO userResp = new OnlineSessionVO(entry.getValue());
            userResp.setSid(sessionSid);
            if (StringUtil.equals(sid, sessionSid)) {
                userResp.setCurrent(true);
            }
            resps.add(userResp);
        }

        return ok(resps);
    }

    /**
     * 踢出指定会话（仅删除会话信息，不拉黑sid）
     *
     * @param sid 会话ID
     * @return 操作结果
     */
    @Operation(summary = "踢出会话")
    @PostMapping("/kickout")
    public Result<Void> kickoutSession(@RequestBody String sid) {
        AuthTypeEnum authType = SecurityUtil.getAuthType();
        tokenService.revoke(authType.getValue(), SecurityUtil.getUid(), sid);
        return ok();
    }

}
