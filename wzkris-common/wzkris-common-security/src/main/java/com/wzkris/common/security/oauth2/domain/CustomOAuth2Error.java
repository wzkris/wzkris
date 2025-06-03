package com.wzkris.common.security.oauth2.domain;

import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.oauth2.constants.CustomErrorCodes;
import java.io.Serial;
import lombok.Getter;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.oauth2.core.OAuth2Error;

@Getter
public class CustomOAuth2Error extends OAuth2Error {

    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final int code;

    public CustomOAuth2Error(int code, String description) {
        this(code, null, description, null);
    }

    public CustomOAuth2Error(int code, String errorCode, String description, String uri) {
        super(StringUtil.isBlank(errorCode) ? CustomErrorCodes.DEFAULT_ERROR : errorCode, description, uri);
        this.code = code;
    }
}
