package com.wzkris.common.security.serialize;

import com.fasterxml.jackson.annotation.*;

import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
abstract class TokenSettingsMixin {

    @JsonCreator
    public TokenSettingsMixin(@JsonProperty("settings") Map<String, Object> settings) {
    }
}
