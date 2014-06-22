package com.realaicy.pg.showcase.excel.repository;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public class ExcelDataRepositoryImpl {

    @PersistenceContext
    private EntityManager em;

    public void truncate() {
        em.unwrap(Session.class).doWork(new Work() {
            @Override
            public void execute(final Connection connection) throws SQLException {
                connection.createStatement().execute("truncate table showcase_excel_data");
            }
        });

    }
}
