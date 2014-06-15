package com.realaicy.pg.core.repository;

import com.realaicy.pg.core.entity.*;
import com.realaicy.pg.core.test.BaseUserIT;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

public class PageAndSortUserRepositoryIT extends BaseUserIT {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindAllForPage() {
        for (int i = 0; i < 15; i++) {
            userRepository.save(createUser());
        }
        PageRequest pageRequest = new PageRequest(1, 5);
        Page<User> page = userRepository.findAll(pageRequest);

        assertEquals(pageRequest.getPageSize(), page.getNumberOfElements());
        assertEquals(3, page.getTotalPages());
    }

    @Test
    public void testFindByBaseInfoSexForPage() {
        for (int i = 0; i < 15; i++) {
            userRepository.save(createUser());
        }
        PageRequest pageRequest = new PageRequest(1, 5);
        Page<User> page = userRepository.findByBaseInfoSex(Sex.male, pageRequest);

        assertEquals(pageRequest.getPageSize(), page.getNumberOfElements());
        assertEquals(3, page.getTotalPages());

        page = userRepository.findByBaseInfoSex(Sex.female, pageRequest);

        assertEquals(0, page.getNumberOfElements());
        assertEquals(0, page.getTotalPages());
    }

    @Test
    public void testFindAllForSort() {
        for (int i = 0; i < 15; i++) {
            userRepository.save(createUser());
        }
        Sort.Order idAsc = new Sort.Order(Sort.Direction.ASC, "id");
        Sort.Order usernameDesc = new Sort.Order(Sort.Direction.DESC, "username");
        Sort sort = new Sort(idAsc, usernameDesc);

        List<User> userList = userRepository.findAll(sort);

        assertTrue(userList.get(0).getId() < userList.get(1).getId());

    }

    @Test
    public void testFindByBaseInfoSexForSort() {
        for (int i = 0; i < 15; i++) {
            userRepository.save(createUser());
        }
        Sort.Order idAsc = new Sort.Order(Sort.Direction.ASC, "id");
        Sort.Order usernameDesc = new Sort.Order(Sort.Direction.DESC, "username");
        Sort sort = new Sort(idAsc, usernameDesc);

        List<User> userList = userRepository.findByBaseInfoSex(Sex.male, sort);

        assertTrue(userList.get(0).getId() < userList.get(1).getId());

    }

    @Test
    public void testFindAllForPageAndSort() {
        for (int i = 0; i < 15; i++) {
            userRepository.save(createUser());
        }

        Sort.Order idAsc = new Sort.Order(Sort.Direction.ASC, "id");
        Sort.Order usernameDesc = new Sort.Order(Sort.Direction.DESC, "username");
        Sort sort = new Sort(idAsc, usernameDesc);

        PageRequest pageRequest = new PageRequest(1, 5, sort);
        Page<User> page = userRepository.findAll(pageRequest);

        assertEquals(pageRequest.getPageSize(), page.getNumberOfElements());
        assertEquals(3, page.getTotalPages());

        assertTrue(page.getContent().get(0).getId() < page.getContent().get(1).getId());

    }

    public User createUser() {
        User user = new User();
        user.setUsername("zhangkaitao$$$" + System.nanoTime() + RandomStringUtils.random(10));
        user.setPassword("123456");
        BaseInfo baseInfo = new BaseInfo();
        baseInfo.setRealname("zhangkaitao");
        baseInfo.setSex(Sex.male);
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
