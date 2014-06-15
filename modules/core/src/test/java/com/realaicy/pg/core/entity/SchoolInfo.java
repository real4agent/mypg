package com.realaicy.pg.core.entity;

import javax.persistence.*;

/**
 * <p>学校信息</p>
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
@Table(name = "user_schoolinfo")
public class SchoolInfo extends BaseEntity<Long> {

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    /**
     * 学校名称
     */
    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "type", length = 2)
    @Enumerated(EnumType.ORDINAL)
    private SchoolType type;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SchoolType getType() {
        return type;
    }

    public void setType(SchoolType type) {
        this.type = type;
    }
}
