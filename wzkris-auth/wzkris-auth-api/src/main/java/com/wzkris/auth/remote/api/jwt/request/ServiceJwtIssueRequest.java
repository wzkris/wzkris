package com.wzkris.auth.remote.api.jwt.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class ServiceJwtIssueRequest {

    @NotBlank
    private String subject;

    private Map<String, Object> claims;

    @NotNull
    @Min(60)
    @Max(86400)
    private Long ttlSeconds;

}
