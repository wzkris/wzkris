package com.wzkris.common.log.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务操作类型
 *
 * @author wzkris
 */
@Getter
@AllArgsConstructor
public enum OperateType {
    /**
     * 其它
     */
    OTHER("0"),

    /**
     * 新增
     */
    INSERT("1"),

    /**
     * 修改
     */
    UPDATE("2"),

    /**
     * 删除
     */
    DELETE("3"),

    /**
     * 授权
     */
    GRANT("4"),

    /**
     * 导出
     */
    EXPORT("5"),

    /**
     * 导入
     */
    IMPORT("6");

    private final String value;
}
