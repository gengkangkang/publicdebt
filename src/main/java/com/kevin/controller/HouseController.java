package com.kevin.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.kevin.blockchain.SendToFactom;
import com.kevin.model.House;
import com.kevin.service.HouseService;

@Controller
@RequestMapping("/api/blockchain/")
public class HouseController {
	@Autowired
	private HouseService houseService;

	@RequestMapping(value="saveHouseInfo",method=RequestMethod.POST)
	@ResponseBody
    public Map<String,String> saveHouseInfo(@RequestParam("HOURCENO") String HOURCENO,
    		@RequestParam("HOURCEAREA") String HOURCEAREA, 
    		@RequestParam("HOURCEADDR") String HOURCEADDR,
    		@RequestParam("HOURCERDATE") String HOURCERDATE){
		System.out.println("========================================come in saveHouseInfo===========");
		String code=null;
		String msg=null;
		Map<String,String> rmap=new HashMap<String,String>();
	
		try{
		House house=new House();
        house.setHOURCENO(HOURCENO);
        house.setHOURCEAREA(HOURCEAREA);
        house.setHOURCEADDR(HOURCEADDR);
        house.setHOURCERDATE(HOURCERDATE);
		int k=houseService.insertHouse(house);
		System.out.println("saveHouseInfo return======="+k);
		if(k>0){
			//写入区块链中
			String data=JSON.toJSONString(house);
			String hash=SendToFactom.send(data);
			if(null != hash){
				Map<String,String> paramap=new HashMap<String,String>();
				paramap.put("HASHNUM", hash);
				paramap.put("HOURCENO", house.getHOURCENO());
				int g=houseService.updateHashByNum(paramap);
				System.out.println("g====="+g);
				if(g>0){
					code="0000";
					msg="房屋信息记录成功";
				}else{
					code="0001";
					msg="房屋信息记录失败";
				}
			}
		}else{
			//return
			code="9998";
			msg="写入失败";
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
}
