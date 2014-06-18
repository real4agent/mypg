package com.realaicy.pg.personal.calendar.repository;

import com.realaicy.pg.core.repository.BaseRepository;
import com.realaicy.pg.personal.calendar.entity.MyCalendar;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public interface CalendarRepository extends BaseRepository<MyCalendar, Long> {

    /*SELECT start_date,length from personal_calendar WHERE user_id=1 and (
            ((start_date BETWEEN DATE(NOW()) and DATE_ADD(start_date,INTERVAL 3 DAY)) )
    OR (start_date < DATE(NOW()) AND DATE_ADD(start_date,INTERVAL length DAY) >= DATE_SUB(NOW(),INTERVAL 3 DAY))
            )*/

    @SuppressWarnings("JpaQlInspection")
    @Query(value = "select count(id) from personal_calendar where user_id=?1 and" +
            " (" +
            "start_date BETWEEN DATE(?2) and DATE_ADD(start_date,INTERVAL ?3 DAY)" +
            ")", nativeQuery = true)
    Long countRecentlyCalendar(Long userId, Date nowDate, Integer interval);
}
