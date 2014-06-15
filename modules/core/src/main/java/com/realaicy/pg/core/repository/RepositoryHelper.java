package com.realaicy.pg.core.repository;

import com.realaicy.pg.core.entity.search.Searchable;
import com.realaicy.pg.core.repository.callback.SearchCallback;
import com.realaicy.pg.core.repository.support.annotation.EnableQueryCache;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;

/**
 * 仓库辅助类
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public class RepositoryHelper {

    private static EntityManager entityManager;
    private Class<?> entityClass;
    private boolean enableQueryCache = false;

    public RepositoryHelper(Class<?> entityClass) {

        this.entityClass = entityClass;

        //扫描这个实体是否启用缓存
        EnableQueryCache enableQueryCacheAnnotation =
                AnnotationUtils.findAnnotation(entityClass, EnableQueryCache.class);
        boolean enableQueryCache = false;
        if (enableQueryCacheAnnotation != null) {
            enableQueryCache = enableQueryCacheAnnotation.value();
        }
        this.enableQueryCache = enableQueryCache;
    }

    public static void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        entityManager = SharedEntityManagerCreator.createSharedEntityManager(entityManagerFactory);
    }

    public static EntityManager getEntityManager() {
        Assert.notNull(entityManager, "entityManager must null, please see " +
                "[com.realaicy.pg.core.repository.RepositoryHelper#setEntityManagerFactory]");
        return entityManager;
    }

    public static void flush() {
        getEntityManager().flush();
    }

    public static void clear() {
        flush();
        getEntityManager().clear();
    }

    /**
     * <p>ql条件查询<br/>
     * searchCallback默认实现请参考 {@see com.realaicy.pg.core.repository.callback.DefaultSearchCallback}<br/>
     * <p/>
     * 测试用例请参考：{@see com.realaicy.pg.core.repository.UserRepositoryImplForCustomSearchIT}
     * 和{@see com.realaicy.pg.core.repository.UserRepositoryImplForDefaultSearchIT}
     *
     * @param ql 查询字符串
     * @param searchable 查询请求对象，包含条件、分页、排序等
     * @param searchCallback 查询回调  自定义设置查询条件和赋值
     * @return 查询实体的list列表
     */
    @SuppressWarnings("unchecked")
    public <M> List<M> findAll(final String ql, final Searchable searchable, final SearchCallback searchCallback) {

        assertConverted(searchable);
        StringBuilder s = new StringBuilder(ql);
        searchCallback.prepareQL(s, searchable);
        searchCallback.prepareOrder(s, searchable);
        Query query = getEntityManager().createQuery(s.toString());
        applyEnableQueryCache(query);
        searchCallback.setValues(query, searchable);
        searchCallback.setPageable(query, searchable);

        return query.getResultList();
    }

    /**
     * <p>按条件统计<br/>
     * 测试用例请参考：{@see com.realaicy.pg.core.repository.UserRepositoryImplForCustomSearchIT}
     * 和{@see com.realaicy.pg.core.repository.UserRepositoryImplForDefaultSearchIT}
     *
     * @param ql 查询字符串
     * @param searchable 查询请求对象，包含条件、分页、排序等
     * @param searchCallback 查询回调  自定义设置查询条件和赋值
     * @return 按条件统计的数量
     */
    public long count(final String ql, final Searchable searchable, final SearchCallback searchCallback) {

        assertConverted(searchable);

        StringBuilder s = new StringBuilder(ql);
        searchCallback.prepareQL(s, searchable);
        Query query = getEntityManager().createQuery(s.toString());
        applyEnableQueryCache(query);
        searchCallback.setValues(query, searchable);

        return (Long) query.getSingleResult();
    }

    /**
     * 按条件查询一个实体
     *
     * @param ql 查询字符串
     * @param searchable 查询请求对象，包含条件、分页、排序等
     * @param searchCallback 查询回调  自定义设置查询条件和赋值
     * @return 查询实体
     */
    @SuppressWarnings("unchecked")
    public <M> M findOne(final String ql, final Searchable searchable, final SearchCallback searchCallback) {

        assertConverted(searchable);

        StringBuilder s = new StringBuilder(ql);
        searchCallback.prepareQL(s, searchable);
        searchCallback.prepareOrder(s, searchable);
        Query query = getEntityManager().createQuery(s.toString());
        applyEnableQueryCache(query);
        searchCallback.setValues(query, searchable);
        searchCallback.setPageable(query, searchable);
        query.setMaxResults(1);
        List<M> result = query.getResultList();

        if (result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    /**
     * @param ql 查询字符串
     * @param params 查询参数
     * @return 查询实体的list列表
     * @see RepositoryHelper#findAll(String, org.springframework.data.domain.Pageable, Object...)
     */
    public <M> List<M> findAll(final String ql, final Object... params) {

        //此处必须 (Pageable) null  否则默认又调用自己了 可变参列表
        return findAll(ql, (Pageable) null, params);

    }

    /**
     * <p>根据ql和按照索引顺序的params执行ql，pageable存储分页信息 null表示不分页<br/>
     * 具体使用请参考测试用例：{@see com.realaicy.pg.core.repository.UserRepository2ImplIT#testFindAll()}
     *
     * @param ql 查询字符串
     * @param pageable null表示不分页
     * @param params 查询参数
     * @return 查询实体的list列表
     */
    @SuppressWarnings("unchecked")
    public <M> List<M> findAll(final String ql, final Pageable pageable, final Object... params) {

        Query query = getEntityManager().createQuery(ql + prepareOrder(pageable != null ? pageable.getSort() : null));
        applyEnableQueryCache(query);
        setParameters(query, params);
        if (pageable != null) {
            query.setFirstResult(pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }

        return query.getResultList();
    }

    /**
     * <p>根据ql和按照索引顺序的params执行ql，sort存储排序信息 null表示不排序<br/>
     * 具体使用请参考测试用例：{@see com.realaicy.pg.core.repository.UserRepository2ImplIT#testFindAll()}
     *
     * @param ql 查询字符串
     * @param sort null表示不排序
     * @param params 查询参数
     * @return 查询实体的list列表
     */
    @SuppressWarnings("unchecked")
    public <M> List<M> findAll(final String ql, final Sort sort, final Object... params) {

        Query query = getEntityManager().createQuery(ql + prepareOrder(sort));
        applyEnableQueryCache(query);
        setParameters(query, params);

        return query.getResultList();
    }

    /**
     * <p>根据ql和按照索引顺序的params查询一个实体<br/>
     * 具体使用请参考测试用例：{@see com.realaicy.pg.core.repository.UserRepository2ImplIT#testFindOne()}
     *
     * @param ql 查询字符串
     * @param params 查询参数
     * @return 查询实体
     */
    public <M> M findOne(final String ql, final Object... params) {

        List<M> list = findAll(ql, new PageRequest(0, 1), params);

        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * <p>根据ql和按照索引顺序的params执行ql统计<br/>
     * 具体使用请参考测试用例：com.realaicy.pg.core.repository.UserRepository2ImplIT#testCountAll()
     *
     * @param ql 查询字符串
     * @param params 查询参数
     * @return 查询实体
     */
    public long count(final String ql, final Object... params) {

        Query query = entityManager.createQuery(ql);
        applyEnableQueryCache(query);
        setParameters(query, params);

        return (Long) query.getSingleResult();
    }

    /**
     * <p>执行批处理语句.如 之间insert, update, delete 等.<br/>
     * 具体使用请参考测试用例：{@see com.realaicy.pg.core.repository.UserRepository2ImplIT#testBatchUpdate()}
     *
     * @param ql 查询字符串
     * @param params 查询参数
     * @return the number of entities updated or deleted
     */
    public int batchUpdate(final String ql, final Object... params) {

        Query query = getEntityManager().createQuery(ql);
        setParameters(query, params);

        return query.executeUpdate();
    }

    /**
     * 按顺序设置Query参数
     *
     * @param query 查询语句
     * @param params 查询参数
     */
    public void setParameters(Query query, Object[] params) {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]);
            }
        }
    }

    /**
     * 拼排序
     */
    public String prepareOrder(Sort sort) {
        if (sort == null || !sort.iterator().hasNext()) {
            return "";
        }
        StringBuilder orderBy = new StringBuilder("");
        orderBy.append(" order by ");
        orderBy.append(sort.toString().replace(":", " "));
        return orderBy.toString();
    }

    public <T> JpaEntityInformation<T, ?> getMetadata(Class<T> entityClass) {
        return JpaEntityInformationSupport.getMetadata(entityClass, entityManager);
    }

    public String getEntityName(Class<?> entityClass) {
        return getMetadata(entityClass).getEntityName();
    }

    public void applyEnableQueryCache(Query query) {
        if (enableQueryCache) {
            query.setHint("org.hibernate.cacheable", true);//开启查询缓存
        }
    }

    private void assertConverted(Searchable searchable) {
        if (!searchable.isConverted()) {
            searchable.convert(this.entityClass);
        }
    }

}
