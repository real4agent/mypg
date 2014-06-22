package com.realaicy.pg.test.jetty;

import com.realaicy.pg.test.jetty.JettyFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class JettyFactoryTest {

    @Test
    public void createServer() {
        Server server = JettyFactory.createServerInSource(1978, "/test");

        assertEquals(1978, server.getConnectors()[0].getPort());
        assertEquals("/test", ((WebAppContext) server.getHandler()).getContextPath());
        assertEquals("src/main/webapp", ((WebAppContext) server.getHandler()).getWar());
    }
}
