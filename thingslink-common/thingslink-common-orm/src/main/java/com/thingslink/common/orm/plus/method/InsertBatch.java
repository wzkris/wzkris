package com.thingslink.common.orm.plus.method;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.thingslink.common.orm.plus.enums.SqlMethod;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.StringJoiner;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 批量插入
 * @date : 2024/1/9 16:30
 */
public class InsertBatch extends AbstractMethod {

    public InsertBatch() {
        this(SqlMethod.INSERT_BATCH.getMethod());
    }

    /**
     * @param methodName 方法名
     * @since 3.5.0
     */
    protected InsertBatch(String methodName) {
        super(methodName);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql;
        SqlMethod sqlMethod = SqlMethod.INSERT_BATCH;

        StringJoiner columns = new StringJoiner(",").add(tableInfo.getKeyColumn());
        StringJoiner values = new StringJoiner(",").add("#{item." + tableInfo.getKeyProperty() + "}");
        tableInfo.getFieldList().forEach(f -> {
            columns.add(f.getColumn());
            values.add("#{item." + f.getProperty() + "}");
        });

        sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), columns,
                SqlScriptUtils.convertForeach("(" + values + ")", COLL, null, "item", COMMA)
        );

        KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
        String keyProperty = null;
        String keyColumn = null;
        // 表包含主键处理逻辑,如果不包含主键当普通字段处理
        if (StringUtils.isNotBlank(tableInfo.getKeyProperty())) {
            if (tableInfo.getIdType() == IdType.AUTO) {
                /* 自增主键 */
                keyGenerator = Jdbc3KeyGenerator.INSTANCE;
                keyProperty = tableInfo.getKeyProperty();
                // 去除转义符
                keyColumn = SqlInjectionUtils.removeEscapeCharacter(tableInfo.getKeyColumn());
            }
            else if (null != tableInfo.getKeySequence()) {
                keyGenerator = TableInfoHelper.genKeyGenerator(methodName, tableInfo, builderAssistant);
                keyProperty = tableInfo.getKeyProperty();
                keyColumn = tableInfo.getKeyColumn();
            }
        }

        SqlSource sqlSource = super.createSqlSource(configuration, sql, Object.class);
        return this.addInsertMappedStatement(mapperClass, modelClass, methodName, sqlSource, keyGenerator, keyProperty, keyColumn);
    }
}
