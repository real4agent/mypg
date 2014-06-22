package com.realaicy.pg.maintain.notification.entity;

import com.realaicy.pg.core.entity.BaseEntity;
import com.realaicy.pg.core.plugin.entity.LogicDeleteable;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 消息通知模板
 * 实体：消息通知模板
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
@Table(name = "maintain_notification_template")
public class NotificationTemplate extends BaseEntity<Long> implements LogicDeleteable {

    /**
     * 模板名称 必须唯一 发送时使用
     */
    @NotNull(message = "{not.null}")
    @Length(min = 1, max = 100, message = "{length.not.valid}")
    private String name;

    /**
     * 所属系统
     */
    @NotNull(message = "{not.null}")
    @Enumerated(EnumType.STRING)
    private NotificationSystem system;

    /**
     * 模板标题
     */
    @Length(min = 1, max = 200, message = "{length.not.valid}")
    private String title;

    /**
     * 模板内容
     */
    private String template;

    /**
     * 是否已逻辑删除
     */
    private Boolean deleted = Boolean.FALSE;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public NotificationSystem getSystem() {
        return system;
    }

    public void setSystem(final NotificationSystem system) {
        this.system = system;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(final String template) {
        this.template = template;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(final Boolean deleted) {
        this.deleted = deleted;
    }

    public void markDeleted() {
        setDeleted(Boolean.TRUE);
    }

}
