package com.realaicy.pg.showcase.upload.service;

import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.service.BaseService;
import com.realaicy.pg.showcase.upload.entity.Upload;
import com.realaicy.pg.showcase.upload.repository.UploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SD-JPA-Service：上传
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
public class UploadService extends BaseService<Upload, Long> {

    @Autowired
    @BaseComponent
    private UploadRepository uploadRepository;

}
