package com.realaicy.pg.maintain.notification.service;

import com.realaicy.pg.maintain.notification.exception.TemplateNotFoundException;

import java.util.List;
import java.util.Map;

/**
 * 通知接口
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public interface NotificationApi {

    /**
     * 发送通知
     *
     * @param userId 接收人用户编号
     * @param templateName 模板名称
     * @param context 模板需要的数据
     * @throws TemplateNotFoundException 没有找到相应模板
     */
    public void notify(Long userId, String templateName, Map<String, Object> context) throws TemplateNotFoundException;

    /**
     * id :
     * title
     * content
     * date
     *
     * @param userId xxx
     * @return xxx
     */
    public List<Map<String, Object>> topFiveNotification(Long userId);
}
