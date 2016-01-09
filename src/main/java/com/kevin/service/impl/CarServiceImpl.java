package com.kevin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kevin.dao.CarDAO;
import com.kevin.model.Car;
import com.kevin.service.CarService;

@Service
public class CarServiceImpl implements CarService {
	
    @Autowired
    private CarDAO carDAO;

	@Override
	public int insertCar(Car car) {
		// TODO Auto-generated method stub
		return carDAO.insertCar(car);
	}

}
