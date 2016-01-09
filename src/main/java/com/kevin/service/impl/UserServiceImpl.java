package com.kevin.service.impl;

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

}
