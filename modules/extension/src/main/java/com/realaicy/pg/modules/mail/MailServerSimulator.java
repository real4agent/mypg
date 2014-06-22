package com.realaicy.pg.modules.mail;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 基于GreenMail的MailServer模拟器，用于开发/测试环境。
 * 默认在localhost的3025端口启动SMTP服务, 用户名密码是greenmail@localhost.com/greemail, 均可设置.
 * FactoryBean已将greenMail对象注入到Context中，可在测试中取用.
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public class MailServerSimulator implements InitializingBean, DisposableBean, FactoryBean<GreenMail> {

    public static final String DEFAULT_ACCOUNT = "greenmail@localhost.com";
    private String account = DEFAULT_ACCOUNT;
    public static final String DEFAULT_PASSWORD = "greenmail";
    private String password = DEFAULT_PASSWORD;
    public static final int DEFAULT_PORT = 3025;
    private int port = DEFAULT_PORT;
    private GreenMail greenMail;

    @Override
    public void afterPropertiesSet() throws Exception {
        greenMail = new GreenMail(new ServerSetup(port, null, ServerSetup.PROTOCOL_SMTP));
        greenMail.setUser(account, password);
        greenMail.start();
    }

    @Override
    public void destroy() throws Exception {
        if (greenMail != null) {
            greenMail.stop();
        }
    }

    @Override
    public GreenMail getObject() throws Exception {
        return greenMail;
    }

    @Override
    public Class<?> getObjectType() {
        return GreenMail.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
