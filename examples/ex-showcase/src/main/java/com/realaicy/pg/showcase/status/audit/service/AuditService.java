package com.realaicy.pg.showcase.status.audit.service;

import com.realaicy.pg.core.inject.annotation.BaseComponent;
import com.realaicy.pg.core.service.BaseService;
import com.realaicy.pg.showcase.status.audit.entity.Audit;
import com.realaicy.pg.showcase.status.audit.repository.AuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SD-JPA-Service：审计状态
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
public class AuditService extends BaseService<Audit, Long> {

    @Autowired
    @BaseComponent
    private AuditRepository sampleRepository;

}
