package com.wzkris.common.security.field;

import com.wzkris.common.security.field.annotation.FieldPerms;
import lombok.Data;

import java.util.List;

@Data
public class TestDomain {

    @FieldPerms(perms = FieldPerms.Perms.WRITE, value = "@LoginUserUtil.isSuperTenant()")
    private Long id;

    @FieldPerms(perms = FieldPerms.Perms.READ, value = "@LoginUserUtil.isSuperTenant()")
    private Boolean young;

    @FieldPerms(perms = FieldPerms.Perms.WRITE, value = "@LoginUserUtil.isSuperTenant()")
    private TestDefault testDefault;

    @FieldPerms(perms = FieldPerms.Perms.READ, value = "@LoginUserUtil.isSuperTenant()")
    private List<TestDefault> testDefaults;

}
