package com.wzkris.common.core.model.domain;

import com.wzkris.common.core.enums.AuthType;
import com.wzkris.common.core.model.CorePrincipal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.Set;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 租户员工
 * @date : 2025/10/9 13:30
 */
@Getter
@Setter
@ToString
public class LoginStaff extends CorePrincipal {

    private boolean admin;

    private String staffName;

    private Long tenantId;

    public LoginStaff() {
        this(null);
    }

    public LoginStaff(Long staffId) {
        this(staffId, Collections.emptySet());
    }

    public LoginStaff(Long staffId, Set<String> permissions) {
        super(staffId, AuthType.STAFF.getValue(), permissions);
    }

    @Override
    public String getName() {
        return this.getStaffName();
    }

}
