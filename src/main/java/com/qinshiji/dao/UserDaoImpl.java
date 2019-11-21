package com.qinshiji.dao;

/**
 * @author qinshiji
 * @date 2019/11/21 14:06
 */
public class UserDaoImpl implements UserDao {
    @Override
    public void query() {
        System.out.println("query");
    }

    @Override
    public String query(String str) {
        return str;
    }
}
