package com.realaicy.pg.core.repository;

import com.realaicy.pg.core.entity.BaseInfo;
import com.realaicy.pg.core.entity.SchoolInfo;
import com.realaicy.pg.core.entity.User;
import com.realaicy.pg.core.entity.search.Searchable;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * <p>用户仓库2</p>
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public interface UserRepository2 extends BaseRepository<User, Long> {

    ////////////////////////////////////////////////////
    /////////以下实现都委托给UserRepository2Impl///////
    ////////////////////////////////////////////////////

    public BaseInfo findBaseInfoByUserId(Long userId);

    public List<SchoolInfo> findAllSchoolTypeByUserId(Long userId);

    public Page<User> findAllByDefault(final Searchable searchable);

    public long countAllByDefault(final Searchable searchable);

    public long countAllByCustom(final Searchable searchable);

    public Page<User> findAllByCustom(final Searchable searchable);

}
