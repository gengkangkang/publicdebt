package com.kevin.dao;

import java.util.Map;

import com.kevin.model.House;

public interface HouseDAO {

	public int insertHouse(House house);
	public int updateHashByNum(Map<String,String> map);

}
