package com.realaicy.pg.core.repository.callback;

import com.realaicy.pg.core.entity.search.SearchOperator;
import com.realaicy.pg.core.entity.search.Searchable;
import com.realaicy.pg.core.entity.search.filter.AndCondition;
import com.realaicy.pg.core.entity.search.filter.Condition;
import com.realaicy.pg.core.entity.search.filter.OrCondition;
import com.realaicy.pg.core.entity.search.filter.SearchFilter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import javax.persistence.Query;

/**
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public class DefaultSearchCallback implements SearchCallback {

    private static final String paramPrefix = "param_";

    private String alias;
    private String aliasWithDot;

    public DefaultSearchCallback() {
        this("");
    }

    public DefaultSearchCallback(String alias) {
        this.alias = alias;
        if (!StringUtils.isEmpty(alias)) {
            this.aliasWithDot = alias + ".";
        } else {
            this.aliasWithDot = "";
        }
    }

    public String getAlias() {
        return alias;
    }

    public String getAliasWithDot() {
        return aliasWithDot;
    }

    /**
     * 准备XQL语句<br/>
     * 方法首先判断 {@link Searchable}对象是否包含查询条件，如果不包含则直接返回<br/>
     * 接着方法遍历{@link Searchable}对象中的查询条件，为每一个查询条件拼接参数化查询语句，即拼接where 后面的<br/>
     *
     * @param ql 查询语句字符串
     * @param search 查询请求条件对象
     */
    @Override
    public void prepareQL(StringBuilder ql, Searchable search) {

        if (!search.hasSearchFilter()) {
            return;
        }

        int paramIndex = 1;
        for (SearchFilter searchFilter : search.getSearchFilters()) {

            if (searchFilter instanceof Condition) {
                Condition condition = (Condition) searchFilter;
                if (condition.getOperator() == SearchOperator.custom) {
                    continue;
                }
            }
            ql.append(" and ");

            paramIndex = genCondition(ql, paramIndex, searchFilter);

        }
    }

    /**
     * 准备XQL语句的order by 部分<br/>
     * 方法首先判断 {@link Searchable}对象是否包含排序信息，如果不包含则直接返回<br/>
     * 接着方法遍历{@link Searchable}对象中的的每一个排序条件，为每一个排序条件拼接查询语句，即拼接order by 后面的<br/>
     *
     * @param ql 查询语句字符串
     * @param search 查询请求条件对象
     */
    @Override
    public void prepareOrder(StringBuilder ql, Searchable search) {
        if (search.hashSort()) {
            ql.append(" order by ");
            for (Sort.Order order : search.getSort()) {
                ql.append(String.format("%s%s %s, ", getAliasWithDot(), order.getProperty(), order.getDirection().name().toLowerCase()));
            }
            //处理最后的多余的“，”
            ql.delete(ql.length() - 2, ql.length());
        }
    }

    /**
     * 为已经准备好的XQL语句的参数赋值<br/>
     *
     * @param query 查询语句
     * @param search 查询请求条件对象
     */
    @Override
    public void setValues(Query query, Searchable search) {

        int paramIndex = 1;

        for (SearchFilter searchFilter : search.getSearchFilters()) {
            paramIndex = setValues(query, searchFilter, paramIndex);
        }
    }

    /**
     * 为已经准备好的XQL语句的设置分页<br/>
     *
     * @param query 查询语句
     * @param search 查询请求条件对象
     */
    public void setPageable(Query query, Searchable search) {
        if (search.hasPageable()) {
            Pageable pageable = search.getPage();
            query.setFirstResult(pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }
    }

    /**
     * 为一个具体位置上的查询参数拼接条件语句<br/>
     * 方法首先判断 {@link SearchFilter}对象是否是复合条件（即；OrCondition或者AndCondition）<br/>
     * 接着方法根据{@link SearchFilter}对象的类型分别进行拼接处理，如果是复合条件的则需要循环遍历处理
     * <p/>
     *
     * @param ql 查询语句字符串
     * @param paramIndex 查询参数的位置
     * @param searchFilter 查询参数请求对象
     */
    private int genCondition(StringBuilder ql, int paramIndex, SearchFilter searchFilter) {

        boolean needAppendBracket = searchFilter instanceof OrCondition || searchFilter instanceof AndCondition;

        if (needAppendBracket) {
            ql.append("(");
        }

        if (searchFilter instanceof Condition) {
            Condition condition = (Condition) searchFilter;
            //自定义条件
            String entityProperty = condition.getEntityProperty();
            String operatorStr = condition.getOperatorStr();
            //实体名称
            ql.append(getAliasWithDot());
            ql.append(entityProperty);
            //操作符
            //1、如果是自定义查询符号，则使用SearchPropertyMappings中定义的默认的操作符
            ql.append(" ");
            ql.append(operatorStr);

            if (!condition.isUnaryFilter()) {
                ql.append(" :");
                ql.append(paramPrefix);
                ql.append(paramIndex++);
                return paramIndex;
            }
        } else if (searchFilter instanceof OrCondition) {
            boolean isFirst = true;
            for (SearchFilter orSearchFilter : ((OrCondition) searchFilter).getOrFilters()) {
                if (!isFirst) {
                    ql.append(" or ");
                }
                paramIndex = genCondition(ql, paramIndex, orSearchFilter);
                isFirst = false;
            }
        } else if (searchFilter instanceof AndCondition) {
            boolean isFirst = true;
            for (SearchFilter andSearchFilter : ((AndCondition) searchFilter).getAndFilters()) {
                if (!isFirst) {
                    ql.append(" and ");
                }
                paramIndex = genCondition(ql, paramIndex, andSearchFilter);
                isFirst = false;
            }
        }

        if (needAppendBracket) {
            ql.append(")");
        }
        return paramIndex;
    }

    private int setValues(Query query, SearchFilter searchFilter, int paramIndex) {
        if (searchFilter instanceof Condition) {

            Condition condition = (Condition) searchFilter;
            if (condition.getOperator() == SearchOperator.custom) {
                return paramIndex;
            }
            if (condition.isUnaryFilter()) {
                return paramIndex;
            }
            query.setParameter(paramPrefix + paramIndex++, formtValue(condition, condition.getValue()));

        } else if (searchFilter instanceof OrCondition) {

            for (SearchFilter orSearchFilter : ((OrCondition) searchFilter).getOrFilters()) {
                paramIndex = setValues(query, orSearchFilter, paramIndex);
            }

        } else if (searchFilter instanceof AndCondition) {
            for (SearchFilter andSearchFilter : ((AndCondition) searchFilter).getAndFilters()) {
                paramIndex = setValues(query, andSearchFilter, paramIndex);
            }
        }
        return paramIndex;
    }

    /**
     * 处理XQL中的“like”相关的拼接
     */
    private Object formtValue(Condition condition, Object value) {
        SearchOperator operator = condition.getOperator();
        if (operator == SearchOperator.like || operator == SearchOperator.notLike) {
            return "%" + value + "%";
        }
        if (operator == SearchOperator.prefixLike || operator == SearchOperator.prefixNotLike) {
            return value + "%";
        }

        if (operator == SearchOperator.suffixLike || operator == SearchOperator.suffixNotLike) {
            return "%" + value;
        }
        return value;
    }

}
