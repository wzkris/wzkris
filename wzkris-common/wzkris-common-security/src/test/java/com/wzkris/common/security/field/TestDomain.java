package com.wzkris.common.security.field;

import com.wzkris.common.security.field.annotation.FieldPerms;
import com.wzkris.common.security.field.enums.FieldPerm;
import lombok.Data;

import java.util.List;

@Data
public class TestDomain {

    @FieldPerms(perm = FieldPerm.WRITE, value = "@SysUtil.isSuperTenant()")
    private Long id;

    @FieldPerms(perm = FieldPerm.READ, value = "@SysUtil.isSuperTenant()")
    private Boolean young;

    @FieldPerms(perm = FieldPerm.WRITE, value = "@SysUtil.isSuperTenant()")
    private TestDefault testDefault;

    @FieldPerms(perm = FieldPerm.READ, value = "@SysUtil.isSuperTenant()")
    private List<TestDefault> testDefaults;

}
