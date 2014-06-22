package com.realaicy.pg.test.security.shiro;

import org.apache.shiro.SecurityUtils;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class ShiroTestUtilsTest {

    @Test
    public void mockSubject() {
        ShiroTestUtils.mockSubject("foo");
        assertTrue(SecurityUtils.getSubject().isAuthenticated());
        assertEquals("foo", SecurityUtils.getSubject().getPrincipal());

        ShiroTestUtils.clearSubject();

        ShiroTestUtils.mockSubject("bar");
        assertTrue(SecurityUtils.getSubject().isAuthenticated());
        assertEquals("bar", SecurityUtils.getSubject().getPrincipal());

    }

}
