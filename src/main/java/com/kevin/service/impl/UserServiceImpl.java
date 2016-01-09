package com.kevin.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kevin.dao.UserDAO;
import com.kevin.model.User;
import com.kevin.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
    @Autowired
    private UserDAO userDAO;

	@Override
	public int insertUser(User user) {
		// TODO Auto-generated method stub
		return userDAO.insertUser(user);
	}

	@Override
	public int updateUserHash(Map<String, String> map) {
		// TODO Auto-generated method stub
		return userDAO.updateUserHash(map);
	}

	@Override
	public List<User> queryUserInfo() {
		// TODO Auto-generated method stub
		return userDAO.queryUserInfo();
	}

	@Override
	public List<User> queryUserInfoByID(Map<String, String> map) {
		// TODO Auto-generated method stub
		return userDAO.queryUserInfoByID(map);
	}

}
