package com.realaicy.pg.sys.user.service;

import com.realaicy.pg.core.entity.search.Searchable;
import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.service.BaseService;
import com.realaicy.pg.sys.user.entity.User;
import com.realaicy.pg.sys.user.entity.UserStatus;
import com.realaicy.pg.sys.user.entity.UserStatusHistory;
import com.realaicy.pg.sys.user.repository.UserStatusHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * SD-JPA-Service：用户状态历史服务
 * <p/>
 * 提供如下服务：<br/>
 * 1.记录状态变更历史
 * 2.查找给定的某个用户的上一次的用户状态，即最近一次变更之前的用户状态
 * 3.查找给定的某个用户的上一次的用户状态变更的原因，即最近一次变更用户状态的原因
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
public class UserStatusHistoryService extends BaseService<UserStatusHistory, Long> {

    @Autowired
    @BaseComponent
    private UserStatusHistoryRepository userStatusHistoryRepository;

    public void log(User opUser, User user, UserStatus newStatus, String reason) {
        UserStatusHistory history = new UserStatusHistory();
        history.setUser(user);
        history.setOpUser(opUser);
        history.setOpDate(new Date());
        history.setStatus(newStatus);
        history.setReason(reason);
        save(history);
    }

    public UserStatusHistory findLastHistory(final User user) {
        Searchable searchable = Searchable.newSearchable()
                .addSearchParam("user_eq", user)
                .addSort(Sort.Direction.DESC, "opDate")
                .setPage(0, 1);

        Page<UserStatusHistory> page = userStatusHistoryRepository.findAll(searchable);

        if (page.hasContent()) {
            return page.getContent().get(0);
        }
        return null;
    }

    public String getLastReason(User user) {
        UserStatusHistory history = findLastHistory(user);
        if (history == null) {
            return "";
        }
        return history.getReason();
    }
}
