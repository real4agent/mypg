package com.realaicy.pg.core.entity.search;

import com.realaicy.pg.core.entity.search.exception.InvalidSearchPropertyException;
import com.realaicy.pg.core.entity.search.exception.InvalidSearchValueException;
import com.realaicy.pg.core.entity.search.exception.SearchException;
import com.realaicy.pg.core.entity.search.filter.SearchFilter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collection;
import java.util.Map;

/**
 * 查询请求接口
 * <p/>
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public abstract class Searchable {

    /**
     * 创建一个新的查询请求
     *
     * @return 返回一个默认实现 {@link SearchRequest}
     */
    public static Searchable newSearchable() {
        return new SearchRequest();
    }

    /**
     * 创建一个新的查询请求
     *
     * @return 返回一个默认实现 {@link SearchRequest}
     */
    public static Searchable newSearchable(final Map<String, Object> searchParams) throws SearchException {
        return new SearchRequest(searchParams);
    }

    /**
     * 创建一个新的查询请求,包含分页信息
     *
     * @return 返回一个默认实现 {@link SearchRequest}
     */
    public static Searchable newSearchable(final Map<String, Object> searchParams, final Pageable page)
            throws SearchException {
        return new SearchRequest(searchParams, page);
    }

    /**
     * 创建一个新的查询请求，包含排序信息
     *
     * @return 返回一个默认实现 {@link SearchRequest}
     */
    public static Searchable newSearchable(final Map<String, Object> searchParams, final Sort sort)
            throws SearchException {
        return new SearchRequest(searchParams, sort);
    }

    /**
     * 创建一个新的查询请求，包含分页和排序等信息
     *
     * @return 返回一个默认实现 {@link SearchRequest}
     */
    public static Searchable newSearchable(final Map<String, Object> searchParams,
                                           final Pageable page, final Sort sort) {
        return new SearchRequest(searchParams, page, sort);
    }

    /**
     * <p>添加一个查询参数</p>
     * 添加过滤条件 如key="parent.id_eq" value = 1
     * 如果添加时不加操作符 默认是custom 即如key=parent 实际key是parent_custom
     *
     * @param key 如 name_like
     * @param value 如果是in查询 多个值之间","分隔
     * @return 返回一个默认实现
     */
    public abstract Searchable addSearchParam(final String key, final Object value) throws SearchException;

    /**
     * 添加一组查询参数
     *
     * @param searchParams 一组查询参数
     * @return 返回一个默认实现
     */
    public abstract Searchable addSearchParams(final Map<String, Object> searchParams) throws SearchException;

    /**
     * 添加过滤条件
     *
     * @param searchProperty 查询的属性名
     * @param operator 操作运算符
     * @param value 值
     */
    public abstract Searchable addSearchFilter(
            final String searchProperty, final SearchOperator operator, final Object value) throws SearchException;

    public abstract Searchable addSearchFilter(final SearchFilter searchFilter);

    /**
     * 添加多个and连接的过滤条件
     *
     * @param searchFilters 需要连接的多个过滤条件
     * @return 返回一个默认实现
     */
    public abstract Searchable addSearchFilters(final Collection<? extends SearchFilter> searchFilters);

    /**
     * 添加多个or连接的过滤条件
     *
     * @param first 第一个
     * @param others 其他
     * @return 返回一个默认实现
     */
    public abstract Searchable or(final SearchFilter first, final SearchFilter... others);

    /**
     * 添加多个and连接的过滤条件
     *
     * @param first 第一个
     * @param others 其他
     * @return 返回一个默认实现
     */
    public abstract Searchable and(final SearchFilter first, final SearchFilter... others);

    /**
     * 移除指定key的过滤条件
     *
     * @param key 指定的key
     */
    public abstract Searchable removeSearchFilter(final String key);

    /**
     * 移除指定属性 和 操作符的过滤条件
     *
     * @param searchProperty 属性
     * @param operator 操作符
     * @return 返回一个默认实现
     */
    public abstract Searchable removeSearchFilter(String searchProperty, SearchOperator operator);

    /**
     * 把字符串类型的值转化为entity属性值
     *
     * @param entityClass 实体类型
     * @param <T> 返回一个默认实现
     */
    public abstract <T> Searchable convert(final Class<T> entityClass)
            throws InvalidSearchValueException, InvalidSearchPropertyException;

    /**
     * 标识为已经转换过了 避免多次转换
     */
    public abstract Searchable markConverted();

    /**
     * 是否已经转换过了 避免多次转换
     *
     * @return 如果已经转换则返回真
     */
    public abstract boolean isConverted();

    /**
     * 获取查询过滤条件
     *
     * @return 查询过滤条件的集合
     */
    public abstract Collection<SearchFilter> getSearchFilters();

    /**
     * 是否有查询参数
     *
     * @return 如果有查询参数则返回真
     */
    public abstract boolean hasSearchFilter();

    /**
     * 是否有排序
     *
     * @return 如果有排序则返回真
     */
    public abstract boolean hashSort();

    public abstract void removeSort();

    /**
     * 获取排序信息
     *
     * @return 排序接口对象
     */
    public abstract Sort getSort();

    public abstract Searchable addSort(final Sort sort);

    public abstract Searchable addSort(final Sort.Direction direction, String property);

    /**
     * 是否有分页
     *
     * @return 如果有分页则返回真
     */
    public abstract boolean hasPageable();

    public abstract void removePageable();

    /**
     * 获取分页和排序信息
     *
     * @return 分页接口对象
     */
    public abstract Pageable getPage();

    public abstract Searchable setPage(final Pageable page);

    /**
     * @param pageNumber 分页页码 索引从 0 开始
     * @param pageSize 每页大小
     * @return 返回一个默认实现
     */
    public abstract Searchable setPage(final int pageNumber, final int pageSize);

    /**
     * 是否包含查询键  如 name_like
     * 包括 or 和 and的
     *
     * @param key 属性的值
     * @return 如果包含则返回真
     */
    public abstract boolean containsSearchKey(final String key);

    /**
     * 获取查询属性对应的值
     * 不能获取or 或 and 的
     *
     * @param key 属性
     * @return 对应的值
     */
    public abstract <T> T getValue(final String key);

}
