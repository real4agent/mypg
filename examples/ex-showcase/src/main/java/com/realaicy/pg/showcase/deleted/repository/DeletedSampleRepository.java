/**
 * Copyright (c) 2005-2012 https://github.com/zhangkaitao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.realaicy.pg.showcase.deleted.repository;

import com.realaicy.pg.core.repository.BaseRepository;
import com.realaicy.pg.showcase.deleted.entity.DeletedSample;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-2-4 下午3:00
 * <p>Version: 1.0
 */
public interface DeletedSampleRepository extends BaseRepository<DeletedSample, Long> {

    DeletedSample findByName(String name);

}