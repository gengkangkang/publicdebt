package com.kevin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kevin.model.User;
import com.kevin.service.UserService;

@Controller
@RequestMapping("/")
public class UserController {
	@Autowired
	private UserService userService;

	@RequestMapping(value="index",method=RequestMethod.GET)
	@ResponseBody
    public String index(){
		System.out.println("========================================hello");
		User user=new User();
		user.setState(3);
		user.setNickname("中国");
		int k=userService.insertUser(user);
		System.out.println("k======="+k);
		return "{\"HELLO\":\"23\"}";
    }
}
