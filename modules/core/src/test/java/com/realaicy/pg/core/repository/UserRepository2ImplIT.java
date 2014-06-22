package com.realaicy.pg.core.repository;

import com.google.common.collect.Lists;
import com.realaicy.pg.core.entity.BaseInfo;
import com.realaicy.pg.core.entity.SchoolInfo;
import com.realaicy.pg.core.entity.User;
import com.realaicy.pg.core.test.BaseUserIT;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

public class UserRepository2ImplIT extends BaseUserIT {

    @Autowired
    private UserRepository2 userRepository2;

    private User user;

    private RepositoryHelper repositoryHelper = new RepositoryHelper(User.class);

    @Before
    public void setUp() {
        user = createUser();
    }

    @After
    public void tearDown() {
        user = null;
    }

    @Test
    public void testFindBaseInfoByUserId() {
        userRepository2.save(user);

        clear();

        BaseInfo baseInfo = userRepository2.findBaseInfoByUserId(user.getId());
        assertNotNull(baseInfo);
    }

    @Test
    public void findAllSchoolTypeByUserId() {
        userRepository2.save(user);

        clear();

        List<SchoolInfo> schoolInfoList = userRepository2.findAllSchoolTypeByUserId(user.getId());
        assertEquals(user.getSchoolInfoSet().size(), schoolInfoList.size());
    }

    @Test
    public void testFindAll() {
        int count = 15;
        List<Long> ids = Lists.newArrayList();
        List<Date> birthdayList = Lists.newArrayList();
        String realnamePrefix = "liu";
        for (int i = 0; i < count; i++) {
            User user = createUser();
            user.getBaseInfo().setRealname(realnamePrefix + i);
            userRepository2.save(user);
            ids.add(user.getId());
            birthdayList.add(user.getBaseInfo().getBirthday());
        }

        String ql = "from User u where u.id in(?1) and u.baseInfo.realname like ?2 and u.baseInfo.birthday in (?3)";
        assertEquals(count, repositoryHelper.findAll(ql, ids, realnamePrefix + "%", birthdayList).size());
    }

    @Test
    public void testCountAll() {
        int count = 15;
        List<Long> ids = Lists.newArrayList();
        List<Date> birthdayList = Lists.newArrayList();
        String realnamePrefix = "liu";
        for (int i = 0; i < count; i++) {
            User user = createUser();
            user.getBaseInfo().setRealname(realnamePrefix + i);
            userRepository2.save(user);
            ids.add(user.getId());
            birthdayList.add(user.getBaseInfo().getBirthday());
        }

        String ql = "select count(o) from User o where o.id in(?1) and o.baseInfo.realname like ?2 and o.baseInfo.birthday in (?3)";
        assertEquals(count, repositoryHelper.count(ql, ids, realnamePrefix + "%", birthdayList));
    }

    @Test
    public void testFindOne() {
        int count = 15;
        User lastUser = null;
        String realnamePrefix = "liu";
        for (int i = 0; i < count; i++) {
            User user = createUser();
            user.getBaseInfo().setRealname(realnamePrefix + i);
            lastUser = userRepository2.save(user);
        }
        String ql = "select u from User u where u=?1 and u.baseInfo.realname like ?2";
        assertEquals(lastUser, repositoryHelper.findOne(ql, lastUser, realnamePrefix + "%"));
    }

    @Test
    public void testBatchUpdate() {
        int count = 15;
        String realname = "123321";
        User lastUser = null;
        for (int i = 0; i < count; i++) {
            User user = createUser();
            user.getBaseInfo().setRealname(realname);
            lastUser = userRepository2.save(user);
        }

        String ql = "update BaseInfo set realname=?1";
        assertEquals(count, repositoryHelper.batchUpdate(ql, realname));

        String findOneQL = "select u from User u where u=?1";
        User user = repositoryHelper.findOne(findOneQL, lastUser);
        assertEquals(realname, user.getBaseInfo().getRealname());

    }

}
