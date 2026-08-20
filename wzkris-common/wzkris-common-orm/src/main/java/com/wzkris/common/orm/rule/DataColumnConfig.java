package com.wzkris.common.orm.rule;

import com.wzkris.common.orm.annotation.DataPermission;

/**
 * 数据权限列配置，运行时传递给 {@link DataPermissionRule} 的值对象
 *
 * @author wzkris
 */
public record DataColumnConfig(String alias, String column) {

    /**
     * 从注解构建
     */
    public static DataColumnConfig of(DataPermission dp) {
        return new DataColumnConfig(dp.alias(), dp.column());
    }

    /**
     * 获取完整列名：alias.column 或 column
     */
    public String getFullColumn() {
        return (alias == null || alias.isBlank())
                ? column
                : alias + "." + column;
    }

}
