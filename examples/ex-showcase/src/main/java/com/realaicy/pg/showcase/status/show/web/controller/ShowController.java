package com.realaicy.pg.showcase.status.show.web.controller;

import com.realaicy.pg.core.Constants;
import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.plugin.entity.Stateable;
import com.realaicy.pg.core.web.controller.BaseCRUDController;
import com.realaicy.pg.showcase.status.show.entity.Show;
import com.realaicy.pg.showcase.status.show.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * SD-JPA-Controller：显示状态
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
@RequestMapping(value = "/showcase/status/show")
public class ShowController extends BaseCRUDController<Show, Long> {

    @Autowired
    @BaseComponent
    private ShowService showService;

    public ShowController() {
        setListAlsoSetCommonData(true);
        setResourceIdentity("showcase:statusShow");
    }

    @RequestMapping(value = "status/{status}", method = RequestMethod.GET)
    public String audit(
            HttpServletRequest request,
            @RequestParam("ids") Long[] ids,
            @PathVariable("status") Stateable.ShowStatus status,
            RedirectAttributes redirectAttributes
    ) {

        this.permissionList.assertHasPermission("audit");

        for (Long id : ids) {
            Show show = showService.findOne(id);
            show.setStatus(status);
            showService.update(show);
        }

        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "操作成功！");

        return "redirect:" + request.getAttribute(Constants.BACK_URL);
    }

    @Override
    protected void setCommonData(Model model) {
        model.addAttribute("statusList", Stateable.ShowStatus.values());
    }

    /**
     * 验证失败返回true
     */
    @Override
    protected boolean hasError(Show m, BindingResult result) {
        Assert.notNull(m);

        return result.hasErrors();
    }

}
