package com.realaicy.pg.maintain.keyvalue.repository;

import com.realaicy.pg.core.repository.BaseRepository;
import com.realaicy.pg.maintain.keyvalue.entity.KeyValue;

/**
 * SD-JPA-Repository：keyValue
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
public interface KeyValueRepository extends BaseRepository<KeyValue, Long> {

    KeyValue findByKey(String key);

}
