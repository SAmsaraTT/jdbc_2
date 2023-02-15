package com.atguigu4.connection;


import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @Description DBCP数据库连接池测试
 * @Author hliu
 * @Date 2023/2/14 18:04
 * @Version 1.0
 */
public class DBCPTest {

    /**
     * 方式1
     * @throws SQLException
     */
    @Test
    public void testGetConnection() throws SQLException {
        //创建DBCP的数据库连接池
        BasicDataSource basicDataSource = new BasicDataSource();

        //设置基本信息
        basicDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        basicDataSource.setUrl("jdbc:mysql://localhost:3306/test");
        basicDataSource.setUsername("root");
        basicDataSource.setPassword("soul990719");

        //设置数据库管理的相关属性
        basicDataSource.setInitialSize(10);
        basicDataSource.setMaxActive(10);

        Connection connection = basicDataSource.getConnection();
        System.out.println(connection);
    }

    /**
     * 方式2
     * @throws Exception
     */
    @Test
    public void  testGetConnection2() throws Exception {
        Properties properties = new Properties();

        InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
        properties.load(resourceAsStream);
        DataSource dataSource = BasicDataSourceFactory.createDataSource(properties);

        Connection connection = dataSource.getConnection();
        System.out.println(connection);

    }
}
