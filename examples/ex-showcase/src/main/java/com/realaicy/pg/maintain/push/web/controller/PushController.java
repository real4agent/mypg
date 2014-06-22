package com.realaicy.pg.maintain.push.web.controller;

import com.google.common.collect.Maps;
import com.realaicy.pg.maintain.notification.service.NotificationApi;
import com.realaicy.pg.maintain.push.service.PushService;
import com.realaicy.pg.personal.message.service.MessageApi;
import com.realaicy.pg.sys.user.entity.User;
import com.realaicy.pg.sys.user.web.bind.annotation.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * SD-JPA-Controller：推送
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
public class PushController {

    @Autowired
    private MessageApi messageApi;

    @Autowired
    private NotificationApi notificationApi;

    @Autowired
    private PushService pushService;

    /**
     * 获取页面的提示信息
     */
    @RequestMapping(value = "/admin/polling")
    @ResponseBody
    public Object polling(HttpServletResponse resp, @CurrentUser User user) {
        resp.setHeader("Connection", "Keep-Alive");
        resp.addHeader("Cache-Control", "private");
        resp.addHeader("Pragma", "no-cache");

        Long userId = user.getId();
        if (userId == null) {
            return null;
        }
        //如果用户第一次来 立即返回
        if (!pushService.isOnline(userId)) {
            Long unreadMessageCount = messageApi.countUnread(userId);
            List<Map<String, Object>> notifications = notificationApi.topFiveNotification(user.getId());

            Map<String, Object> data = Maps.newHashMap();
            data.put("unreadMessageCount", unreadMessageCount);
            data.put("notifications", notifications);
            pushService.online(userId);
            return data;
        } else {
            //长轮询
            return pushService.newDeferredResult(userId);
        }
    }

}
