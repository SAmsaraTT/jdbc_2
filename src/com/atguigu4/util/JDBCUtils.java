package com.atguigu4.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * @Description 使用c3p0数据库连接池技术
 * @Author hliu
 * @Date 2023/2/13 21:10
 * @Version 1.0
 */
public class JDBCUtils {
    /**
     * 使用c3p0获取数据库连接
     */
    private static ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");
    public static Connection getConnection() throws SQLException {
        Connection connection = cpds.getConnection();
        return connection;
    }

    /**
     * 使用DBCP获取数据库连接
     */
    private static DataSource dpcp;
    static {
        try {
            Properties properties = new Properties();
            InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
            properties.load(resourceAsStream);
            dpcp = BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static Connection getConnection1() throws Exception {
        Connection connection = dpcp.getConnection();
        return connection;
    }

    private static DataSource druid;
    static {
        try {
            Properties properties = new Properties();
            InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
            properties.load(resourceAsStream);

            druid = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection2() throws Exception {
        Connection connection = druid.getConnection();
        return connection;
    }

    //关闭资源的操作
    public static void closeResource(Connection connection, Statement preparedStatement) {
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeResource(Connection connection, Statement preparedStatement, ResultSet resultSet) {
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
