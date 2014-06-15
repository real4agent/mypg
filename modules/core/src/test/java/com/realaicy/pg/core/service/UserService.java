package com.realaicy.pg.core.service;

import com.realaicy.pg.core.entity.User;
import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户Service
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
@Service()
public class UserService extends BaseService<User, Long> {

    @Autowired
    @BaseComponent
    UserRepository userRepository;
}
