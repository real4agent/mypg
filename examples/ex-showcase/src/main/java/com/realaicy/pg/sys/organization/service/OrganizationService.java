package com.realaicy.pg.sys.organization.service;

import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.plugin.serivce.BaseTreeableService;
import com.realaicy.pg.sys.organization.entity.Organization;
import com.realaicy.pg.sys.organization.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Set;

/**
 * SD-JPA-Service：组织结构
 * <p/>
 * 提供如下服务：<br/>
 * 1.过滤服务，仅获取可显示的数据
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
public class OrganizationService extends BaseTreeableService<Organization, Long> {

    @SuppressWarnings("UnusedDeclaration")
    @Autowired
    @BaseComponent
    private OrganizationRepository organizationRepository;

    /**
     * 过滤仅获取可显示的数据
     *
     * @param organizationIds 组织机构id集合
     * @param organizationJobIds 组织机构/职务关系id集合
     */
    public void filterForCanShow(Set<Long> organizationIds, Set<Long[]> organizationJobIds) {

        Iterator<Long> iter1 = organizationIds.iterator();

        while (iter1.hasNext()) {
            Long id = iter1.next();
            Organization o = findOne(id);
            if (o == null || Boolean.FALSE.equals(o.getShow())) {
                iter1.remove();
            }
        }

        Iterator<Long[]> iter2 = organizationJobIds.iterator();

        while (iter2.hasNext()) {
            Long id = iter2.next()[0];
            Organization o = findOne(id);
            if (o == null || Boolean.FALSE.equals(o.getShow())) {
                iter2.remove();
            }
        }

    }
}
