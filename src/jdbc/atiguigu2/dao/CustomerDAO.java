package jdbc.atiguigu2.dao;

import jdbc.atiguigu2.bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * @Description 此接口用于规范针对于customers表的常用操作
 * @Author hliu
 * @Date 2023/2/10 16:40
 * @Version 1.0
 */
public interface CustomerDAO {
    /**
    * @Description: 将customer对象添加到数据库中
    * @Param: [connection, customer]
    * @return: void
    * @Author: hliu
    * @Date: 2023/2/10
    */
    void insert(Connection connection, Customer customer);

    /**
    * @Description: 根据指定的id删除表中的一条记录
    * @Param: [connection, id]
    * @return: void
    * @Author: hliu
    * @Date: 2023/2/10
    */
    void deleteById(Connection connection, int id);

    /**
    * @Description: 针对customer对象修改数据库表中的记录！
    * @Param: [connection]
    * @return: void
    * @Author: hliu
    * @Date: 2023/2/10
    */
    void update(Connection connection, Customer customer);

    /**
    * @Description: 根据指定id查询指定customer对象
    * @Param: [connection, id]
    * @return: jdbc.atiguigu2.bean.Customer
    * @Author: hliu
    * @Date: 2023/2/10
    */
    Customer getCustomerbyId(Connection connection, int id);
    
    /**
    * @Description: 查询表中的所有记录
    * @Param: [connection]
    * @return: java.util.List<jdbc.atiguigu2.bean.Customer>
    * @Author: hliu
    * @Date: 2023/2/10
    */
    List<Customer> getAll(Connection connection);
    
    /**
    * @Description: 返回数据表的数量
    * @Param: [connection]
    * @return: java.lang.Long
    * @Author: hliu
    * @Date: 2023/2/10
    */
    Long getCount(Connection connection);
    
    /**
    * @Description: 返回数据表中最大的生日
    * @Param: [connection]
    * @return: java.sql.Date
    * @Author: hliu
    * @Date: 2023/2/10
    */
    Date getMaxBirth(Connection connection);
}
