package com.atguigu.transaction;

import com.atguigu.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;

/**
 * @Description 数据库连接测试
 * @Author hliu
 * @Date 2023/2/7 21:41
 * @Version 1.0
 */
public class ConnectionTest {
    @Test
    public void testConnection() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        System.out.println(connection);
    }
}


