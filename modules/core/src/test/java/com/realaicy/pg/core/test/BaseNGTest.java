package com.realaicy.pg.core.test;

import com.realaicy.pg.core.test.spring.SpringContextNGTestCase;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * 测试基类
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
@ContextConfiguration({"classpath:spring-common.xml", "classpath:spring-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public abstract class BaseNGTest extends SpringContextNGTestCase {

}
