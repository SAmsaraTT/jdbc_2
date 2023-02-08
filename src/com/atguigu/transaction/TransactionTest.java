package com.atguigu.transaction;

import com.atguigu.util.JDBCUtils;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;

/**
 * @Description 数据库事务的引入
 * 1. 事务：一组逻辑操作单元,使数据从一种状态变换到另一种状态。
 * 		> 一组逻辑操作单元：一个或多个DML操作。
 *
 * 2.事务处理的原则：保证所有事务都作为一个工作单元来执行，即使出现了故障，都不能改变这种执行方式。
 *  当在一个事务中执行多个操作时，要么所有的事务都被提交(commit)，那么这些修改就永久地保存
 *  下来；要么数据库管理系统将放弃所作的所有修改，整个事务回滚(rollback)到最初状态。
 *
 * 3.数据一旦提交，就不可回滚
 *
 * 4.哪些操作会导致数据的自动提交？
 *   	>DDL操作一旦执行，都会自动提交。
 *  	>set autocommit = false 对DDL操作失效
 *  	DML默认情况下，一旦执行，就会自动提交。
 *  	>我们可以通过set autocommit = false的方式取消DML操作的自动提交。
 *      >默认在关闭连接时，会自动的提交数据
 * @Author hliu
 * @Date 2023/2/8 16:41
 * @Version 1.0
 */
public class TransactionTest {
    /**
     * 未考虑事务！
     * 针对于user_table
     * AA用户给BB用户转账100
     *
     * update user_table set balance = balance - 100 where user = 'AA';
     * update user_table set balance = balance + 100 where user = 'BB';
     */
    @Test
    public void testUpdate() {
        String sql1 = "update user_table set balance = balance - 100 where user = ?";
        update(sql1, "AA");

        //模拟网络异常
        //System.out.println(10 / 0);

        String sql2 = "update user_table set balance = balance + 100 where user = ?";
        update(sql2, "BB");

        System.out.println("转账成功！");
    }

    /**
    * @Description: 通用增删改查 version1.0
    * @Param: [sql, args]
    * @return: int
    * @Author: hliu
    * @Date: 2023/2/8
    */
    public int update(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            //1.获取数据库的连接
            connection = JDBCUtils.getConnection();

            //2.预编译sql语句，返回PreparedStatement的实例
            preparedStatement = connection.prepareStatement(sql);

            //3.填充占位符号
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }

            //4.执行 execute():有返回结果true, 无返回结果false
            return preparedStatement.executeUpdate();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            //5.资源的关闭
            JDBCUtils.closeResource(connection, preparedStatement);
        }
    }

    @Test
    public void testUpdateWithTx() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            //取消数据的自动提交
            System.out.println(connection.getAutoCommit());
            connection.setAutoCommit(false);

            String sql1 = "update user_table set balance = balance - 100 where user = ?";
            update(connection, sql1, "AA");

            //模拟网络异常
            System.out.println(10 / 0);

            String sql2 = "update user_table set balance = balance + 100 where user = ?";
            update(connection, sql2, "BB");

            System.out.println("转账成功！");

            //提交数据
            connection.commit();
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    /**
    * @Description: 通用增删改 version2.0
    * @Param: [connection, sql, args]
    * @return: int
    * @Author: hliu
    * @Date: 2023/2/8
    */
    public int update(Connection connection, String sql, Object... args) {
        PreparedStatement preparedStatement = null;
        try {
            //1.预编译sql语句，返回PreparedStatement的实例
            preparedStatement = connection.prepareStatement(sql);

            //2.填充占位符号
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }

            //3.执行 execute():有返回结果true, 无返回结果false
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            //4.资源的关闭
            JDBCUtils.closeResource(null, preparedStatement);
        }
    }

    /**
     *
     * 设置隔离级别
     *
     */
    @Test
    public void testTransactionSelect() throws Exception{
        Connection connection = JDBCUtils.getConnection();
        //设置隔离级别
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        System.out.println(connection.getTransactionIsolation());

        connection.setAutoCommit(false);

        String sql = "select user,password,balance from user_table where user = ?";
        User user = getInstance(connection, User.class, sql, "CC");
        System.out.println(user);
    }

    @Test
    public void testTransactionUpdate() throws Exception{
        Connection connection = JDBCUtils.getConnection();
        System.out.println(connection.getTransactionIsolation());
        connection.setAutoCommit(false);
        String sql = "update user_table set balance = ? where user = ?";
        update(connection, sql, 5000, "CC");

        Thread.sleep(15000);
        System.out.println("修改结束！");
    }

    /**
    * @Description: 通用的查询操作，返回一条记录，version 2.0
    * @Param: [connection, clazz, sql, args]
    * @return: T
    * @Author: hliu
    * @Date: 2023/2/8
    */
    public <T> T getInstance(Connection connection, Class<T> clazz, String sql, Object... args) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {

            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }

            resultSet = preparedStatement.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            if (resultSet.next()) {
                T t = null;
                try {
                    t = clazz.newInstance();
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < columnCount; i++) {
                    Object object = resultSet.getObject(i + 1);

                    //获取每个列的列名
                    String columnName = metaData.getColumnLabel(i + 1);


                    //给customer对象的某个属性，赋值为value, 通过反射
                    Field declaredField = clazz.getDeclaredField(columnName);
                    declaredField.setAccessible(true);
                    declaredField.set(t, object);
                }
                return t;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(null, preparedStatement, resultSet);
        }

        return null;
    }
}


