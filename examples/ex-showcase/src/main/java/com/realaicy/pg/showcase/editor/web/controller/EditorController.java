package com.realaicy.pg.showcase.editor.web.controller;

import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.web.controller.BaseCRUDController;
import com.realaicy.pg.showcase.editor.entity.Editor;
import com.realaicy.pg.showcase.editor.service.EditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * SD-JPA-Controller：编辑器
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
@RequestMapping(value = "/showcase/editor")
public class EditorController extends BaseCRUDController<Editor, Long> {

    @Autowired
    @BaseComponent
    private EditorService editorService;

    public EditorController() {
        setResourceIdentity("showcase:editor");
    }

    /**
     * 验证失败返回true
     */
    @Override
    protected boolean hasError(Editor m, BindingResult result) {
        Assert.notNull(m);

        return result.hasErrors();
    }

}
