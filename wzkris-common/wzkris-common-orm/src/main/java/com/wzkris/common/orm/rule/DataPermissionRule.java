package com.wzkris.common.orm.rule;

import com.wzkris.common.core.model.BaseLoginUser;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;

/**
 * 数据权限规则策略接口
 * <p>
 * 每种数据权限维度（部门、创建人、自定义等）实现一个规则，
 * 通过 Spring Bean 自动注册，可无限扩展。
 *
 * @author wzkris
 */
public interface DataPermissionRule {

    /**
     * 规则标识，对应 {@link com.wzkris.common.orm.annotation.DataPermission#type()}
     *
     * @return 规则类型
     */
    DataPermissionType getType();

    /**
     * 当前用户是否适用此规则
     *
     * @param loginUser 登录用户
     * @return 是否适用
     */
    boolean isApplicable(BaseLoginUser loginUser);

    /**
     * 生成 SQL 表达式，完全由规则实现控制
     *
     * @param table       当前解析的表
     * @param where       原始 where 条件
     * @param config      列配置
     * @param loginUser   登录用户
     * @return SQL 表达式，返回 null 表示跳过
     */
    Expression getExpression(Table table, Expression where, DataColumnConfig config, BaseLoginUser loginUser);

}
