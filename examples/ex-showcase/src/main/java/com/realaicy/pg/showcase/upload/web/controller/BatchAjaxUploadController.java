package com.realaicy.pg.showcase.upload.web.controller;

import com.realaicy.pg.core.Constants;
import com.realaicy.pg.core.web.upload.FileUploadUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * SD-JPA-Controller：ajax批量文件上传、下载
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
public class BatchAjaxUploadController {

    //最大上传大小 字节为单位
    private long maxSize = FileUploadUtils.DEFAULT_MAX_SIZE;
    //允许的文件内容类型
    private String[] allowedExtension = FileUploadUtils.DEFAULT_ALLOWED_EXTENSION;
    //文件上传下载的父目录
    private String baseDir = FileUploadUtils.getDefaultBaseDir();

    @RequiresPermissions("showcase:upload:create")
    @RequestMapping(value = "ajaxUpload", method = RequestMethod.GET)
    public String showAjaxUploadForm(Model model) {
        model.addAttribute(Constants.OP_NAME, "新增");
        return "showcase/upload/ajax/uploadForm";
    }

}
