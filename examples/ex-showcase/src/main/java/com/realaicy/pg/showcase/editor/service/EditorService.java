package com.realaicy.pg.showcase.editor.service;

import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.service.BaseService;
import com.realaicy.pg.showcase.editor.entity.Editor;
import com.realaicy.pg.showcase.editor.repository.EditorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SD-JPA-Service：编辑器
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
public class EditorService extends BaseService<Editor, Long> {

    @Autowired
    @BaseComponent
    private EditorRepository sampleRepository;

}
