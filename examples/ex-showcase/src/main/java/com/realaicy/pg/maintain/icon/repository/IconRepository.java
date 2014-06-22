package com.realaicy.pg.maintain.icon.repository;

import com.realaicy.pg.core.repository.BaseRepository;
import com.realaicy.pg.maintain.icon.entity.Icon;

/**
 * SD-JPA-Repository：图标
 * <p/>
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public interface IconRepository extends BaseRepository<Icon, Long> {
    Icon findByIdentity(String identity);
}
