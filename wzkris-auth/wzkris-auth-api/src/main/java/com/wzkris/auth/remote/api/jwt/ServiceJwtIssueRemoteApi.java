package com.wzkris.auth.remote.api.jwt;

import com.wzkris.auth.remote.api.jwt.request.ServiceJwtIssueRequest;
import com.wzkris.auth.remote.api.jwt.response.ServiceJwtIssueResponse;
import com.wzkris.common.core.model.Result;

public interface ServiceJwtIssueRemoteApi {

    Result<ServiceJwtIssueResponse> issue(ServiceJwtIssueRequest request);

}
