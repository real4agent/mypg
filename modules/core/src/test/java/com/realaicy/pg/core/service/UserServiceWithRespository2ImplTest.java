package com.realaicy.pg.core.service;

import com.realaicy.pg.core.repository.UserRepository2;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户服务2测试
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public class UserServiceWithRespository2ImplTest extends UserServiceTest {

    @Autowired
    private UserRepository2 userRepository2;

    @Before
    public void setUp() {
        userService.setBaseRepository(userRepository2);
    }

}
