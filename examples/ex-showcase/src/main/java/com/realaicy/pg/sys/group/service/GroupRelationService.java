package com.realaicy.pg.sys.group.service;

import com.google.common.collect.Sets;
import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.service.BaseService;
import com.realaicy.pg.sys.group.entity.GroupRelation;
import com.realaicy.pg.sys.group.repository.GroupRelationRepository;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * SD-JPA-Service：组关系
 * <p/>
 * 提供如下服务：<br/>
 * 1.关联组和组织机构
 * 2.关联组和用户
 * 3.查找“给定用户Id以及组织机构id集合”相关联的组集合
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
public class GroupRelationService extends BaseService<GroupRelation, Long> {

    @Autowired
    @BaseComponent
    private GroupRelationRepository groupRelationRepository;

    /**
     * 关联组和组织机构
     *
     * @param groupId 组Id
     * @param organizationIds 组织机构id
     */
    public void appendRelation(Long groupId, Long[] organizationIds) {

        if (ArrayUtils.isEmpty(organizationIds)) {
            return;
        }
        for (Long organizationId : organizationIds) {
            if (organizationId == null) {
                continue;
            }
            GroupRelation r = groupRelationRepository.findByGroupIdAndOrganizationId(groupId, organizationId);
            if (r == null) {
                r = new GroupRelation();
                r.setGroupId(groupId);
                r.setOrganizationId(organizationId);
                save(r);
            }
        }
    }

    /**
     * @param groupId 组id
     * @param userIds 用户id集合
     * @param startUserIds 开始范围的用户id集合
     * @param endUserIds 结束范围的用户id集合
     */
    public void appendRelation(Long groupId, Long[] userIds, Long[] startUserIds, Long[] endUserIds) {

        if (ArrayUtils.isEmpty(userIds) && ArrayUtils.isEmpty(startUserIds)) {
            return;
        }
        if (!ArrayUtils.isEmpty(userIds)) {
            for (Long userId : userIds) {
                if (userId == null) {
                    continue;
                }
                GroupRelation r = groupRelationRepository.findByGroupIdAndUserId(groupId, userId);
                if (r == null) {
                    r = new GroupRelation();
                    r.setGroupId(groupId);
                    r.setUserId(userId);
                    save(r);
                }
            }
        }

        if (!ArrayUtils.isEmpty(startUserIds)) {
            for (int i = 0, l = startUserIds.length; i < l; i++) {
                Long startUserId = startUserIds[i];
                Long endUserId = endUserIds[i];
                //范围查 如果在指定范围内 就没必要再新增一个 如当前是[10,20] 如果数据库有[9,21]
                GroupRelation r = groupRelationRepository.
                        findByGroupIdAndStartUserIdLessThanEqualAndEndUserIdGreaterThanEqual(groupId,
                                startUserId, endUserId);

                if (r == null) {
                    //删除范围内的单个的用户关联，再整合成为集合
                    groupRelationRepository.deleteInRange(startUserId, endUserId);
                    r = new GroupRelation();
                    r.setGroupId(groupId);
                    r.setStartUserId(startUserId);
                    r.setEndUserId(endUserId);
                    save(r);
                }

            }
        }
    }

    /**
     * @param userId 用户id
     * @param organizationIds 组织结构Id集合
     * @return 和“用户Id以及组织机构id集合相关联的”组的id集合
     */
    public Set<Long> findGroupIds(Long userId, Set<Long> organizationIds) {

        if (organizationIds.isEmpty()) {
            return Sets.newHashSet(groupRelationRepository.findGroupIds(userId));
        }
        return Sets.newHashSet(groupRelationRepository.findGroupIds(userId, organizationIds));
    }

}
