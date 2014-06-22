package com.realaicy.pg.maintain.icon.service;

import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.service.BaseService;
import com.realaicy.pg.maintain.icon.entity.Icon;
import com.realaicy.pg.maintain.icon.repository.IconRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SD-JPA-Service：资源
 * <p/>
 * 提供如下服务：<br/>
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
public class IconService extends BaseService<Icon, Long> {

    @Autowired
    @BaseComponent
    private IconRepository iconRepository;

    public Icon findByIdentity(String identity) {
        return iconRepository.findByIdentity(identity);
    }
}
