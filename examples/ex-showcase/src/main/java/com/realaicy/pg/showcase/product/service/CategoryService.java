/**
 * Copyright (c) 2005-2012 https://github.com/zhangkaitao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.realaicy.pg.showcase.product.service;

import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.plugin.serivce.BaseMovableService;
import com.realaicy.pg.showcase.product.entity.Category;
import com.realaicy.pg.showcase.product.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-2-4 下午3:01
 * <p>Version: 1.0
 */
@Service
public class CategoryService extends BaseMovableService<Category, Long> {

    @Autowired
    @BaseComponent
    private CategoryRepository categoryRepository;


}
