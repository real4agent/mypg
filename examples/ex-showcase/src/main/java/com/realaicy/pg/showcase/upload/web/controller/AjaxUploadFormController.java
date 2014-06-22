package com.realaicy.pg.showcase.upload.web.controller;

import com.realaicy.pg.core.Constants;
import com.realaicy.pg.showcase.upload.entity.Upload;
import com.realaicy.pg.showcase.upload.service.UploadService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * SD-JPA-Controller：文件上传、下载（Ajax方式）
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
@RequestMapping(value = "showcase/upload/ajax")
public class AjaxUploadFormController {

    @Autowired
    private UploadService uploadService;

    @RequiresPermissions("showcase:upload:create")
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String showCreateForm(Model model) {
        model.addAttribute(Constants.OP_NAME, "新增");
        if (!model.containsAttribute("upload")) {
            model.addAttribute("upload", new Upload());
        }
        return "showcase/upload/ajax/editForm";
    }

    @RequiresPermissions("showcase:upload:create")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(@Valid Upload upload, RedirectAttributes redirectAttributes) {

        uploadService.save(upload);
        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "创建文件成功");
        return "redirect:/showcase/upload";
    }

}
