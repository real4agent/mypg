package com.realaicy.pg.sys.user.service;

import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.service.BaseService;
import com.realaicy.pg.sys.user.entity.UserLastOnline;
import com.realaicy.pg.sys.user.repository.UserLastOnlineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SD-JPA-Service：用户上一次在线服务
 * <p/>
 * 提供如下服务：<br/>
 * 1.处理上一次在线
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
@Service
public class UserLastOnlineService extends BaseService<UserLastOnline, Long> {

    @Autowired
    @BaseComponent
    private UserLastOnlineRepository userLastOnlineRepository;

    public UserLastOnline findByUserId(Long userId) {
        return userLastOnlineRepository.findByUserId(userId);
    }

    /**
     * 如果系统没有上一次的登录记录，则认为是第一个，则将当前信息作为将要保存的“上一次登录信息”
     * 如果有，则将当前的信息和系统中存在的信息进行整合
     */
    public void lastOnline(UserLastOnline lastOnline) {

        UserLastOnline dbLastOnline = findByUserId(lastOnline.getUserId());

        if (dbLastOnline == null) {
            dbLastOnline = lastOnline;
        } else {
            UserLastOnline.merge(lastOnline, dbLastOnline);
        }
        dbLastOnline.incLoginCount();
        dbLastOnline.incTotalOnlineTime();
        //相对于save or update
        save(dbLastOnline);
    }
}
