package com.realaicy.pg.core.test.security.shiro;

import org.apache.shiro.SecurityUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
