package com.realaicy.pg.showcase.product.web.controller;

import com.realaicy.pg.core.entity.enums.BooleanEnum;
import com.realaicy.pg.core.entity.search.Searchable;
import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.plugin.web.controller.BaseMovableController;
import com.realaicy.pg.core.web.bind.annotation.PageableDefaults;
import com.realaicy.pg.showcase.product.entity.Category;
import com.realaicy.pg.showcase.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * SD-JPA-Controller：产品类别
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
@Controller
@RequestMapping(value = "/showcase/product/category")
public class CategoryController extends BaseMovableController<Category, Long> {

    @Autowired
    @BaseComponent
    private CategoryService categoryService;

    public CategoryController() {
        setResourceIdentity("showcase:productCategory");
    }

    //selectType  multiple single
    @RequestMapping(value = {"select/{selectType}", "select"}, method = RequestMethod.GET)
    @PageableDefaults(sort = "weight=desc")
    public String select(
            Searchable searchable, Model model,
            @PathVariable(value = "selectType") String selectType,
            @MatrixVariable(value = "domId", pathVar = "selectType") String domId,
            @MatrixVariable(value = "domName", pathVar = "selectType", required = false) String domName) {

        this.permissionList.assertHasViewPermission();

        model.addAttribute("selectType", selectType);
        model.addAttribute("domId", domId);
        model.addAttribute("domName", domName);

        super.list(searchable, model);
        return "showcase/product/category/select";
    }

    @Override
    protected void setCommonData(Model model) {
        model.addAttribute("booleanList", BooleanEnum.values());
    }
}
