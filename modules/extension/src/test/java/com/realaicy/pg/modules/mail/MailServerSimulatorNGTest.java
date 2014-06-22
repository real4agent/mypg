package com.realaicy.pg.modules.mail;

import com.icegreen.greenmail.util.GreenMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;
import com.realaicy.pg.test.spring.SpringContextNGTestCase;

import static org.testng.AssertJUnit.assertEquals;

@ContextConfiguration({"classpath:applicationContext-mail.xml"})
public class MailServerSimulatorNGTest extends SpringContextNGTestCase {

    @Autowired
    private GreenMail greenMail;

    @Test
    public void greenMail() {
        assertEquals(3025, greenMail.getSmtp().getPort());
    }
}
