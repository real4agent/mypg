package com.realaicy.pg.tmp.parentchild;

import com.realaicy.pg.core.test.BaseIT;
import org.junit.Test;

/**
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public class ParentChildTest extends BaseIT {

    @Test
    public void testCascade() {
        Parent p = new Parent();
        p.setId("123");

        Child c1 = new Child();
        c1.setId("1");
        c1.setName("123");
        p.addChild(c1);

        Child c2 = new Child();
        c2.setId("2");
        c2.setName("234");
        p.addChild(c2);

        entityManager.persist(p);
        entityManager.flush();
        entityManager.clear();

        p = new Parent();
        p.setId("123");

        c2 = new Child();
        c2.setId("2");
        c2.setName("345");
        p.addChild(c2);

        Child c3 = new Child();
        c3.setId("23");
        c3.setName("456");
        p.addChild(c3);

        entityManager.merge(p);

        entityManager.flush();
        entityManager.clear();

        p = entityManager.find(Parent.class, "123");

    }

}
