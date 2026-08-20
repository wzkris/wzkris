package com.wzkris.auth.domain;

import com.wzkris.common.core.model.DefaultLoginUser;
import com.wzkris.common.core.model.RoleContext;

public record UserContext(DefaultLoginUser loginUser, RoleContext roleContext) {

}