package com.kevin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.kevin.blockchain.SendToFactom;
import com.kevin.model.Car;
import com.kevin.model.House;
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
    public Map<String,String> saveCarInfo(@RequestParam("CARNO") String CARNO,
    		@RequestParam("CARBRAND") String CARBRAND, 
    		@RequestParam("CARPRICE") String CARPRICE,
    		@RequestParam("BUYDATE") String BUYDATE){
		System.out.println("========================================come in saveCarInfo===========");
		
		String code=null;
		String msg=null;
		Map<String,String> rmap=new HashMap<String,String>();
	
		try{
			Car car=new Car();
	        car.setCARNO(CARNO);
	        car.setCARBRAND(CARBRAND);
	        car.setCARPRICE(CARPRICE);
	        car.setBUYDATE(BUYDATE);
			int k=carService.insertCar(car);
		System.out.println("saveCarInfo return======="+k);
		if(k>0){
			//写入区块链中
			String data=JSON.toJSONString(car);
			String hash=SendToFactom.send(data);
			if(null != hash){
				Map<String,String> paramap=new HashMap<String,String>();
				paramap.put("HASHNUM", hash);
				paramap.put("CARNO", car.getCARNO());
				int g=carService.updateCarHash(paramap);
				System.out.println("g====="+g);
				if(g>0){
					code="0000";
					msg="车辆信息记录成功";
				}else{
					code="0001";
					msg="车辆信息记录失败";
				}
			}
		}else{
			//return
			code="9998";
			msg="车辆信息写入失败";
		}
		}catch(Exception e){
			code="9999";
			msg="系统异常";
			e.printStackTrace();
		}finally{
		rmap.put("code", code);
		rmap.put("msg", msg);
		}
		return rmap;
		
    }
	
	@RequestMapping(value="queryCarInfoByID",method=RequestMethod.GET)
	@ResponseBody
    public Map<String,Object> queryCarInfoByID(@RequestParam("CARNO") String CARNO){
		System.out.println("========================================come in queryCarInfoByID===========");
		
		String code=null;
		String msg=null;
		List<Car> list=new ArrayList<Car>();
		Map<String,Object> rmap=new HashMap<String,Object>();
	
		try{
			Map<String,String> map=new HashMap<String,String>();
			map.put("CARNO", CARNO);
			list=carService.queryCarInfoByID(map);
		
		}catch(Exception e){
			code="9999";
			msg="系统异常";
			e.printStackTrace();
		}finally{
		rmap.put("code", code);
		rmap.put("msg", msg);
		rmap.put("data", list);
		}
		return rmap;
		
    }
}
