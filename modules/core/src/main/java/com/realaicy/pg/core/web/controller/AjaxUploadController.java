package com.realaicy.pg.core.web.controller;

import com.realaicy.pg.core.Constants;
import com.realaicy.pg.core.utils.ImagesUtils;
import com.realaicy.pg.core.utils.LogUtils;
import com.realaicy.pg.core.utils.MessageUtils;
import com.realaicy.pg.core.web.entity.AjaxUploadResponse;
import com.realaicy.pg.core.web.upload.FileUploadUtils;
import com.realaicy.pg.core.web.upload.exception.FileNameLengthLimitExceededException;
import com.realaicy.pg.core.web.upload.exception.InvalidExtensionException;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * ajax文件上传/下载
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
public class AjaxUploadController {

    //允许的文件内容类型
    private String[] allowedExtension = FileUploadUtils.DEFAULT_ALLOWED_EXTENSION;
    //文件上传下载的父目录
    private String baseDir = FileUploadUtils.getDefaultBaseDir();

    @RequestMapping(value = "ajaxUpload", method = RequestMethod.POST)
    @ResponseBody
    public AjaxUploadResponse ajaxUpload(
            HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "files[]", required = false) MultipartFile[] files) {

        //The file upload plugin makes use of an Iframe Transport module for browsers like Microsoft Internet Explorer and Opera, which do not yet support XMLHTTPRequest file uploads.
        response.setContentType("text/plain");

        AjaxUploadResponse ajaxUploadResponse = new AjaxUploadResponse();

        if (ArrayUtils.isEmpty(files)) {
            return ajaxUploadResponse;
        }

        for (MultipartFile file : files) {
            String filename = file.getOriginalFilename();
            long size = file.getSize();

            try {
                long maxSize = FileUploadUtils.DEFAULT_MAX_SIZE;
                String url = FileUploadUtils.upload(request, baseDir, file, allowedExtension, maxSize, true);
                String deleteURL = "/ajaxUpload/delete?filename=" + URLEncoder.encode(url, Constants.ENCODING);
                if (ImagesUtils.isImage(filename)) {
                    ajaxUploadResponse.add(filename, size, url, url, deleteURL);
                } else {
                    ajaxUploadResponse.add(filename, size, url, deleteURL);
                }
                //continue;
            } catch (IOException e) {
                LogUtils.logError("file upload error", e);
                ajaxUploadResponse.add(filename, size, MessageUtils.message("upload.server.error"));
                //continue;
            } catch (InvalidExtensionException e) {
                ajaxUploadResponse.add(filename, size, MessageUtils.message("upload.not.allow.extension"));
                //continue;
            } catch (FileUploadBase.FileSizeLimitExceededException e) {
                ajaxUploadResponse.add(filename, size, MessageUtils.message("upload.exceed.maxSize"));
                //continue;
            } catch (FileNameLengthLimitExceededException e) {
                ajaxUploadResponse.add(filename, size, MessageUtils.message("upload.filename.exceed.length"));
                //continue;
            }
        }
        return ajaxUploadResponse;
    }

    @RequestMapping(value = "ajaxUpload/delete", method = RequestMethod.POST)
    @ResponseBody
    public String ajaxUploadDelete(
            HttpServletRequest request,
            @RequestParam(value = "filename") String filename) throws Exception {

        if (StringUtils.isEmpty(filename) || filename.contains("\\.\\.")) {
            return "";
        }
        filename = URLDecoder.decode(filename, Constants.ENCODING);

        String filePath = FileUploadUtils.extractUploadDir(request) + "/" + filename;

        File file = new File(filePath);
        file.deleteOnExit();

        return "";
    }
}
