package com.realaicy.pg.showcase.move.service;

import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.plugin.serivce.BaseMovableService;
import com.realaicy.pg.showcase.move.entity.Move;
import com.realaicy.pg.showcase.move.repository.MoveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SD-JPA-Service：移动
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
public class MoveService extends BaseMovableService<Move, Long> {

    @Autowired
    @BaseComponent
    private MoveRepository moveRepository;

}
