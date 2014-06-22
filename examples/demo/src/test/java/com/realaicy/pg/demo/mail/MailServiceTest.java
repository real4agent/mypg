package com.realaicy.pg.demo.mail;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.realaicy.pg.demo.util.email.MimeMailService;
import com.realaicy.pg.demo.util.email.SimpleMailService;
import com.realaicy.pg.test.spring.SpringContextNGTestCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

@DirtiesContext
@ContextConfiguration(locations = {"/email/applicationContext-email.xml"})
public class MailServiceTest extends SpringContextNGTestCase {

    @Autowired
    private MimeMailService mimeMailService;

    @Autowired
    private SimpleMailService simpleMailService;

    @Autowired
    private GreenMail greenMail;

    @Test
    public void sendSimpleMail() throws MessagingException, InterruptedException, IOException {
        simpleMailService.sendNotificationMail("Xudong Liu");

        greenMail.waitForIncomingEmail(2000, 1);

        MimeMessage[] messages = greenMail.getReceivedMessages();
        MimeMessage message = messages[messages.length - 1];

        assertEquals("realaicy@gmail.com", message.getFrom()[0].toString());
        assertEquals("用户修改通知", message.getSubject());
        // text格式内容
        System.out.println(message.getContent());
        assertTrue(((String) message.getContent()).contains("被修改"));

    }

    @Test
    public void sendMimeMail() throws InterruptedException, MessagingException, IOException {
        mimeMailService.sendNotificationMail("Xudong Liu");

        greenMail.waitForIncomingEmail(2000, 1);
        MimeMessage[] messages = greenMail.getReceivedMessages();
        MimeMessage message = messages[messages.length - 1];

        assertEquals("realaicy@gmail.com", message.getFrom()[0].toString());
        assertEquals("用户修改通知", message.getSubject());

        MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();

        assertEquals(2, mimeMultipart.getCount());

        // Html格式的主邮件
        String mainPartText = getMainPartText(mimeMultipart.getBodyPart(0));
        System.out.println(mainPartText);
        assertTrue(mainPartText.contains("<h1>用户Xudong Liu被修改.</h1>"));

        // 附件
        assertEquals("Hello,i am a attachment.", GreenMailUtil.getBody(mimeMultipart.getBodyPart(1)).trim());

    }

    private String getMainPartText(Part mainPart) throws MessagingException, IOException {
        return (String) ((Multipart) mainPart.getContent()).getBodyPart(0).getContent();
    }
}
