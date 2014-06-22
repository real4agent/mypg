package com.realaicy.pg.showcase.tree.service;

import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.plugin.serivce.BaseTreeableService;
import com.realaicy.pg.showcase.tree.entity.Tree;
import com.realaicy.pg.showcase.tree.repository.TreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SD-JPA-Service：树形结构
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
public class TreeService extends BaseTreeableService<Tree, Long> {

    @Autowired
    @BaseComponent
    private TreeRepository treeRepository;

}
