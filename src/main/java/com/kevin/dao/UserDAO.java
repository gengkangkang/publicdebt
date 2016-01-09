package com.kevin.dao;

import java.util.List;
import java.util.Map;

import com.kevin.model.User;

public interface UserDAO {

	public int insertUser(User user);
	public int updateUserHash(Map<String,String> map);
	public List<User> queryUserInfo();
	public List<User> queryUserInfoByID(Map<String,String> map);



}
