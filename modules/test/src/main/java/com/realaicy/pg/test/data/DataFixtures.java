package com.realaicy.pg.test.data;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;

/**
 * SQL数据文件导入工具类。
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public class DataFixtures {

    public static final String DEFAULT_ENCODING = "UTF-8";

    private static ResourceLoader resourceLoader = new DefaultResourceLoader();

    public static void executeScript(DataSource dataSource, String... sqlResourcePaths) throws DataAccessException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        for (String sqlResourcePath : sqlResourcePaths) {
            Resource resource = resourceLoader.getResource(sqlResourcePath);
            JdbcTestUtils.executeSqlScript(jdbcTemplate,
                    new EncodedResource(resource, DEFAULT_ENCODING), true);
        }
    }
}
