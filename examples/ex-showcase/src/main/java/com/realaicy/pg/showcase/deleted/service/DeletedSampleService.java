package com.realaicy.pg.showcase.deleted.service;

import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.service.BaseService;
import com.realaicy.pg.showcase.deleted.entity.DeletedSample;
import com.realaicy.pg.showcase.deleted.repository.DeletedSampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SD-JPA-Service：样例
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
public class DeletedSampleService extends BaseService<DeletedSample, Long> {

    @Autowired
    @BaseComponent
    private DeletedSampleRepository sampleRepository;

    public DeletedSample findByName(String name) {
        return sampleRepository.findByName(name);
    }

}
