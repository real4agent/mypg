package com.realaicy.pg.showcase.status.audit.web.controller;

import com.realaicy.pg.core.Constants;
import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.plugin.entity.Stateable;
import com.realaicy.pg.core.web.controller.BaseCRUDController;
import com.realaicy.pg.showcase.status.audit.entity.Audit;
import com.realaicy.pg.showcase.status.audit.service.AuditService;
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
import java.util.ArrayList;
import java.util.List;

/**
 * SD-JPA-Controller：审计状态
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
@RequestMapping(value = "/showcase/status/audit")
public class AuditController extends BaseCRUDController<Audit, Long> {

    @Autowired
    @BaseComponent
    private AuditService auditService;

    public AuditController() {
        setListAlsoSetCommonData(true);
        setResourceIdentity("showcase:statusAudit");
    }

    @RequestMapping(value = "status/{status}", method = RequestMethod.GET)
    public String audit(
            HttpServletRequest request,
            @RequestParam("ids") Long[] ids,
            @PathVariable("status") Stateable.AuditStatus status,
            @RequestParam(value = "comment", required = false) String comment,
            RedirectAttributes redirectAttributes
    ) {

        this.permissionList.assertHasPermission("audit");

        List<Audit> auditList = new ArrayList<Audit>();
        for (Long id : ids) {
            Audit audit = auditService.findOne(id);
            if (audit.getStatus() != Stateable.AuditStatus.waiting) {
                redirectAttributes.addFlashAttribute(Constants.ERROR, "数据中有已通过审核的，不能重复审核！");
                return "redirect:" + request.getAttribute(Constants.BACK_URL);
            }
            auditList.add(audit);
        }
        for (Audit audit : auditList) {
            audit.setStatus(status);
            audit.setComment(comment);
            auditService.update(audit);
        }

        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "操作成功！");

        return "redirect:" + request.getAttribute(Constants.BACK_URL);
    }

    @Override
    protected void setCommonData(Model model) {
        model.addAttribute("statusList", Stateable.AuditStatus.values());
    }

    /**
     * 验证失败返回true
     */
    @Override
    protected boolean hasError(Audit m, BindingResult result) {
        Assert.notNull(m);

        return result.hasErrors();
    }

}
