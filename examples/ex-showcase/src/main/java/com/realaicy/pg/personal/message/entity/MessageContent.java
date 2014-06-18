package com.realaicy.pg.personal.message.entity;

import com.realaicy.pg.core.entity.BaseEntity;
import org.hibernate.annotations.Proxy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 表：消息内容
 * <p/>
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
@Entity
@Table(name = "personal_message_content")
@Proxy(lazy = true, proxyClass = MessageContent.class)
public class MessageContent extends BaseEntity<Long> {

    @OneToOne(fetch = FetchType.LAZY)
    private Message message;

    /**
     * 消息内容
     */
    @Length(min = 5, max = 50000, message = "{length.not.valid}")
    private String content;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
