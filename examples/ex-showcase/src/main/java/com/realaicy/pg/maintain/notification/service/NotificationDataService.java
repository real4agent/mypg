/**
 * Copyright (c) 2005-2012 https://github.com/zhangkaitao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.realaicy.pg.maintain.notification.service;

import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.service.BaseService;
import com.realaicy.pg.maintain.notification.entity.NotificationData;
import com.realaicy.pg.maintain.notification.repository.NotificationDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-5-22 下午2:40
 * <p>Version: 1.0
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
        if(data == null || data.getRead().equals(Boolean.TRUE)) {
            return;
        }
        data.setRead(Boolean.TRUE);
        update(data);
    }
}
