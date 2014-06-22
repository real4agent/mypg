package com.realaicy.pg.showcase.sample.repository;

import com.realaicy.pg.core.repository.BaseRepository;
import com.realaicy.pg.showcase.sample.entity.Sample;

/**
 * SD-JPA-Repository：样例
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public interface SampleRepository extends BaseRepository<Sample, Long> {

    Sample findByName(String name);

}
