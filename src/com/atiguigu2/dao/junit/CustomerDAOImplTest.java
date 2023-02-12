package com.atiguigu2.dao.junit;

import com.atguigu.util.JDBCUtils;
import com.atiguigu2.bean.Customer;
import com.atiguigu2.dao.CustomerDAOImpl;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @Description CustomerDAOImpl的测试类
 * @Author hliu
 * @Date 2023/2/11 19:33
 * @Version 1.0
 */
public class CustomerDAOImplTest {
    private CustomerDAOImpl customerDAO = new CustomerDAOImpl();

    @Test
    public void insert() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parse = simpleDateFormat.parse("1000-01-01");
            Customer hliu = new Customer(1, "hliu", "hliu714@gatech.edu", new Date(parse.getTime()));
            customerDAO.insert(connection, hliu);
            System.out.println("添加成功！");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    @Test
    public void deleteById() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            customerDAO.deleteById(connection, 13);
            System.out.println("删除成功！");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    @Test
    public void update() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parse = simpleDateFormat.parse("1680-01-01");
            Customer customer = new Customer(18, "贝多芬", "beiduofen@126.com", new Date(parse.getTime()));
            customerDAO.update(connection, customer);
            System.out.println("更新成功！");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    @Test
    public void getCustomerbyId() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            Customer customerbyId = customerDAO.getCustomerbyId(connection, 19);
            System.out.println(customerbyId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    @Test
    public void getAll() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            List<Customer> all = customerDAO.getAll(connection);
            all.forEach(a -> System.out.println(a));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    @Test
    public void getCount() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            Long count = customerDAO.getCount(connection);
            System.out.println(count);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    @Test
    public void getMaxBirth() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            Date maxBirth = customerDAO.getMaxBirth(connection);
            System.out.println(maxBirth);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }
}