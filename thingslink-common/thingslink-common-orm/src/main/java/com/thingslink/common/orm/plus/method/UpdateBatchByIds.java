package com.thingslink.common.orm.plus.method;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.thingslink.common.orm.plus.enums.SqlMethod;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 批量更新实体集合，根据pk判断
 * @date : 2024/1/4 11:11
 */
public class UpdateBatchByIds extends AbstractMethod {

    public UpdateBatchByIds() {
        this(SqlMethod.UPDATE_BATCH_BY_IDS.getMethod());
    }

    /**
     * @param methodName 方法名
     * @since 3.5.0
     */
    public UpdateBatchByIds(String methodName) {
        super(methodName);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql;
        SqlMethod sqlMethod = SqlMethod.UPDATE_BATCH_BY_IDS;

        // 拼接case when条件
        StringBuilder sqlScript = new StringBuilder();
        for (TableFieldInfo f : tableInfo.getFieldList()) {
            sqlScript.append(
                    SqlScriptUtils.convertTrim(
                            SqlScriptUtils.convertForeach(
                                    SqlScriptUtils.convertIf(
                                            "WHEN #{item." + tableInfo.getKeyProperty() + "} THEN #{item." + f.getProperty() + "}",
                                            "item." + f.getProperty() + " != null",
                                            true
                                    ),
                                    COLL,
                                    null,
                                    "item",
                                    null
                            ),
                            f.getColumn() + " = CASE " + tableInfo.getKeyColumn(),
                            "ELSE " + f.getColumn() + " END,",
                            null,
                            null
                    )
            ).append(NEWLINE);
        }

        sql = String.format(
                sqlMethod.getSql(),
                tableInfo.getTableName(),
                SqlScriptUtils.convertTrim(sqlScript.toString(), "SET", null, null, ","),
                tableInfo.getKeyColumn(),
                SqlScriptUtils.convertForeach("#{item." + tableInfo.getKeyProperty() + "}", COLL, null, "item", COMMA)
        );

        SqlSource sqlSource = super.createSqlSource(configuration, sql, Object.class);
        return this.addUpdateMappedStatement(mapperClass, modelClass, methodName, sqlSource);
    }
}
