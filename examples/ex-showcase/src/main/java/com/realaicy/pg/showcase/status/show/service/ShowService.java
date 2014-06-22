package com.realaicy.pg.showcase.status.show.service;

import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.service.BaseService;
import com.realaicy.pg.showcase.status.show.entity.Show;
import com.realaicy.pg.showcase.status.show.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SD-JPA-Service：显示状态
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
public class ShowService extends BaseService<Show, Long> {

    @Autowired
    @BaseComponent
    private ShowRepository sampleRepository;

}
