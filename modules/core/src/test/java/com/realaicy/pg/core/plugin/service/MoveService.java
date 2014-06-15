package com.realaicy.pg.core.plugin.service;

import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.plugin.entity.Move;
import com.realaicy.pg.core.plugin.repository.MoveRepository;
import com.realaicy.pg.core.plugin.serivce.BaseMovableService;
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
public class MoveService extends BaseMovableService<Move, Long> {

    @Autowired
    @BaseComponent
    private MoveRepository moveRepository;

}
