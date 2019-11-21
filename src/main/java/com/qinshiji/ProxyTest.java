package com.qinshiji;

import com.qinshiji.dao.UserDao;
import com.qinshiji.dao.UserDaoImpl;
import com.qinshiji.proxy.ProxyUtil;

import java.lang.reflect.Proxy;

/**
 * @author qinshiji
 * @date 2019/11/21 11:31
 */
public class ProxyTest {
    public static void main(String[] args) {
        UserDao userDao = (UserDao) ProxyUtil.getProxyInstance(new UserDaoImpl());
        userDao.query();
        System.out.println(userDao.query("测试"));
//        Proxy.newProxyInstance()
    }
}
