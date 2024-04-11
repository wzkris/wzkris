package com.thingslink.common.orm.plus.enums;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 方法枚举
 * @date : 2024/1/4 13:19
 */
public enum SqlMethod {
    UPDATE_BATCH_BY_IDS("updateBatchByIds", "批量更新", "<script>\nUPDATE %s %s WHERE %s IN (%s)\n</script>"),
    INSERT_BATCH("insertBatch", "批量插入", "<script>\nINSERT INTO %s (%s) VALUES %s\n</script>");


    private final String method;
    private final String desc;
    private final String sql;

    SqlMethod(String method, String desc, String sql) {
        this.method = method;
        this.desc = desc;
        this.sql = sql;
    }

    public String getMethod() {
        return method;
    }

    public String getDesc() {
        return desc;
    }

    public String getSql() {
        return sql;
    }
}
