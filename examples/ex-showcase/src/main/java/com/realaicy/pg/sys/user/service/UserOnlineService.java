package com.realaicy.pg.sys.user.service;

import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.service.BaseService;
import com.realaicy.pg.sys.user.entity.UserOnline;
import com.realaicy.pg.sys.user.repository.UserOnlineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * SD-JPA-Service：用户在线服务
 * <p/>
 * 提供如下服务：<br/>
 * 1.处理用户上线
 * 2.处理用户下线
 * 3.批量处理用户下线
 * 4.查找无效的“UserOnline”
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
@Service
public class UserOnlineService extends BaseService<UserOnline, String> {

    @Autowired
    @BaseComponent
    private UserOnlineRepository userOnlineRepository;

    /**
     * 处理用户上线
     */
    public void online(UserOnline userOnline) {
        save(userOnline);
    }

    /**
     * 处理用户下线
     *
     * @param sid 用户sid
     */
    public void offline(String sid) {
        UserOnline userOnline = findOne(sid);
        if (userOnline != null) {
            delete(userOnline);
        }
        //游客 无需记录上次访问记录
        //此处使用数据库的触发器完成同步
//        if(userOnline.getUserId() == null) {
//            userLastOnlineService.lastOnline(UserLastOnline.fromUserOnline(userOnline));
//        }
    }

    /**
     * 批量下线
     */
    public void batchOffline(List<String> needOfflineIdList) {
        userOnlineRepository.batchDelete(needOfflineIdList);
    }

    /**
     * 无效的UserOnline
     */
    public Page<UserOnline> findExpiredUserOnlineList(Date expiredDate, Pageable pageable) {
        return userOnlineRepository.findExpiredUserOnlineList(expiredDate, pageable);
    }

}
