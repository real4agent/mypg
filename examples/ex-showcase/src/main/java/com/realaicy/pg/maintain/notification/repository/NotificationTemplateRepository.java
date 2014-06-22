package com.realaicy.pg.maintain.notification.repository;

import com.realaicy.pg.core.repository.BaseRepository;
import com.realaicy.pg.maintain.notification.entity.NotificationTemplate;
import org.springframework.data.jpa.repository.Query;

/**
 * SD-JPA-Repository：通知模板
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public interface NotificationTemplateRepository extends BaseRepository<NotificationTemplate, Long> {

    @Query("from NotificationTemplate o where name=?1")
    NotificationTemplate findByName(String name);
}
