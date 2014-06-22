package com.realaicy.pg.monitor.web.controller;

import com.google.common.collect.Lists;
import com.realaicy.pg.core.Constants;
import com.realaicy.pg.core.repository.hibernate.HibernateUtils;
import com.realaicy.pg.core.web.bind.annotation.PageableDefaults;
import com.realaicy.pg.core.web.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

/**
 * SQL执行控制器
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
@Controller
@RequestMapping("/admin/monitor/db")
@RequiresPermissions("monitor:ql:*")
public class SQLExecutorController extends BaseController {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @RequestMapping(value = "/sql", method = RequestMethod.GET)
    public String showSQLForm() {
        return viewName("sqlForm");
    }

    @PageableDefaults(pageNumber = 0, value = 10)
    @RequestMapping(value = "/sql", method = RequestMethod.POST)
    public String executeQL(
            final @RequestParam("sql") String sql, final Model model,
            final Pageable pageable
    ) {

        model.addAttribute("sessionFactory", HibernateUtils.getSessionFactory(em));

        String lowerCaseSQL = sql.trim().toLowerCase();
        final boolean isDML = lowerCaseSQL.startsWith("insert") || lowerCaseSQL.startsWith("update") || lowerCaseSQL.startsWith("delete");
        final boolean isDQL = lowerCaseSQL.startsWith("select");

        if (!isDML && !isDQL) {
            model.addAttribute(Constants.ERROR, "您执行的SQL不允许，只允许insert、update、delete、select");
            return showSQLForm();
        }
        try {
            new TransactionTemplate(transactionManager).execute(new TransactionCallback<Void>() {
                @SuppressWarnings("unchecked")
                @Override
                public Void doInTransaction(TransactionStatus status) {

                    if (isDML) {
                        Query query = em.createNativeQuery(sql);
                        int updateCount = query.executeUpdate();
                        model.addAttribute("updateCount", updateCount);
                    } else {
                        String countSQL = "select count(*) count from (" + sql + ") o";
                        Query countQuery = em.createNativeQuery(countSQL);
                        Query findQuery = em.createNativeQuery(sql);
                        findQuery.setFirstResult(pageable.getOffset());
                        findQuery.setMaxResults(pageable.getPageSize());

                        Page page = new PageImpl(
                                findQuery.getResultList(),
                                pageable,
                                ((BigInteger) countQuery.getSingleResult()).longValue());

                        model.addAttribute("resultPage", page);

                        em.unwrap(Session.class).doWork(new Work() {
                            @Override
                            public void execute(final Connection connection) throws SQLException {
                                PreparedStatement psst = connection.prepareStatement(sql);
                                ResultSetMetaData metaData = psst.getMetaData();

                                List<String> columnNames = Lists.newArrayList();
                                for (int i = 1, l = metaData.getColumnCount(); i <= l; i++) {
                                    columnNames.add(metaData.getColumnLabel(i));
                                }
                                psst.close();
                                model.addAttribute("columnNames", columnNames);
                            }
                        });
                    }

                    return null;
                }
            });
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            model.addAttribute(Constants.ERROR, sw.toString());
        }

        return showSQLForm();
    }

}
