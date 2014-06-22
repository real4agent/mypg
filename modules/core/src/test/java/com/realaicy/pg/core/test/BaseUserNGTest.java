package com.realaicy.pg.core.test;

import com.realaicy.pg.core.entity.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 用户测试基类
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public abstract class BaseUserNGTest extends BaseTransactionNGTest {

    public User createUser() {
        User user = new User();
        user.setUsername("liuxudong$$$" + System.nanoTime() + RandomStringUtils.random(10));
        user.setPassword("123456");
        user.setRegisterDate(new Date());
        BaseInfo baseInfo = new BaseInfo();
        baseInfo.setRealname("liuxudong");
        baseInfo.setSex(Sex.male);
        baseInfo.setBirthday(new Timestamp(System.currentTimeMillis()));
        baseInfo.setAge(15);
        user.setBaseInfo(baseInfo);

        SchoolInfo primary = new SchoolInfo();
        primary.setName("abc");
        primary.setType(SchoolType.primary_school);
        user.addSchoolInfo(primary);

        SchoolInfo secondary = new SchoolInfo();
        secondary.setName("bcd");
        secondary.setType(SchoolType.secondary_school);
        user.addSchoolInfo(secondary);

        SchoolInfo high = new SchoolInfo();
        high.setName("cde");
        high.setType(SchoolType.high_school);
        user.addSchoolInfo(high);

        SchoolInfo university = new SchoolInfo();
        university.setName("def");
        university.setType(SchoolType.university);
        user.addSchoolInfo(university);

        return user;
    }

}
