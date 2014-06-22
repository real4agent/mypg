package com.realaicy.pg.showcase.move.web.controller;

import com.realaicy.pg.core.entity.enums.BooleanEnum;
import com.realaicy.pg.core.entity.validate.group.Create;
import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.plugin.web.controller.BaseMovableController;
import com.realaicy.pg.showcase.move.entity.Move;
import com.realaicy.pg.showcase.move.service.MoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * SD-JPA-Controller：移动
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
@RequestMapping(value = "/showcase/move")
public class MoveController extends BaseMovableController<Move, Long> {

    @Autowired
    @BaseComponent
    private MoveService moveService;

    public MoveController() {
        setResourceIdentity("showcase:move");
    }

    public void setCommonData(Model model) {
        model.addAttribute("booleanList", BooleanEnum.values());
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    @Override
    public String create(Model model,
                         //表示只验证Create.class分组
                         @Validated(value = Create.class) @Valid Move move, BindingResult result,
                         RedirectAttributes redirectAttributes) {
        return super.create(model, move, result, redirectAttributes);
    }

}
