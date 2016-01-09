package com.kevin.service;

import java.util.Map;

import com.kevin.model.House;

public interface HouseService {

	public int insertHouse(House house);
	public int updateHashByNum(Map<String,String> map);

}
