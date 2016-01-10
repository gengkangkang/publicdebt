package com.kevin.service.impl;

import java.util.List;
import java.util.Map;

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

	@Override
	public int updateCarHash(Map<String, String> map) {
		// TODO Auto-generated method stub
		return carDAO.updateCarHash(map);
	}

	@Override
	public List<Car> queryCarInfoByID(Map<String, String> map) {
		// TODO Auto-generated method stub
		return carDAO.queryCarInfoByID(map);
	}

}
