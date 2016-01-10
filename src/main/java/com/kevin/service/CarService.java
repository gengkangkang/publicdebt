package com.kevin.service;

import java.util.List;
import java.util.Map;

import com.kevin.model.Car;

public interface CarService {

	public int insertCar(Car car);
	public int updateCarHash(Map<String,String> map);
	public List<Car> queryCarInfoByID(Map<String,String> map);

}
