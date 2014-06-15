package com.realaicy.pg.sys.auth.repository;

import com.realaicy.pg.core.repository.BaseRepository;
import com.realaicy.pg.sys.auth.entity.Auth;

import java.util.Set;

/**
 * SD-JPA-Repository：资源
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public interface AuthRepository extends BaseRepository<Auth, Long> {

    Auth findByUserId(Long userId);

    Auth findByGroupId(Long groupId);

    @SuppressWarnings("SpringDataJpaMethodInconsistencyInspection")
    Auth findByOrganizationIdAndJobId(Long organizationId, Long jobId);

    ///////////委托给AuthRepositoryImpl实现
    public Set<Long> findRoleIds(Long userId, Set<Long> groupIds, Set<Long> organizationIds,
                                 Set<Long> jobIds, Set<Long[]> organizationJobIds);

}
