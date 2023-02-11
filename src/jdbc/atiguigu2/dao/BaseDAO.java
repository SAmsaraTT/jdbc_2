package jdbc.atiguigu2.dao;

import com.atguigu.util.JDBCUtils;
import jdk.nashorn.internal.ir.SplitReturn;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 封装了针对于数据表的通用操作
 * @Author hliu
 * @Date 2023/2/10 16:12
 * @Version 1.0
 */
public abstract class BaseDAO {
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

    /**
    * @Description: 通用的查询操作，返回多条记录，version 2.0
    * @Param: [connection, clazz, sql, args]
    * @return: java.util.List<T>
    * @Author: hliu
    * @Date: 2023/2/10
    */
    public <T> List<T> getList(Connection connection, Class<T> clazz, String sql, Object... args) {
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
            List<T> ret = new ArrayList<>();
            while (resultSet.next()) {
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

                ret.add(t);
            }
            return ret;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(null, preparedStatement, resultSet);
        }
    }

    /**
    * @Description: 查询特殊值的通用方法
    * @Param: [connection, sql, args]
    * @return: E
    * @Author: hliu
    * @Date: 2023/2/10
    */
    public <E> E getValue(Connection connection, String sql, Object ...args) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return (E) resultSet.getObject(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(null, preparedStatement, resultSet);
        }
        return null;
    }

}
