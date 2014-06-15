package com.realaicy.pg.sys.user.web.controller;

import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.web.controller.BaseCRUDController;
import com.realaicy.pg.sys.user.entity.UserLastOnline;
import com.realaicy.pg.sys.user.service.UserLastOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 上一次在线相关信息
 * SD-JPA-Controller：上一次在线相关信息
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
@SuppressWarnings("UnusedDeclaration")
@Controller
@RequestMapping(value = "/admin/sys/user/lastOnline")
public class UserLastOnlineController extends BaseCRUDController<UserLastOnline, Long> {
    @Autowired
    @BaseComponent
    private UserLastOnlineService userLastOnlineService;

    public UserLastOnlineController() {
        setResourceIdentity("sys:userLastOnline");
    }
}
