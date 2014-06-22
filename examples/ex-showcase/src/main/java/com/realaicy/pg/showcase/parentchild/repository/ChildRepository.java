package com.realaicy.pg.showcase.parentchild.repository;

import com.realaicy.pg.core.repository.BaseRepository;
import com.realaicy.pg.showcase.parentchild.entity.Child;
import com.realaicy.pg.showcase.parentchild.entity.Parent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * SD-JPA-Repository：儿子
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public interface ChildRepository extends BaseRepository<Child, Long> {

    @Query(value = "select o from Child o where o.parent=?1")
    Page<Child> findByParent(Parent parent, Pageable pageable);

    @Query(value = "select o from Child o where o.parent in(?1)")
    Page<Child> findByParents(List<Parent> parents, Pageable pageable);

    @Modifying
    @Query(value = "delete from Child where parent = ?1")
    void deleteByParent(Parent parent);
}
