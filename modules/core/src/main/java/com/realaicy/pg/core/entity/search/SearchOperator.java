package com.realaicy.pg.core.entity.search;

import com.realaicy.pg.core.entity.search.exception.SearchException;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * <p>查询操作符</p>
 */
public enum SearchOperator {
    eq("等于", "="), ne("不等于", "!="),
    gt("大于", ">"), gte("大于等于", ">="), lt("小于", "<"), lte("小于等于", "<="),
    prefixLike("前缀模糊匹配", "like"), prefixNotLike("前缀模糊不匹配", "not like"),
    suffixLike("后缀模糊匹配", "like"), suffixNotLike("后缀模糊不匹配", "not like"),
    like("模糊匹配", "like"), notLike("不匹配", "not like"),
    isNull("空", "is null"), isNotNull("非空", "is not null"),
    in("包含", "in"), notIn("不包含", "not in"), custom("自定义默认的", null);

    private final String info;
    private final String symbol;

    SearchOperator(final String info, String symbol) {
        this.info = info;
        this.symbol = symbol;
    }

    /**
     * 操作符是否允许为空
     * <p>除了查询条件为“isNull”或者“isNotNull”之外都不允许查询的目标值为空
     *
     * @param operator 查询操作符
     * @return true 如果操作符允许为空
     */
    public static boolean isAllowBlankValue(final SearchOperator operator) {
        return operator == SearchOperator.isNotNull || operator == SearchOperator.isNull;
    }

    /**
     * 所有操作符集合的字符串
     *
     * @return 所有操作符集合的字符串
     */
    public static String getAllOperator() {
        return Arrays.toString(SearchOperator.values());
    }

    /**
     * 通过给定的symbol返回对应的SearchOperator对象
     * <p>除了查询条件为“isNull”或者“isNotNull”之外都不允许查询的目标值为空
     *
     * @param symbol 查询操作符的symbol
     * @return SearchOperator
     */
    public static SearchOperator getSearchOperatorBySymbol(String symbol) throws SearchException {
        symbol = formatSymbol(symbol);
        for (SearchOperator operator : values()) {
            if (operator.getSymbol().equals(symbol)) {
                return operator;
            }
        }
        throw new SearchException("SearchOperator not matched search operator symbol : " + symbol);
    }

    private static String formatSymbol(String symbol) {
        if (StringUtils.isBlank(symbol)) {
            return symbol;
        }
        return symbol.trim().toLowerCase().replace("  ", " ");
    }

    public String getInfo() {
        return info;
    }

    public String getSymbol() {
        return symbol;
    }

}
