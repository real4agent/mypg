package com.realaicy.pg.core.repository.support;

import com.google.common.collect.Sets;
import com.realaicy.pg.core.entity.search.Searchable;
import com.realaicy.pg.core.plugin.entity.LogicDeleteable;
import com.realaicy.pg.core.repository.BaseRepository;
import com.realaicy.pg.core.repository.RepositoryHelper;
import com.realaicy.pg.core.repository.callback.SearchCallback;
import com.realaicy.pg.core.repository.support.annotation.QueryJoin;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.LockMetadataProvider;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

/**
 * <p>抽象基础Custom Repository 实现</p>
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public class SimpleBaseRepository<M, ID extends Serializable> extends SimpleJpaRepository<M, ID>
        implements BaseRepository<M, ID> {

    public static final String LOGIC_DELETE_ALL_QUERY_STRING = "update %s x set x.deleted=true where x in (?1)";
    public static final String DELETE_ALL_QUERY_STRING = "delete from %s x where x in (?1)";
    public static final String FIND_QUERY_STRING = "from %s x where 1=1 ";
    public static final String COUNT_QUERY_STRING = "select count(x) from %s x where 1=1 ";

    private final EntityManager em;
    private final JpaEntityInformation<M, ID> entityInformation;

    private final RepositoryHelper repositoryHelper;

    private LockMetadataProvider lockMetadataProvider;

    private Class<M> entityClass;
    private String entityName;
    private String idName;

    /**
     * 查询所有的QL
     */
    private String findAllQL;
    /**
     * 统计QL
     */
    private String countAllQL;

    private QueryJoin[] joins;

    private SearchCallback searchCallback = SearchCallback.DEFAULT;

    public SimpleBaseRepository(JpaEntityInformation<M, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);

        this.entityInformation = entityInformation;
        this.em = entityManager;

        this.entityClass = this.entityInformation.getJavaType();
        this.entityName = this.entityInformation.getEntityName();
        this.idName = this.entityInformation.getIdAttributeNames().iterator().next();

        repositoryHelper = new RepositoryHelper(entityClass);
        findAllQL = String.format(FIND_QUERY_STRING, entityName);
        countAllQL = String.format(COUNT_QUERY_STRING, entityName);
    }

    /**
     * Configures a custom {@link org.springframework.data.jpa.repository.support.LockMetadataProvider} to be used to detect {@link javax.persistence.LockModeType}s to be applied to
     * queries.
     */
    public void setLockMetadataProvider(LockMetadataProvider lockMetadataProvider) {
        super.setLockMetadataProvider(lockMetadataProvider);
        this.lockMetadataProvider = lockMetadataProvider;
    }

    /**
     * 设置searchCallback
     *
     * @param searchCallback xxx
     */
    public void setSearchCallback(SearchCallback searchCallback) {
        this.searchCallback = searchCallback;
    }

    /**
     * 设置查询所有的ql
     *
     * @param findAllQL xxx
     */
    public void setFindAllQL(String findAllQL) {
        this.findAllQL = findAllQL;
    }

    /**
     * 设置统计的ql
     *
     * @param countAllQL xxx
     */
    public void setCountAllQL(String countAllQL) {
        this.countAllQL = countAllQL;
    }

    public void setJoins(QueryJoin[] joins) {
        this.joins = joins;
    }

    /////////////////////////////////////////////////
    ////////覆盖默认spring data jpa的实现////////////
    /////////////////////////////////////////////////

    /**
     * 删除实体<br/>
     * 重写父类方法，以便处理“逻辑删除”的情况
     *
     * @param entity 实体
     */
    @Transactional
    @Override
    public void delete(final M entity) {
//        if (m == null) {
//            return;
//        }
        Assert.notNull(entity, "The entity must not be null!");
        if (entity instanceof LogicDeleteable) {
            ((LogicDeleteable) entity).markDeleted();
            save(entity);
        } else {
            super.delete(entity);
        }
    }

    /**
     * 批量删除实体<br/>
     * 重写父类方法，以便处理“逻辑删除”的情况
     *
     * @param entities 实体集合
     */
    @Transactional
    @Override
    public void deleteInBatch(final Iterable<M> entities) {

        Assert.notNull(entities, "The given Iterable of entities not be null!");

        if (!entities.iterator().hasNext()) {
            return;
        }

        Set models = Sets.newHashSet(entities.iterator());

        boolean logicDeleteableEntity = LogicDeleteable.class.isAssignableFrom(this.entityClass);

        if (logicDeleteableEntity) {
            String ql = String.format(LOGIC_DELETE_ALL_QUERY_STRING, entityName);
            repositoryHelper.batchUpdate(ql, models);
        } else {
            String ql = String.format(DELETE_ALL_QUERY_STRING, entityName);
            repositoryHelper.batchUpdate(ql, models);
        }
    }

    /**
     * 按照主键查询<br/>
     * 重写父类方法，用以提高效率
     *
     * @param id 主键
     * @return 返回id对应的实体
     */
    @Transactional
    @Override
    public M findOne(ID id) {
        if (id == null) {
            return null;
        }
        if (id instanceof Integer && (Integer) id == 0) {
            return null;
        }
        if (id instanceof Long && (Long) id == 0L) {
            return null;
        }
        return super.findOne(id);
    }

    /**
     * 重写默认的 这样可以走一级/二级缓存
     *
     * @param id 主键
     * @return 是否包含
     */
    @Override
    public boolean exists(ID id) {
        return findOne(id) != null;
    }

    ////////根据Specification查询 直接从SimpleJpaRepository复制过来的---开始///////////////////////////////////
    //public M findOne(Specification<M> spec)
    //public List<M> findAll(Specification<M> spec)
    //public Page<M> findAll(Specification<M> spec, Pageable pageable)
    //public List<M> findAll(Specification<M> spec, Sort sort)
    //public long count(Specification<M> spec)

    /*
     * @see org.springframework.data.repository.CrudRepository#findAll(ID[])
     * 重写父类方法，因为父类的这个方法中调用了父类自己的一个私有方法。。。
     */
    public List<M> findAll(Iterable<ID> ids) {

        return getQuery(new Specification<M>() {
            public Predicate toPredicate(Root<M> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<?> path = root.get(entityInformation.getIdAttribute());
                return path.in(cb.parameter(Iterable.class, "ids"));
            }
        }, (Sort) null).setParameter("ids", ids).getResultList();
    }

    ////////根据Specification查询 直接从SimpleJpaRepository复制过来的---结束///////////////////////////////////

    @Override
    public List<M> findAll() {
        return repositoryHelper.findAll(findAllQL);
    }

    @Override
    public List<M> findAll(final Sort sort) {
        return repositoryHelper.findAll(findAllQL, sort);
    }

    @Override
    public Page<M> findAll(final Pageable pageable) {
        return new PageImpl<M>(
                repositoryHelper.<M>findAll(findAllQL, pageable),
                pageable,
                repositoryHelper.count(countAllQL)
        );
    }

    @Override
    public long count() {
        return repositoryHelper.count(countAllQL);
    }

    @Override
    public Page<M> findAll(final Searchable searchable) {
        List<M> list = repositoryHelper.findAll(findAllQL, searchable, searchCallback);
        long total = searchable.hasPageable() ? count(searchable) : list.size();
        return new PageImpl<M>(
                list,
                searchable.getPage(),
                total
        );
    }

    @Override
    public long count(final Searchable searchable) {
        return repositoryHelper.count(countAllQL, searchable, searchCallback);
    }

    /**
     * 根据主键删除相应实体
     * <br/>实现自定义接口方法
     *
     * @param ids 实体
     */
    @Transactional
    public void delete(final ID[] ids) {
        if (ArrayUtils.isEmpty(ids)) {
            return;
        }
        List<M> models = new ArrayList<M>();
        for (ID id : ids) {
            M model;
            try {
                model = entityClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("batch delete " + entityClass + " error", e);
            }
            try {
                BeanUtils.setProperty(model, idName, id);
            } catch (Exception e) {
                throw new RuntimeException("batch delete " + entityClass + " error, can not set id", e);
            }
            models.add(model);
        }
        deleteInBatch(models);
    }

//protected Page<M> readPage(TypedQuery<M> query, Pageable pageable, Specification<M> spec)
//protected TypedQuery<M> getQuery(Specification<M> spec, Pageable pageable)

    /**
     * Creates a new count query for the given {@link org.springframework.data.jpa.domain.Specification}.
     * <p/>
     * <br/>重写父类方法，以便可以在需要的时候启用缓存
     *
     * @param spec can be {@literal null}.
     * @return xxx
     */
    protected TypedQuery<Long> getCountQuery(Specification<M> spec) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);

        Root<M> root = applySpecificationToCriteria(spec, query);

        if (query.isDistinct()) {
            query.select(builder.countDistinct(root));
        } else {
            query.select(builder.count(root));
        }

        TypedQuery<Long> q = em.createQuery(query);
        repositoryHelper.applyEnableQueryCache(q);
        return q;
    }

    /**
     * Creates a {@link javax.persistence.TypedQuery} for the given {@link org.springframework.data.jpa.domain.Specification} and {@link org.springframework.data.domain.Sort}.
     * <br/>重写父类方法，以便可以在需要的时候启用缓存
     *
     * @param spec can be {@literal null}.
     * @param sort can be {@literal null}.
     * @return xxx
     */
    protected TypedQuery<M> getQuery(Specification<M> spec, Sort sort) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<M> query = builder.createQuery(entityClass);

        Root<M> root = applySpecificationToCriteria(spec, query);
        query.select(root);

        applyJoins(root);

        if (sort != null) {
            query.orderBy(toOrders(sort, root, builder));
        }

        TypedQuery<M> q = em.createQuery(query);

        repositoryHelper.applyEnableQueryCache(q);

        return applyLockMode(q);
    }

    /**
     * Applies the given {@link org.springframework.data.jpa.domain.Specification} to the given {@link javax.persistence.criteria.CriteriaQuery}.
     *
     * @param spec can be {@literal null}.
     * @param query must not be {@literal null}.
     * @return xxx
     */
    private <S> Root<M> applySpecificationToCriteria(Specification<M> spec, CriteriaQuery<S> query) {

        Assert.notNull(query);
        Root<M> root = query.from(entityClass);

        if (spec == null) {
            return root;
        }

        CriteriaBuilder builder = em.getCriteriaBuilder();
        Predicate predicate = spec.toPredicate(root, query, builder);

        if (predicate != null) {
            query.where(predicate);
        }

        return root;
    }

    private TypedQuery<M> applyLockMode(TypedQuery<M> query) {
        LockModeType type = lockMetadataProvider == null ? null : lockMetadataProvider.getLockModeType();
        return type == null ? query : query.setLockMode(type);
    }

    private void applyJoins(Root<M> root) {
        if (joins == null) {
            return;
        }

        for (QueryJoin join : joins) {
            root.join(join.property(), join.joinType());
        }
    }

}
