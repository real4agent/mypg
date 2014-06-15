package com.realaicy.pg.sys.user.repository;

import com.realaicy.pg.core.repository.BaseRepository;
import com.realaicy.pg.sys.user.entity.UserLastOnline;

/**
 * SD-JPA-Repository：用户最后登录
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public interface UserLastOnlineRepository extends BaseRepository<UserLastOnline, Long> {

    UserLastOnline findByUserId(Long userId);
}
