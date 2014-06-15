package com.realaicy.pg.sys.user.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.realaicy.pg.core.entity.BaseEntity;
import com.realaicy.pg.core.plugin.entity.LogicDeleteable;
import com.realaicy.pg.core.repository.support.annotation.EnableQueryCache;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 表：用户
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
@Table(name = "sys_user")
@EnableQueryCache
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User extends BaseEntity<Long> implements LogicDeleteable {
    public static final String USERNAME_PATTERN = "^[\\u4E00-\\u9FA5\\uf900-\\ufa2d_a-zA-Z][\\u4E00-\\u9FA5\\uf900-\\ufa2d\\w]{1,19}$";
    public static final String EMAIL_PATTERN = "^((([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+(\\.([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+)*)|((\\x22)((((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(([\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]|\\x21|[\\x23-\\x5b]|[\\x5d-\\x7e]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(\\\\([\\x01-\\x09\\x0b\\x0c\\x0d-\\x7f]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF]))))*(((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(\\x22)))@((([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.)+(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.?";
    public static final String MOBILE_PHONE_NUMBER_PATTERN = "^0{0,1}(13[0-9]|15[0-9]|14[0-9]|18[0-9])[0-9]{8}$";
    public static final int USERNAME_MIN_LENGTH = 2;
    public static final int USERNAME_MAX_LENGTH = 20;
    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 50;

    @NotNull(message = "{not.null}")
    @Pattern(regexp = USERNAME_PATTERN, message = "{user.username.not.valid}")
    private String username;

    @NotEmpty(message = "{not.null}")
    @Pattern(regexp = EMAIL_PATTERN, message = "{user.email.not.valid}")
    private String email;

    @NotEmpty(message = "{not.null}")
    @Pattern(regexp = MOBILE_PHONE_NUMBER_PATTERN, message = "{user.mobile.phone.number.not.valid}")
    @Column(name = "mobile_phone_number")
    private String mobilePhoneNumber;

    /**
     * 使用md5(username + original password + salt)加密存储
     */
    @Length(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH, message = "{user.password.not.valid}")
    private String password;

    /**
     * 加密密码时使用的种子（盐）
     */
    private String salt;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    /**
     * 系统用户的状态
     */
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.normal;

    /**
     * 是否是管理员
     */
    private Boolean admin = false;

    /**
     * 逻辑删除flag
     */
    private Boolean deleted = Boolean.FALSE;

    /**
     * 用户 组织机构 工作职务关联表<p/>
     * Fetch type (lazy/eager) refers to when Hibernate will fetch the association,
     * whether in advance when it fetches the entity (eager),
     * or whether it waits for the code to ask for the association (lazy).
     * <p/>
     * Fetch mode (select/join) refers to how Hibernate will fetch the association,
     * i.e. does it use an extra SELECT statement, or does it use a join.
     * <p/>
     * Some combinations of these make no sense, e.g. lazy+join.
     * If you use lazy fetching, then SELECT fetch mode is the only one that you can do.
     * <p/>
     * If you use eager fetch, then you can choose to use either fetch mode
     * <p/>
     * orphanRemoval has nothing to do with ON DELETE CASCADE.
     * <p/>
     * orphanRemoval is an entirely ORM-specific thing. It marks "child" entity to be removed when
     * it's no longer referenced from the "parent" entity,
     * e.g. when you remove the child entity from the corresponding collection of the parent entity.
     * <p/>
     * ON DELETE CASCADE is a database-specific thing, it deletes the "child" row in the database when the "parent" row is deleted.
     */
    //targetEntity在使用泛型的时候可以省略
    //orphanRemoval具体可见：http://stackoverflow.com/questions/4329577/jpa-2-0-orphanremoval-true-vs-on-delete-cascade
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,
            targetEntity = UserOrganizationJob.class, mappedBy = "user", orphanRemoval = true)
    @Fetch(FetchMode.SELECT)
    //@Basic(optional = true是说java object的对应的属性可以为空，区别于@Column（not null），后者是对应于物理数据库的限制。
    //@Basic(optional = true, fetch = FetchType.EAGER)
    //http://stackoverflow.com/questions/12546133/difference-between-jpa-cascade-annotations-and-hibernate-cascade-annotation
    //http://www.mkyong.com/hibernate/cascade-jpa-hibernate-annotation-common-mistake/
    //此处用hibernate的私有注解@Cascade是为了解决的当代码中直接调用hibernate私有api的时候级联不起作用的问题
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    //集合缓存引起的
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)//集合缓存
    @OrderBy() // ordering by primary key is assumed
    private List<UserOrganizationJob> organizationJobs;
    private transient Map<Long, List<UserOrganizationJob>> organizationJobsMap;

    public User() {
    }

    public User(Long id) {
        setId(id);
    }

    public List<UserOrganizationJob> getOrganizationJobs() {
        if (organizationJobs == null) {
            organizationJobs = Lists.newArrayList();
        }
        return organizationJobs;
    }

    public void setOrganizationJobs(List<UserOrganizationJob> organizationJobs) {
        this.organizationJobs = organizationJobs;
    }

    public void addOrganizationJob(UserOrganizationJob userOrganizationJob) {
        userOrganizationJob.setUser(this);
        getOrganizationJobs().add(userOrganizationJob);
    }

    @Transient
    public Map<Long, List<UserOrganizationJob>> getDisplayOrganizationJobs() {
        if (organizationJobsMap != null) {
            return organizationJobsMap;
        }

        organizationJobsMap = Maps.newHashMap();

        for (UserOrganizationJob userOrganizationJob : getOrganizationJobs()) {
            Long organizationId = userOrganizationJob.getOrganizationId();
            List<UserOrganizationJob> userOrganizationJobList = organizationJobsMap.get(organizationId);
            if (userOrganizationJobList == null) {
                userOrganizationJobList = Lists.newArrayList();
                organizationJobsMap.put(organizationId, userOrganizationJobList);
            }
            userOrganizationJobList.add(userOrganizationJob);
        }
        return organizationJobsMap;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * 生成新的种子
     */
    public void randomSalt() {
        setSalt(RandomStringUtils.randomAlphanumeric(10));
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    @Override
    public Boolean getDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public void markDeleted() {
        this.deleted = Boolean.TRUE;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

}
