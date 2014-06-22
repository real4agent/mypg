package com.realaicy.pg.test.spring;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * Spring的支持依赖注入的JUnit4 集成测试基类, 相比Spring原基类名字更短.
 * <p/>
 * 子类需要定义applicationContext文件的位置,如:
 * <p/>
 * contextConfiguration(locations = { "/applicationContext-test.xml" })
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
@ActiveProfiles(Profiles.UNIT_TEST)
public abstract class SpringContextTestCase extends AbstractJUnit4SpringContextTests {
}
