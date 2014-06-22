package com.realaicy.pg.showcase.parentchild.service;

import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.service.BaseService;
import com.realaicy.pg.showcase.parentchild.entity.Child;
import com.realaicy.pg.showcase.parentchild.entity.Parent;
import com.realaicy.pg.showcase.parentchild.repository.ChildRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SD-JPA-Service：儿子
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
@Service
public class ChildService extends BaseService<Child, Long> {

    @Autowired
    @BaseComponent
    private ChildRepository childRepository;


    public ChildService() {
    }

    public Page<Child> findByParent(Parent parent, Pageable pageable) {
        return childRepository.findByParent(parent, pageable);
    }

    Page<Child> findByParents(List<Parent> parents, Pageable pageable) {
        return childRepository.findByParents(parents, pageable);
    }


    public void deleteByParent(Parent parent) {
        childRepository.deleteByParent(parent);
    }
}
