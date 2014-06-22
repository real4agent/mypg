package com.realaicy.pg.showcase.product.service;

import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.plugin.serivce.BaseMovableService;
import com.realaicy.pg.showcase.product.entity.Category;
import com.realaicy.pg.showcase.product.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SD-JPA-Service：产品类别
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
public class CategoryService extends BaseMovableService<Category, Long> {

    @Autowired
    @BaseComponent
    private CategoryRepository categoryRepository;


}
