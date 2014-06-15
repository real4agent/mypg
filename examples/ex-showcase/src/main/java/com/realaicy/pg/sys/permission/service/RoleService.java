package com.realaicy.pg.sys.permission.service;

import com.google.common.collect.Sets;
import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.service.BaseService;
import com.realaicy.pg.sys.permission.entity.Role;
import com.realaicy.pg.sys.permission.entity.RoleResourcePermission;
import com.realaicy.pg.sys.permission.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * SD-JPA-Service：角色
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
public class RoleService extends BaseService<Role, Long> {

    @Autowired
    @BaseComponent
    private RoleRepository roleRepository;

    @Override
    public Role update(Role role) {

        List<RoleResourcePermission> localResourcePermissions = role.getResourcePermissions();
        for (int i = 0, l = localResourcePermissions.size(); i < l; i++) {
            RoleResourcePermission localResourcePermission = localResourcePermissions.get(i);
            localResourcePermission.setRole(role);
            RoleResourcePermission dbResourcePermission = findRoleResourcePermission(localResourcePermission);
            if (dbResourcePermission != null) {//出现在先删除再添加的情况
                dbResourcePermission.setRole(localResourcePermission.getRole());
                dbResourcePermission.setResourceId(localResourcePermission.getResourceId());
                dbResourcePermission.setPermissionIds(localResourcePermission.getPermissionIds());
                localResourcePermissions.set(i, dbResourcePermission);
            }
        }
        return super.update(role);
    }

    /**
     * 获取可用的角色列表
     *
     * @param roleIds 待过滤列表
     * @return 过滤到不可用之后的角色列表
     */
    public Set<Role> findShowRoles(Set<Long> roleIds) {

        Set<Role> roles = Sets.newHashSet();

        //TODO 如果角色很多 此处应该写查询
        for (Role role : findAll()) {
            if (Boolean.TRUE.equals(role.getShow()) && roleIds.contains(role.getId())) {
                roles.add(role);
            }
        }
        return roles;
    }

    private RoleResourcePermission findRoleResourcePermission(RoleResourcePermission roleResourcePermission) {
        return roleRepository.findRoleResourcePermission(
                roleResourcePermission.getRole(), roleResourcePermission.getResourceId());
    }

}
