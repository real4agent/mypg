package com.realaicy.pg.sys.permission.service;

import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.service.BaseService;
import com.realaicy.pg.sys.permission.entity.Permission;
import com.realaicy.pg.sys.permission.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SD-JPA-Service：权限
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
@Service
public class PermissionService extends BaseService<Permission, Long> {

    @SuppressWarnings("UnusedDeclaration")
    @Autowired
    @BaseComponent
    private PermissionRepository permissionRepository;

}
