package com.wzkris.auth.remote.api.jwt.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceJwtIssueResponse {

    private String token;

    private OffsetDateTime expiresAt;

}
