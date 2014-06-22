package com.realaicy.pg.maintain.notification.service;

import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.service.BaseService;
import com.realaicy.pg.maintain.notification.entity.NotificationTemplate;
import com.realaicy.pg.maintain.notification.repository.NotificationTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SD-JPA-Service：通知模板
 * <p/>
 * 提供如下服务：<br/>
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
public class NotificationTemplateService extends BaseService<NotificationTemplate, Long> {

    @Autowired
    @BaseComponent
    private NotificationTemplateRepository notificationTemplateRepository;

    public NotificationTemplate findByName(final String name) {
        return notificationTemplateRepository.findByName(name);
    }
}
