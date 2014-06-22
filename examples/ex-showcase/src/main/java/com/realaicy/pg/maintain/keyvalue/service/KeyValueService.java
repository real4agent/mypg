package com.realaicy.pg.maintain.keyvalue.service;

import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.service.BaseService;
import com.realaicy.pg.maintain.keyvalue.entity.KeyValue;
import com.realaicy.pg.maintain.keyvalue.repository.KeyValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SD-JPA-Service：keyValue
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
public class KeyValueService extends BaseService<KeyValue, Long> {

    @Autowired
    @BaseComponent
    private KeyValueRepository keyValueRepository;

    public KeyValue findByKey(String key) {
        return keyValueRepository.findByKey(key);
    }

}
