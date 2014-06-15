package com.realaicy.pg.core.repository;

import com.realaicy.pg.core.entity.search.Searchable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * <p>抽象DAO层基类 提供一些简便方法<br/>
 * 具体使用请参考测试用例：{@see com.realaicy.pg.core.repository.UserRepository}
 * <p/>
 * 想要使用该接口需要在spring配置文件的jpa:repositories中添加
 * factory-class="com.realaicy.pg.core.repository.support.SimpleBaseRepositoryFactoryBean"
 * <p/>泛型 ： M 表示实体类型；ID表示主键类型
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
@NoRepositoryBean
public interface BaseRepository<M, ID extends Serializable> extends JpaRepository<M, ID> {

    /**
     * 根据主键删除
     *
     * @param ids 待删除的主键数组
     */
    public void delete(ID[] ids);

    /**
     * 根据条件查询所有
     * 条件 + 分页 + 排序
     *
     * @param searchable 查询请求对象
     * @return a page of entities
     */
    public Page<M> findAll(Searchable searchable);

    /**
     * 根据条件统计所有记录数
     *
     * @param searchable 查询请求对象
     * @return 根据条件统计所有记录数
     */
    public long count(Searchable searchable);

}
