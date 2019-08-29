package com.hxy.user.server.dao;

import com.hxy.user.server.bean.model.UserModel;

public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserModel record);

    int insertSelective(UserModel record);

    UserModel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserModel record);

    int updateByPrimaryKey(UserModel record);

    /**
     * 根据用户名查询
     * @param username
     * @return
     */
    UserModel selectByUsername(String username);

    /**
     * 校验用户名是否存在
     * @param username
     * @return
     */
    int checkUserName(String username);

    /**
     * 校验email是否存在
     * @param email
     * @return
     */
    int checkEmail(String email);
}