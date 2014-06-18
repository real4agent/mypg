package com.realaicy.pg.personal.calendar.service;

import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.service.BaseService;
import com.realaicy.pg.personal.calendar.entity.MyCalendar;
import com.realaicy.pg.personal.calendar.repository.CalendarRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * SD-JPA-Service：日历
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
public class CalendarService extends BaseService<MyCalendar, Long> {

    @Autowired
    @BaseComponent
    private CalendarRepository calendarRepository;

    public void copyAndRemove(MyCalendar calendar) {

        delete(calendar);

        MyCalendar copyCalendar = new MyCalendar();
        BeanUtils.copyProperties(calendar, copyCalendar);
        copyCalendar.setId(null);
        save(copyCalendar);
    }


    public Long countRecentlyCalendar(Long userId, Integer interval) {
        Date nowDate = new Date();
//        nowDate.setHours(0);
//        nowDate.setMinutes(0);
//        nowDate.setSeconds(0);

        return calendarRepository.countRecentlyCalendar(userId, nowDate,interval);
    }
}
