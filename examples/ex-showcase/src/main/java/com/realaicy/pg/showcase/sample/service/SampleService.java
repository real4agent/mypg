package com.realaicy.pg.showcase.sample.service;

import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.service.BaseService;
import com.realaicy.pg.showcase.sample.entity.Sample;
import com.realaicy.pg.showcase.sample.repository.SampleRepository;
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
public class SampleService extends BaseService<Sample, Long> {

    @Autowired
    @BaseComponent
    private SampleRepository sampleRepository;

    public Sample findByName(String name) {
        return sampleRepository.findByName(name);
    }

}
