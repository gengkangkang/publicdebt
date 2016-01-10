package com.kevin.dao;

import java.util.List;
import java.util.Map;

import com.kevin.model.Car;
import com.kevin.model.House;

public interface CarDAO {

	public int insertCar(Car car);
	public int updateCarHash(Map<String,String> map);
	public List<Car> queryCarInfoByID(Map<String,String> map);

}
