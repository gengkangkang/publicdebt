package com.kevin.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kevin.dao.CarDAO;
import com.kevin.dao.HouseDAO;
import com.kevin.model.Car;
import com.kevin.model.House;
import com.kevin.service.CarService;
import com.kevin.service.HouseService;

@Service
public class HouseServiceImpl implements HouseService {
	
    @Autowired
    private HouseDAO houseDAO;

	@Override
	public int insertHouse(House house) {
		// TODO Auto-generated method stub
		return houseDAO.insertHouse(house);
	}

	@Override
	public int updateHashByNum(Map<String, String> map) {
		// TODO Auto-generated method stub
		return houseDAO.updateHashByNum(map);
	}

}
