package com.atguigu4.connection;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.sql.Connection;

/**
 * @Description C3P0连接池连接测试
 * @Author hliu
 * @Date 2023/2/12 16:52
 * @Version 1.0
 */
public class C3P0Test {
    @Test
    public void testGetConnection() throws Exception {
        //获取数据库连接池
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass( "com.mysql.cj.jdbc.Driver" ); //loads the jdbc driver
        cpds.setJdbcUrl( "jdbc:mysql://localhost:3306/test" );
        cpds.setUser("root");
        cpds.setPassword("soul990719");

        //设置相关的参数
        cpds.setInitialPoolSize(10);

        Connection connection = cpds.getConnection();
        System.out.println(connection);
    }
}
