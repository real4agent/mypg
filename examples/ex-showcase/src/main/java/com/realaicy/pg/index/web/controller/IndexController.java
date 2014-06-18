package com.realaicy.pg.index.web.controller;

import com.realaicy.pg.maintain.push.service.PushApi;
import com.realaicy.pg.personal.calendar.service.CalendarService;
import com.realaicy.pg.personal.message.service.MessageService;
import com.realaicy.pg.sys.resource.entity.tmp.Menu;
import com.realaicy.pg.sys.resource.service.ResourceService;
import com.realaicy.pg.sys.user.entity.User;
import com.realaicy.pg.sys.user.web.bind.annotation.CurrentUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 控制器：首页
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
@Controller("adminIndexController")
@RequestMapping("/admin")
public class IndexController {

    private static final Logger log = LoggerFactory.getLogger("pg-debug-sys");

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private PushApi pushApi;

    @Autowired
    private MessageService messageService;

    @Autowired
    private CalendarService calendarService;

    @RequestMapping(value = {"/{index}"})
    public String index(@CurrentUser User user, Model model) {

        //首先加载用户的系统菜单，user对象是在SysUserFilter过滤器中存入的
        List<Menu> menus = resourceService.findMenus(user);
        log.debug("user=={}", user.getUsername());
        if (log.isDebugEnabled()) {
            StringBuilder strBuilder = new StringBuilder();
            for (Menu aMenu : menus) {
                strBuilder.append("(").append(aMenu.getName()).append(")").append("||");
            }
            log.debug("menu=={}", strBuilder.toString());

        }
//        //noinspection SuspiciousToArrayCall
//        log.debug("user=={} \t userPermissions==\t{}", user.getUsername(),
//                Arrays.toString(menus.toArray(new String[menus.size()])));

        model.addAttribute("menus", menus);

        pushApi.offline(user.getId());

        return "admin/index/index";
    }

    @RequestMapping(value = "/welcome")
    public String welcome(@CurrentUser User loginUser, Model model) {

        //未读消息
        Long messageUnreadCount = messageService.countUnread(loginUser.getId());
        model.addAttribute("messageUnreadCount", messageUnreadCount);

        //最近3天的日历
        model.addAttribute("calendarCount", calendarService.countRecentlyCalendar(loginUser.getId(), 2));

        return "admin/index/welcome";
    }

}
