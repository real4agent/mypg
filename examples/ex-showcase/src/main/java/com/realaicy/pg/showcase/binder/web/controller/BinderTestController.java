package com.realaicy.pg.showcase.binder.web.controller;

import com.realaicy.pg.sys.user.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * SD-JPA-Controller：绑定测试
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
@RequestMapping("/binder")
public class BinderTestController {

    //http://localhost:9080/pg-web/binder/test1?user1.id=1&user2.id=2
    @RequestMapping("/test1")
    public String test1(@ModelAttribute("user1") User user1, @ModelAttribute("user2") User user2) {
        System.out.println(user1);
        System.out.println(user2);
        return "";
    }

    //http://localhost:9080/pg-web/binder/test2?user3.id=1&user3.username=123
    @RequestMapping("/test2")
    public String test2(@ModelAttribute("user3") User user3) {
        System.out.println(user3);
        return "";
    }

    @RequestMapping("/test3")
    public String test3(@ModelAttribute("user4") User user3) {
        System.out.println(user3);
        return "";
    }

    @InitBinder("user1")
    public void initBinder1(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("user1.");
    }

    @InitBinder("user2")
    public void initBinder2(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("user2.");
    }

    @InitBinder("user3")
    public void initBinder3(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("user3.");
        binder.setAllowedFields("id");
    }

    @InitBinder("user4")
    public void initBinder4(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("user4.");
        binder.setDisallowedFields("id");

    }

}
