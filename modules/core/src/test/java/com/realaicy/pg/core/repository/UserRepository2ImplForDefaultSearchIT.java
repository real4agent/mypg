package com.realaicy.pg.core.repository;

import com.realaicy.pg.core.entity.Sex;
import com.realaicy.pg.core.entity.User;
import com.realaicy.pg.core.entity.search.SearchRequest;
import com.realaicy.pg.core.entity.search.Searchable;
import com.realaicy.pg.core.test.BaseUserIT;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * <p>User Repository2集成测试</p>
 * <p>测试时使用内嵌的HSQL内存数据库完成</p>
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */

public class UserRepository2ImplForDefaultSearchIT extends BaseUserIT {

    @Autowired
    private UserRepository2 userRepository2;

    @Test
    public void testEqForEnum() {
        int count = 15;
        for (int i = 0; i < count; i++) {
            User user = createUser();
            user.getBaseInfo().setSex(Sex.male);
            userRepository2.save(user);
        }
        Searchable search = Searchable.newSearchable().addSearchParam("baseInfo.sex_eq", "male");
        assertEquals(count, userRepository2.countAllByDefault(search));
    }

    @Test
    public void testNotEqForEnum() {
        int count = 15;
        for (int i = 0; i < count; i++) {
            User user = createUser();
            user.getBaseInfo().setSex(Sex.male);
            userRepository2.save(user);
        }
        Searchable search = Searchable.newSearchable().addSearchParam("baseInfo.sex_ne", "male");
        assertEquals(0, userRepository2.countAllByDefault(search));
    }

    @Test
    public void testEqForInteger() {
        int count = 15;
        for (int i = 0; i < count; i++) {
            User user = createUser();
            user.getBaseInfo().setAge(15);
            userRepository2.save(user);
        }
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("baseInfo.age_eq", "15");
        SearchRequest search = new SearchRequest(searchParams);
        assertEquals(count, userRepository2.countAllByDefault(search));
    }

    @Test
    public void testEqForDate() throws ParseException {
        int count = 15;
        String dateStr = "2012-01-15 16:59:00";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < count; i++) {
            User user = createUser();
            user.setRegisterDate(df.parse(dateStr));
            userRepository2.save(user);
        }

        Searchable search = Searchable.newSearchable().addSearchParam("registerDate_eq", dateStr);
        assertEquals(count, userRepository2.countAllByDefault(search));
    }

    @Test
    public void testLtAndGtForDate() throws ParseException {
        int count = 15;
        String dateStr = "2012-01-15 16:59:00";
        String dateStrFrom = "2012-01-15 16:58:00";
        String dateStrEnd = "2012-01-15 16:59:01";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < count; i++) {
            User user = createUser();
            user.getBaseInfo().setBirthday(new Timestamp(df.parse(dateStr).getTime()));
            userRepository2.save(user);
        }
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("baseInfo.birthday_gt", dateStrFrom);
        searchParams.put("baseInfo.birthday_lt", dateStrEnd);
        Searchable search = Searchable.newSearchable(searchParams);
        assertEquals(count, userRepository2.countAllByDefault(search));
    }

    @Test
    public void testLteAndGteForDate() throws ParseException {
        int count = 15;
        String dateStr = "2012-01-15 16:59:00";
        String dateStrFrom = "2012-01-15 16:59:00";
        String dateStrEnd = "2012-01-15 16:59:00";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < count; i++) {
            User user = createUser();
            user.getBaseInfo().setBirthday(new Timestamp(df.parse(dateStr).getTime()));
            userRepository2.save(user);
        }
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("baseInfo.birthday_gte", dateStrFrom);
        searchParams.put("baseInfo.birthday_lte", dateStrEnd);
        Searchable search = Searchable.newSearchable(searchParams);
        assertEquals(count, userRepository2.countAllByDefault(search));
    }

    @Test
    public void testIsNotNull() throws ParseException {
        int count = 15;
        for (int i = 0; i < count; i++) {
            User user = createUser();
            userRepository2.save(user);
        }
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("username_isNotNull", null);
        Searchable search = Searchable.newSearchable(searchParams);
        assertEquals(count, userRepository2.countAllByDefault(search));
    }

    @Test
    public void testInWithList() throws ParseException {
        List<Long> uuids = new ArrayList<Long>();
        int count = 15;
        for (int i = 0; i < count; i++) {
            User user = createUser();
            userRepository2.save(user);
            uuids.add(user.getId());
        }
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("id_in", uuids);
        Searchable search = Searchable.newSearchable(searchParams);
        assertEquals(count, userRepository2.countAllByDefault(search));
    }

    @Test
    public void testInWithArray() throws ParseException {
        List<Long> uuids = new ArrayList<Long>();
        int count = 15;
        for (int i = 0; i < count; i++) {
            User user = createUser();
            userRepository2.save(user);
            uuids.add(user.getId());
        }
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("id_in", uuids.toArray(new Long[count]));
        Searchable search = Searchable.newSearchable(searchParams);
        assertEquals(count, userRepository2.countAllByDefault(search));
    }

    @Test
    public void testInWithSingleValue() throws ParseException {
        List<Long> uuids = new ArrayList<Long>();
        int count = 15;
        for (int i = 0; i < count; i++) {
            User user = createUser();
            userRepository2.save(user);
            uuids.add(user.getId());
        }
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("id_in", uuids.get(0));
        Searchable search = Searchable.newSearchable(searchParams);
        assertEquals(1, userRepository2.countAllByDefault(search));
    }

}
