/**
 * Copyright (c) 2005-2012 https://github.com/zhangkaitao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.realaicy.pg.showcase.deleted.service;

import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.service.BaseService;
import com.realaicy.pg.showcase.deleted.entity.DeletedSample;
import com.realaicy.pg.showcase.deleted.repository.DeletedSampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-2-4 下午3:01
 * <p>Version: 1.0
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
