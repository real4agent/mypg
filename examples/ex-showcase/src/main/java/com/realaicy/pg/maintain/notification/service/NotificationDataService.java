package com.realaicy.pg.maintain.notification.service;

import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.service.BaseService;
import com.realaicy.pg.maintain.notification.entity.NotificationData;
import com.realaicy.pg.maintain.notification.repository.NotificationDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SD-JPA-Service：通知
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
public class NotificationDataService extends BaseService<NotificationData, Long> {

    @Autowired
    @BaseComponent
    private NotificationDataRepository notificationDataRepository;

    public void markReadAll(final Long userId) {
        notificationDataRepository.markReadAll(userId);
    }

    public void markRead(final Long notificationId) {
        NotificationData data = findOne(notificationId);
        if (data == null || data.getRead().equals(Boolean.TRUE)) {
            return;
        }
        data.setRead(Boolean.TRUE);
        update(data);
    }
}
