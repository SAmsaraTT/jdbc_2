package com.atguigu5.dbutils;

import com.atguigu4.util.JDBCUtils;
import com.atiguigu2.bean.Customer;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @Description 针对QueryRunner的测试
 * commons-dbutils 是 Apache 组织提供的一个开源 JDBC工具类库,封装了针对于数据库的增删改查操作
 * @Author hliu
 * @Date 2023/2/15 16:19
 * @Version 1.0
 */
public class QueryRunnerTest {
    /**
    * @Description: 测试插入
    * @Param: []
    * @return: void
    * @Author: hliu
    * @Date: 2023/2/15
    */
    @Test
    public void testInsert() {
        Connection connection = null;
        try {
            QueryRunner queryRunner = new QueryRunner();

            connection = JDBCUtils.getConnection2();
            String sql = "insert into customers(name, email, birth) values(?, ?, ?)";
            int update = queryRunner.update(connection, sql, "蔡徐坤", "caixukun@126.com", "1997-09-08");

            System.out.println("count:" + update);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    /**
    * @Description: BeanHandler:是ResultSetHandler接口的实现类(单条记录）
    * @Param: []
    * @return: void
    * @Author: hliu
    * @Date: 2023/2/15
    */
    @Test
    public void testQuery1() {
        Connection connection = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            connection = JDBCUtils.getConnection2();
            String sql = "select id, name, email, birth from customers where id = ?";
            BeanHandler<Customer> beanHandler = new BeanHandler<>(Customer.class);
            Customer query = queryRunner.query(connection, sql, beanHandler, 23);
            System.out.println(query);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    /**
    * @Description: BeanListHandler:是ResultSetHandler接口的实现类(多条记录）
    * @Param: []
    * @return: void
    * @Author: hliu
    * @Date: 2023/2/15
    */
    @Test
    public void testQuery2() {
        Connection connection = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            connection = JDBCUtils.getConnection2();
            String sql = "select id, name, email, birth from customers where id < ?";
            BeanListHandler<Customer> beanListHandler = new BeanListHandler<>(Customer.class);
            List<Customer> query = queryRunner.query(connection, sql, beanListHandler, 23);
            query.forEach(System.out::println);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    /**
    * @Description: MapHandler:是ResultSetHandler接口的实现类(单条记录）,将对应字段和值储存在map中
    * @Param: []
    * @return: void
    * @Author: hliu
    * @Date: 2023/2/15
    */
    @Test
    public void testQuery3() {
        Connection connection = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            connection = JDBCUtils.getConnection2();
            String sql = "select id, name, email, birth from customers where id = ?";
            MapHandler mapHandler = new MapHandler();
            Map<String, Object> query = queryRunner.query(connection, sql, mapHandler, 23);
            System.out.println(query);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    /**
    * @Description: MapListHandler:是ResultSetHandler接口的实现类(多条记录）,将对应字段和值储存在map中
    * @Param: []
    * @return: void
    * @Author: hliu
    * @Date: 2023/2/15
    */
    @Test
    public void testQuery4() {
        Connection connection = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            connection = JDBCUtils.getConnection2();
            String sql = "select id, name, email, birth from customers where id < ?";
            MapListHandler mapListHandler = new MapListHandler();
            List<Map<String, Object>> query = queryRunner.query(connection, sql, mapListHandler, 23);
            query.forEach(System.out::println);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    /**
    * @Description:  ScalarHandler：返回特殊值
    * @Param: []
    * @return: void
    * @Author: hliu
    * @Date: 2023/2/15
    */
    @Test
    public void testQuery5() {
        Connection connection = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            connection = JDBCUtils.getConnection2();
            String sql = "select count(*) from  customers";

            ScalarHandler scalarHandler = new ScalarHandler();
            Long query = (Long) queryRunner.query(connection, sql, scalarHandler);
            System.out.println(query);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    @Test
    public void testQuery6() {
        Connection connection = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            connection = JDBCUtils.getConnection2();
            String sql = "select max(birth) from  customers";

            ScalarHandler scalarHandler = new ScalarHandler();
            Date date = (Date) queryRunner.query(connection, sql, scalarHandler);
            System.out.println(date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    /**
    * @Description: 自定义ResultSetHandler实现类
    * @Param: []
    * @return: void
    * @Author: hliu
    * @Date: 2023/2/15
    */
    @Test
    public void testQuery7() {
        Connection connection = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            connection = JDBCUtils.getConnection2();
            String sql = "select id, name, email, birth from customers where id = ?";
            ResultSetHandler<Customer> handler = new ResultSetHandler<Customer>() {
                @Override
                public Customer handle(ResultSet resultSet) throws SQLException {
                    if (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        String email = resultSet.getString("email");
                        Date birth = resultSet.getDate("birth");

                        Customer customer = new Customer(id, name, email, birth);
                        return customer;
                    }
                    return null;
                }
            };

            Customer query = queryRunner.query(connection, sql, handler, 23);
            System.out.println(query);

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }
}
