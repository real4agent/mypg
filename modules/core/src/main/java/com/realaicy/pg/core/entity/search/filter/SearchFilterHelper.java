package com.realaicy.pg.core.entity.search.filter;

import com.realaicy.pg.core.entity.search.SearchOperator;
import com.realaicy.pg.core.entity.search.exception.SearchException;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

/**
 * 查询条件辅助类
 * <p/>
 * Created by realaicy on 14-3-24.
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public final class SearchFilterHelper {

    /**
     * 根据查询key和值生成Condition
     *
     * @param key 如 name_like
     * @param value 查询属性的目标值
     * @return {@link SearchFilter }
     */
    public static SearchFilter newCondition(final String key, final Object value) throws SearchException {
        return Condition.newCondition(key, value);
    }

    /**
     * 根据查询属性、操作符和值生成Condition
     *
     * @param searchProperty 属性
     * @param operator 操作符
     * @param value 值
     * @return {@link SearchFilter }
     */
    public static SearchFilter newCondition(final String searchProperty, final SearchOperator operator, final Object value) {
        return Condition.newCondition(searchProperty, operator, value);
    }

    /**
     * 拼or条件
     *
     * @param first 第一个条件
     * @param others 其他的条件
     * @return 返回一个拼接后的or条件{@link SearchFilter }
     */
    public static SearchFilter or(SearchFilter first, SearchFilter... others) {
        OrCondition orCondition = new OrCondition();
        orCondition.getOrFilters().add(first);
        if (ArrayUtils.isNotEmpty(others)) {
            orCondition.getOrFilters().addAll(Arrays.asList(others));
        }
        return orCondition;
    }

    /**
     * 拼and条件
     *
     * @param first 第一个条件
     * @param others 其他的条件
     * @return 返回一个拼接后的and条件{@link SearchFilter }
     */
    public static SearchFilter and(SearchFilter first, SearchFilter... others) {
        AndCondition andCondition = new AndCondition();
        andCondition.getAndFilters().add(first);
        if (ArrayUtils.isNotEmpty(others)) {
            andCondition.getAndFilters().addAll(Arrays.asList(others));
        }
        return andCondition;
    }

}
