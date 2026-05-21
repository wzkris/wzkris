package com.wzkris.auth.remote.impl.jwt;

import com.wzkris.auth.remote.api.jwt.ServiceJwtIssueRemoteApi;
import com.wzkris.auth.remote.api.jwt.request.ServiceJwtIssueRequest;
import com.wzkris.auth.remote.api.jwt.response.ServiceJwtIssueResponse;
import com.wzkris.auth.utils.JwtTokenHelper;
import com.wzkris.common.core.model.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
public class ServiceJwtIssueRemoteApiImpl implements ServiceJwtIssueRemoteApi {

    private final JwtTokenHelper jwtTokenHelper;

    @Override
    public Result<ServiceJwtIssueResponse> issue(ServiceJwtIssueRequest request) {
        String token = jwtTokenHelper.issueServiceToken(
                request.getSubject(),
                request.getClaims(),
                request.getTtlSeconds());
        var claims = jwtTokenHelper.parse(token);
        return Result.ok(new ServiceJwtIssueResponse(
                token,
                claims.getExpiresAt().atOffset(ZoneOffset.UTC)));
    }

}
