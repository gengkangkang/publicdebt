package com.kevin.dao;

import java.util.List;
import java.util.Map;

import com.kevin.model.House;
import com.kevin.model.User;

public interface HouseDAO {

	public int insertHouse(House house);
	public int updateHashByNum(Map<String,String> map);
	public List<House> queryHouseInfoByID(Map<String,String> map);


}
