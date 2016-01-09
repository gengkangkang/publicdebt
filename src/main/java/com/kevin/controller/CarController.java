package com.kevin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kevin.model.Car;
import com.kevin.model.User;
import com.kevin.service.CarService;
import com.kevin.service.UserService;

@Controller
@RequestMapping("/api/blockchain/")
public class CarController {
	@Autowired
	private CarService carService;

	@RequestMapping(value="saveCarInfo",method=RequestMethod.POST)
	@ResponseBody
    public String saveCarInfo(){
		System.out.println("========================================come in saveCarInfo===========");
		Car car=new Car();
        car.setCARNO("沪A0002");
        car.setCARBRAND("奥迪");
        car.setCARPRICE("30W");
        car.setBUYDATE("20130618");
		int k=carService.insertCar(car);
		System.out.println("saveCarInfo return======="+k);
		return "{\"HELLO\":\"23\"}";
    }
}
