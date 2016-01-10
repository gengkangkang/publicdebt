package com.kevin.service;

import java.util.List;
import java.util.Map;

import com.kevin.model.User;

public interface UserService {

	public int insertUser(User user);
	public int updateUserHash(Map<String,String> map);
	public List<User> queryUserInfo();
	public List<User> queryUserInfoByID(Map<String,String> map);

}
