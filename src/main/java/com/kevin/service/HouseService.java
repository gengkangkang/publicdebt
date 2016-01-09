package com.kevin.service;

import java.util.List;
import java.util.Map;

import com.kevin.model.House;

public interface HouseService {

	public int insertHouse(House house);
	public int updateHashByNum(Map<String,String> map);
	public List<House> queryHouseInfoByID(Map<String,String> map);


}
