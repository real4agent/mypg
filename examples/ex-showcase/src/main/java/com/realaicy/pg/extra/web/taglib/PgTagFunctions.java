package com.realaicy.pg.extra.web.taglib;

import com.realaicy.pg.core.utils.SpringUtils;
import com.realaicy.pg.sys.organization.entity.Job;
import com.realaicy.pg.sys.organization.entity.Organization;
import com.realaicy.pg.sys.organization.service.JobService;
import com.realaicy.pg.sys.organization.service.OrganizationService;
import com.realaicy.pg.sys.permission.entity.Permission;
import com.realaicy.pg.sys.permission.entity.Role;
import com.realaicy.pg.sys.permission.service.PermissionService;
import com.realaicy.pg.sys.permission.service.RoleService;
import com.realaicy.pg.sys.resource.entity.Resource;
import com.realaicy.pg.sys.resource.service.ResourceService;

import java.util.Iterator;

/**
 * 提供el中可以使用的一些函数
 * <p/>
 * Created by realaicy on 14-3-24.
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public class PgTagFunctions {


    public static boolean in(Iterable iterable, Object obj) {
        if (iterable == null) {
            return false;
        }
        Iterator iterator = iterable.iterator();

        while (iterator.hasNext()) {
            if (iterator.next().equals(obj)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否存在指定id的组织机构
     *
     * @param id              组织机构id
     * @param onlyDisplayShow 是否仅显示可见的
     * @return 如果存在且符合条件则返回真
     */
    public static boolean existsOrganization(Long id, Boolean onlyDisplayShow) {
        Organization organization = SpringUtils.getBean(OrganizationService.class).findOne(id);
        return organization != null &&
                !(Boolean.TRUE.equals(onlyDisplayShow) && Boolean.FALSE.equals(organization.getShow()));
    }

    /**
     * 判断是否存在指定id的工作职务
     *
     * @param id              职务id
     * @param onlyDisplayShow 是否仅显示可见的
     * @return 如果存在且符合条件则返回真
     */
    public static boolean existsJob(Long id, Boolean onlyDisplayShow) {
        Job job = SpringUtils.getBean(JobService.class).findOne(id);
        return job != null &&
                !(Boolean.TRUE.equals(onlyDisplayShow) && Boolean.FALSE.equals(job.getShow()));
    }

    /**
     * 判断是否存在指定id的资源
     *
     * @param id              资源id
     * @param onlyDisplayShow 是否仅显示可见的
     * @return 如果存在且符合条件则返回真
     */
    public static boolean existsResource(Long id, Boolean onlyDisplayShow) {
        Resource resource = SpringUtils.getBean(ResourceService.class).findOne(id);
        return resource != null &&
                !(Boolean.TRUE.equals(onlyDisplayShow) && Boolean.FALSE.equals(resource.getShow()));
    }

    /**
     * 判断是否存在指定id的权限
     *
     * @param id              权限id
     * @param onlyDisplayShow 是否仅显示可见的
     * @return 如果存在且符合条件则返回真
     */
    public static boolean existsPermission(Long id, Boolean onlyDisplayShow) {
        Permission permission = SpringUtils.getBean(PermissionService.class).findOne(id);
        return permission != null &&
                !(Boolean.TRUE.equals(onlyDisplayShow) && Boolean.FALSE.equals(permission.getShow()));
    }


    /**
     * 判断是否存在指定id的角色
     *
     * @param id              角色id
     * @param onlyDisplayShow 是否仅显示可见的
     * @return 如果存在且符合条件则返回真
     */
    public static boolean existsRole(Long id, Boolean onlyDisplayShow) {
        Role role = SpringUtils.getBean(RoleService.class).findOne(id);
        return role != null &&
                !(Boolean.TRUE.equals(onlyDisplayShow) && Boolean.FALSE.equals(role.getShow()));
    }

}
