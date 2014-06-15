package com.realaicy.pg.core.plugin.service;

import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.plugin.entity.Tree;
import com.realaicy.pg.core.plugin.repository.TreeRepository;
import com.realaicy.pg.core.plugin.serivce.BaseTreeableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
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
