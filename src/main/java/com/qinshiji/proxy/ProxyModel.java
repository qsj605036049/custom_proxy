package com.qinshiji.proxy;

import com.qinshiji.dao.UserDao;

/**
 * @author qinshiji
 * @date 2019/11/21 11:33
 */
public class ProxyModel implements UserDao {
    private UserDao userDao;

    public ProxyModel(UserDao userDao){
        this.userDao = userDao;
    }

    @Override
    public void query() {
        System.out.println("proxy");
        userDao.query();
    }

    @Override
    public String query(String str) {
        return null;
    }
}
