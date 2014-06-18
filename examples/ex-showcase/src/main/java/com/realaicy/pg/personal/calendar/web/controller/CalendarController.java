package com.realaicy.pg.personal.calendar.web.controller;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.realaicy.pg.core.entity.search.Searchable;
import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.web.controller.BaseController;
import com.realaicy.pg.personal.calendar.entity.MyCalendar;
import com.realaicy.pg.personal.calendar.service.CalendarService;
import com.realaicy.pg.sys.user.entity.User;
import com.realaicy.pg.sys.user.web.bind.annotation.CurrentUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 控制器：日历
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
@RequestMapping(value = "/admin/personal/calendar")
public class CalendarController extends BaseController<MyCalendar, Long> {

    private static final long oneDayMillis = 24L * 60 * 60 * 1000;
    private static final String dataFormat = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    @BaseComponent
    private CalendarService calendarService;

    @RequestMapping()
    public String list() {
        return viewName("list");
    }

    @RequestMapping("/load")
    @ResponseBody
    public Collection<Map> ajaxLoad(Searchable searchable, @CurrentUser User loginUser) {

        searchable.addSearchParam("userId_eq", loginUser.getId());
        List<MyCalendar> calendarList = calendarService.findAllWithNoPageNoSort(searchable);

        return Lists.transform(calendarList, new Function<MyCalendar, Map>() {
            @SuppressWarnings("deprecation")
            @Override
            public Map apply(MyCalendar c) {

                Map<String, Object> m = Maps.newHashMap();

                DateTime startDate = new DateTime(c.getStartDate());
                DateTime endDate = startDate.plusDays(c.getLength());

                Boolean allDays = c.getStartTime() == null && c.getEndTime() == null;

                if (!allDays) {
                    startDate.withHourOfDay(c.getStartTime().getHours());
                    startDate.withMinuteOfHour(c.getStartTime().getMinutes());
                    startDate.withSecondOfMinute(c.getStartTime().getSeconds());

                    endDate.withHourOfDay(c.getEndTime().getHours());
                    endDate.withMinuteOfHour(c.getEndTime().getMinutes());
                    endDate.withSecondOfMinute(c.getEndTime().getSeconds());
                }
                m.put("id", c.getId());
                m.put("start", startDate.toString("yyyy-MM-dd HH:mm:ss"));
                m.put("start", endDate.toString("yyyy-MM-dd HH:mm:ss"));

                m.put("allDay", allDays);
                m.put("title", c.getTitle());
                m.put("details", c.getDetails());

                if (StringUtils.isNotEmpty(c.getBackgroundColor())) {
                    m.put("backgroundColor", c.getBackgroundColor());
                    m.put("borderColor", c.getBackgroundColor());
                }
                if (StringUtils.isNotEmpty(c.getTextColor())) {
                    m.put("textColor", c.getTextColor());
                }

                return m;
            }
        });
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String viewCalendar(@PathVariable("id") MyCalendar calendar, Model model) {
        model.addAttribute("calendar", calendar);
        return viewName("view");
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String showNewForm(
            @RequestParam(value = "start", required = false) @DateTimeFormat(pattern = dataFormat) Date start,
            @RequestParam(value = "end", required = false) @DateTimeFormat(pattern = dataFormat) Date end,
            Model model) {

        setColorList(model);

        MyCalendar calendar = new MyCalendar();
        calendar.setLength(1);
        if (start != null) {
            calendar.setStartDate(start);
            calendar.setLength((int) Math.ceil(1.0 * (end.getTime() - start.getTime()) / oneDayMillis));
            if (DateUtils.isSameDay(start, end)) {
                calendar.setLength(1);
            }
            if (!"00:00:00".equals(DateFormatUtils.format(start, "HH:mm:ss"))) {
                calendar.setStartTime(start);
            }
            if (!"00:00:00".equals(DateFormatUtils.format(end, "HH:mm:ss"))) {
                calendar.setEndTime(end);
            }

        }
        model.addAttribute("model", calendar);
        return viewName("newForm");
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    @ResponseBody
    public String newCalendar(@ModelAttribute("calendar") MyCalendar calendar, @CurrentUser User loginUser) {
        calendar.setUserId(loginUser.getId());
        calendarService.save(calendar);
        return "ok";
    }

    @RequestMapping(value = "/move", method = RequestMethod.POST)
    @ResponseBody
    public String moveCalendar(
            @RequestParam("id") Long id,
            @RequestParam(value = "start", required = false) @DateTimeFormat(pattern = dataFormat) Date start,
            @RequestParam(value = "end", required = false) @DateTimeFormat(pattern = dataFormat) Date end
    ) {
        MyCalendar calendar = calendarService.findOne(id);

        if (end == null) {
            end = start;
        }

        calendar.setStartDate(start);
        calendar.setLength((int) Math.ceil(1.0 * (end.getTime() - start.getTime()) / oneDayMillis));
        if (DateUtils.isSameDay(start, end)) {
            calendar.setLength(1);
        }
        if (!"00:00:00".equals(DateFormatUtils.format(start, "HH:mm:ss"))) {
            calendar.setStartTime(start);
        }
        if (!"00:00:00".equals(DateFormatUtils.format(end, "HH:mm:ss"))) {
            calendar.setEndTime(end);
        }
        calendarService.copyAndRemove(calendar);

        return "ok";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String deleteCalendar(@RequestParam("id") Long id) {
        calendarService.delete(id);
        return "ok";
    }

    private void setColorList(Model model) {
        List<String> backgroundColorList = Lists.newArrayList();
        backgroundColorList.add("#3a87ad");
        backgroundColorList.add("#0d7813");
        backgroundColorList.add("#f2a640");
        backgroundColorList.add("#b373b3");
        backgroundColorList.add("#f2a640");
        backgroundColorList.add("#668cb3");
        backgroundColorList.add("#28754e");
        backgroundColorList.add("#8c66d9");

        model.addAttribute("backgroundColorList", backgroundColorList);
    }

}
