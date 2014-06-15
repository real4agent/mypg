package com.realaicy.pg.core.repository.callback;

import com.realaicy.pg.core.entity.search.Searchable;

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
public interface SearchCallback {

    public static final SearchCallback NONE = new NoneSearchCallback();
    public static final SearchCallback DEFAULT = new DefaultSearchCallback();

    /**
     * 动态拼HQL where、group by having
     *
     * @param ql 查询语句
     * @param search 查询请求条件对象
     */
    public void prepareQL(StringBuilder ql, Searchable search);

    public void prepareOrder(StringBuilder ql, Searchable search);

    /**
     * 根据search给query赋值及设置分页信息
     *
     * @param query 查询语句对象
     * @param search 查询请求条件对象
     */
    public void setValues(Query query, Searchable search);

    public void setPageable(Query query, Searchable search);

}
