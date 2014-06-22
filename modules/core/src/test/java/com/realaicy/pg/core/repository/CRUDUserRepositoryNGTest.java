package com.realaicy.pg.core.repository;

import com.realaicy.pg.core.entity.SchoolType;
import com.realaicy.pg.core.entity.User;
import com.realaicy.pg.core.test.BaseUserNGTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.testng.AssertJUnit.*;

/**
 * <p>User Repository集成测试</p>
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

public class CRUDUserRepositoryNGTest extends BaseUserNGTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeMethod
    public void setUp() {
        user = createUser();
    }

    @AfterMethod
    public void tearDown() {
        user = null;
    }

    @Test
    public void testSave() {
        User dbUser = userRepository.save(user);
        assertNotNull(dbUser.getId());
    }

    @Test
    public void testUpdate() {
        User dbUser = userRepository.save(user);
        clear();

        String newUsername = "liu$$$$" + System.currentTimeMillis();
        dbUser.setUsername(newUsername);
        userRepository.save(dbUser);

        clear();

        assertEquals(newUsername, userRepository.findOne(dbUser.getId()).getUsername());
    }

    @Test
    public void testUpdateRealname() {
        String realname = "lisi";
        User dbUser = userRepository.save(user);
        userRepository.updateRealname(realname, dbUser.getId());

        userRepository.flush();
        entityManager.clear();

        dbUser = userRepository.findOne(dbUser.getId());
        assertEquals(realname, dbUser.getBaseInfo().getRealname());
    }

    @Test
    public void testUpdateRealnameWithNamedParam() {
        String realname = "lisi";
        User dbUser = userRepository.save(user);
        userRepository.updateRealnameWithNamedParam(realname, dbUser.getId());

        userRepository.flush();
        entityManager.clear();

        dbUser = userRepository.findOne(dbUser.getId());
        assertEquals(realname, dbUser.getBaseInfo().getRealname());
    }

    @Test
    public void testDeleteByUsername() {
        User dbUser = userRepository.save(user);
        userRepository.deleteBaseInfoByUser(dbUser.getId());

        userRepository.flush();
        entityManager.clear();

        dbUser = userRepository.findOne(dbUser.getId());
        assertNull(dbUser.getBaseInfo());
    }

    @Test
    public void testFindByUsername() {
        userRepository.save(user);
        User dbUser = userRepository.findByUsername(user.getUsername());

        assertNotNull(dbUser);
    }

    @Test
    public void testFindByBaseInfoSex() {
        userRepository.save(user);
        User dbUser = userRepository.findByBaseInfoSex(user.getBaseInfo().getSex());
        assertNotNull(dbUser);
    }

    @Test
    public void testFindByBaseInfoSexAndShcoolInfoType() {
        userRepository.save(user);
        User dbUser = userRepository.findByBaseInfoSexAndShcoolInfoSetType(
                user.getBaseInfo().getSex(),
                SchoolType.secondary_school);

        assertNotNull(dbUser);
    }
}
