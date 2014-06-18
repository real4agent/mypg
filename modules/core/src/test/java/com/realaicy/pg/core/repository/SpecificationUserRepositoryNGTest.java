package com.realaicy.pg.core.repository;

import com.realaicy.pg.core.entity.User;
import com.realaicy.pg.core.test.BaseUserNGTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static org.testng.AssertJUnit.assertNotNull;

/**
 * <p>测试DDD Specification，Repository必须继承JpaSpecificationExecutor</p>
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public class SpecificationUserRepositoryNGTest extends BaseUserNGTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeMethod
    public void setUp() {
        user = createUser();
    }

    @AfterMethod
    public void tearDown() {
        user = null;
    }

    @Test
    public void test() {

        userRepository.save(user);

        Specification<User> spec = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("id"), user.getId());
            }
        };

        clear();

        User dbUser = userRepository.findOne(spec);
        assertNotNull(dbUser);

    }

}
