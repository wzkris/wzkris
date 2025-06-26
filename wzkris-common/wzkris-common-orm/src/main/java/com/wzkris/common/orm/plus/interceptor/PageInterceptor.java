package com.wzkris.common.orm.plus.interceptor;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.parser.JsqlParserGlobal;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.DialectModel;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.IDialect;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.utils.PageUtil;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 自定义分页插件
 * @date : 2024/1/11 16:34
 */
public class PageInterceptor extends PaginationInnerInterceptor {

    /**
     * 这里进行count,如果count为0这返回false(就是不再执行sql了)
     */
    @Override
    public boolean willDoQuery(
            Executor executor,
            MappedStatement ms,
            Object parameter,
            RowBounds rowBounds,
            ResultHandler resultHandler,
            BoundSql boundSql)
            throws SQLException {
        Page<?> page = PageUtil.getPage();
        if (page == null) {
            return true;
        }
        // 这里执行COUNT(*)
        BoundSql countSql;
        MappedStatement countMs = buildAutoCountMappedStatement(ms);
        String countSqlStr = this.autoCountSql(boundSql.getSql());
        PluginUtils.MPBoundSql mpBoundSql = PluginUtils.mpBoundSql(boundSql);
        countSql = new BoundSql(countMs.getConfiguration(), countSqlStr, mpBoundSql.parameterMappings(), parameter);
        PluginUtils.setAdditionalParameter(countSql, mpBoundSql.additionalParameters());

        CacheKey cacheKey = executor.createCacheKey(countMs, parameter, rowBounds, countSql);
        List<Object> result = executor.query(countMs, parameter, rowBounds, resultHandler, cacheKey, countSql);
        long total = 0;
        if (CollectionUtils.isNotEmpty(result)) {
            // 个别数据库 count 没数据不会返回 0
            Object o = result.get(0);
            if (o != null) {
                total = Long.parseLong(o.toString());
            }
        }
        page.setTotal(total);
        if (total % page.getPageSize() == 0) {
            page.setPages(total / page.getPageSize());
        } else {
            page.setPages(total / page.getPageSize() + 1);
        }
        return this.continuePage(page);
    }

    /**
     * 改改SQL啥的
     */
    @Override
    public void beforeQuery(
            Executor executor,
            MappedStatement ms,
            Object parameter,
            RowBounds rowBounds,
            ResultHandler resultHandler,
            BoundSql boundSql) {
        Page<?> page = PageUtil.getPage();
        if (page == null) {
            return;
        }

        // 处理 orderBy 拼接
        boolean addOrdered = false;
        String buildSql = boundSql.getSql();
        List<OrderItem> orders = page.getOrders();
        if (CollectionUtils.isNotEmpty(orders)) {
            addOrdered = true;
            buildSql = this.concatOrderBy(buildSql, orders);
        }

        // size 小于 0 且不限制返回值则不构造分页sql
        if (page.getPageSize() < 0 && null == maxLimit) {
            if (addOrdered) {
                PluginUtils.mpBoundSql(boundSql).sql(buildSql);
            }
            return;
        }

        this.handlerLimit(page, maxLimit);
        IDialect dialect = findIDialect(executor);

        final Configuration configuration = ms.getConfiguration();
        DialectModel model = dialect.buildPaginationSql(buildSql, page.offset(), page.getPageSize());
        PluginUtils.MPBoundSql mpBoundSql = PluginUtils.mpBoundSql(boundSql);

        List<ParameterMapping> mappings = mpBoundSql.parameterMappings();
        Map<String, Object> additionalParameter = mpBoundSql.additionalParameters();
        model.consumers(mappings, configuration, additionalParameter);
        mpBoundSql.sql(model.getDialectSql());
        mpBoundSql.parameterMappings(mappings);
    }

    private String autoCountSql(String sql) {
        try {
            Select select = (Select) JsqlParserGlobal.parse(sql);
            // https://github.com/baomidou/mybatis-plus/issues/3920  分页增加union语法支持
            if (select instanceof SetOperationList) {
                return lowLevelCountSql(sql);
            }
            PlainSelect plainSelect = (PlainSelect) select;
            Distinct distinct = plainSelect.getDistinct();
            GroupByElement groupBy = plainSelect.getGroupBy();

            // 包含 distinct、groupBy 不优化
            if (null != distinct || null != groupBy) {
                return lowLevelCountSql(select.toString());
            }

            // 优化 order by 在非分组情况下
            List<OrderByElement> orderBy = plainSelect.getOrderByElements();
            if (CollectionUtils.isNotEmpty(orderBy)) {
                boolean canClean = true;
                for (OrderByElement order : orderBy) {
                    // order by 里带参数,不去除order by
                    Expression expression = order.getExpression();
                    if (!(expression instanceof Column) && expression.toString().contains(StringPool.QUESTION_MARK)) {
                        canClean = false;
                        break;
                    }
                }
                if (canClean) {
                    plainSelect.setOrderByElements(null);
                }
            }

            // #95 Github, selectItems contains #{} ${}, which will be translated to ?, and it may be in a function:
            // power(#{myInt},2)
            for (SelectItem item : plainSelect.getSelectItems()) {
                if (item.toString().contains(StringPool.QUESTION_MARK)) {
                    return lowLevelCountSql(select.toString());
                }
            }

            // 包含 join 连表,进行判断是否移除 join 连表
            if (optimizeJoin) {
                List<Join> joins = plainSelect.getJoins();
                if (CollectionUtils.isNotEmpty(joins)) {
                    boolean canRemoveJoin = true;
                    String whereS = Optional.ofNullable(plainSelect.getWhere())
                            .map(Expression::toString)
                            .orElse(StringPool.EMPTY);
                    // 不区分大小写
                    whereS = whereS.toLowerCase();
                    for (Join join : joins) {
                        if (!join.isLeft()) {
                            canRemoveJoin = false;
                            break;
                        }
                        FromItem rightItem = join.getRightItem();
                        String str = "";
                        if (rightItem instanceof Table table) {
                            str = Optional.ofNullable(table.getAlias())
                                    .map(Alias::getName)
                                    .orElse(table.getName())
                                    + StringPool.DOT;
                        } else if (rightItem instanceof ParenthesedSelect subSelect) {
                            /* 如果 left join 是子查询，并且子查询里包含 ?(代表有入参) 或者 where 条件里包含使用 join 的表的字段作条件,就不移除 join */
                            if (subSelect.toString().contains(StringPool.QUESTION_MARK)) {
                                canRemoveJoin = false;
                                break;
                            }
                            str = subSelect.getAlias().getName() + StringPool.DOT;
                        }
                        // 不区分大小写
                        str = str.toLowerCase();

                        if (whereS.contains(str)) {
                            /* 如果 where 条件里包含使用 join 的表的字段作条件,就不移除 join */
                            canRemoveJoin = false;
                            break;
                        }

                        for (Expression expression : join.getOnExpressions()) {
                            if (expression.toString().contains(StringPool.QUESTION_MARK)) {
                                /* 如果 join 里包含 ?(代表有入参) 就不移除 join */
                                canRemoveJoin = false;
                                break;
                            }
                        }
                    }

                    if (canRemoveJoin) {
                        plainSelect.setJoins(null);
                    }
                }
            }

            // 优化 SQL
            plainSelect.setSelectItems(COUNT_SELECT_ITEM);
            return select.toString();
        } catch (JSQLParserException e) {
            // 无法优化使用原 SQL
            logger.warn(
                    "optimize this sql to a count sql has exception, sql:\"" + sql + "\", exception:\n" + e.getCause());
        } catch (Exception e) {
            logger.warn("optimize this sql to a count sql has error, sql:\"" + sql + "\", exception:\n" + e);
        }
        return lowLevelCountSql(sql);
    }

    private void handlerLimit(Page<?> page, Long limit) {
        final long size = page.getPageSize();
        if (limit != null && limit > 0 && (size > limit || size < 0)) {
            page.setPageSize(limit);
        }
    }

    private boolean continuePage(Page<?> page) {
        if (page.getTotal() <= 0) {
            return false;
        }
        if (page.getPageNum() > page.getPages()) {
            if (overflow) {
                // 溢出总页数处理
                page.setPageNum(1);
            } else {
                // 超过最大范围，未设置溢出逻辑中断 list 执行
                return false;
            }
        }
        return true;
    }

}
