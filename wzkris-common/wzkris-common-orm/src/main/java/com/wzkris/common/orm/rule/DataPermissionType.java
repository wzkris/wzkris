package com.wzkris.common.orm.rule;

/**
 * 数据权限规则类型枚举
 * <p>
 * 新增维度时在此枚举添加值，并实现对应的 {@link DataPermissionRule}。
 *
 * @author wzkris
 */
public enum DataPermissionType {

    /**
     * 部门数据权限
     */
    DEPT("dept"),

    ;

    private final String code;

    DataPermissionType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
