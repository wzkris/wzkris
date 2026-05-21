package com.wzkris.auth.remote.controller.jwt;

import com.wzkris.auth.remote.api.jwt.ServiceJwtIssueRemoteApi;
import com.wzkris.auth.remote.api.jwt.request.ServiceJwtIssueRequest;
import com.wzkris.auth.remote.api.jwt.response.ServiceJwtIssueResponse;
import com.wzkris.common.core.model.Result;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/service-jwt-remote")
@RequiredArgsConstructor
public class ServiceJwtIssueRemoteController {

    private final ServiceJwtIssueRemoteApi serviceJwtIssueRemoteApi;

    @PostMapping("/issue")
    public Result<ServiceJwtIssueResponse> issue(@RequestBody @Valid ServiceJwtIssueRequest request) {
        return serviceJwtIssueRemoteApi.issue(request);
    }

}
