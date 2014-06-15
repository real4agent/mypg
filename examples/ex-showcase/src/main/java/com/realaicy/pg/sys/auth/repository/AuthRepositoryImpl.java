package com.realaicy.pg.sys.auth.repository;

import com.google.common.collect.Sets;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Set;

/**
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public class AuthRepositoryImpl {

    @PersistenceContext
    private EntityManager em;

    @SuppressWarnings("unchecked")
    public Set<Long> findRoleIds(Long userId, Set<Long> groupIds, Set<Long> organizationIds,
                                 Set<Long> jobIds, Set<Long[]> organizationJobIds) {

        boolean hasGroupIds = groupIds.size() > 0;
        boolean hasOrganizationIds = organizationIds.size() > 0;
        boolean hasJobIds = jobIds.size() > 0;
        boolean hasOrganizationJobIds = organizationJobIds.size() > 0;

        StringBuilder hql = new StringBuilder("select roleIds from Auth where ");
        hql.append(" (userId=:userId) ");

        if (hasGroupIds) {
            hql.append(" or ");
            hql.append(" (groupId in (:groupIds)) ");
        }

        if (hasOrganizationIds) {
            hql.append(" or ");
            hql.append(" (( organizationId in (:organizationIds) and jobId=0 )) ");
        }

        if (hasJobIds) {
            hql.append(" or ");
            hql.append(" (( organizationId=0 and jobId in (:jobIds) )) ");
        }
        if (hasOrganizationJobIds) {
            int i = 0, l = organizationJobIds.size();
            while (i < l) {
                hql.append(" or ");
                //hql.append(" ( organizationId=:organizationId_" + i + " and jobId=:jobId_" + i + " ) ");
                hql.append(" ( organizationId=:organizationId_").append(i).
                        append(" and jobId=:jobId_").append(i).append(" ) ");
                i++;
            }
        }

        Query q = em.createQuery(hql.toString());

        q.setParameter("userId", userId);

        if (hasGroupIds) {
            q.setParameter("groupIds", groupIds);
        }

        if (hasOrganizationIds) {
            q.setParameter("organizationIds", organizationIds);
        }

        if (hasJobIds) {
            q.setParameter("jobIds", jobIds);
        }
        if (hasOrganizationJobIds) {
            int i = 0;
            for (Long[] organizationJobId : organizationJobIds) {
                q.setParameter("organizationId_" + i, organizationJobId[0]);
                q.setParameter("jobId_" + i, organizationJobId[1]);
                i++;
            }
        }

        List<Set<Long>> roleIdSets = (List<Set<Long>>) q.getResultList();

        Set<Long> roleIds = Sets.newHashSet();
        for (Set<Long> set : roleIdSets) {
            roleIds.addAll(set);
        }

        return roleIds;
    }
}
