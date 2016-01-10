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
import com.kevin.model.User;
import com.kevin.service.UserService;

@Controller
@RequestMapping("/api/blockchain/")
public class UserController {
	@Autowired
	private UserService userService;

	@RequestMapping(value="saveUserInfo",method=RequestMethod.POST)
	@ResponseBody
    public Map<String,String> saveCarInfo(@RequestParam("NAME") String NAME,
    		@RequestParam("IDTYPE") String IDTYPE, 
    		@RequestParam("IDNUMBER") String IDNUMBER,
    		@RequestParam("HOURSEID") String HOURSEID,
    		@RequestParam("CARID") String CARID,
    		@RequestParam("PROPERTYPAYINFO") String PROPERTYPAYINFO){
		System.out.println("========================================come in saveUserInfo===========");
		
		String code=null;
		String msg=null;
		Map<String,String> rmap=new HashMap<String,String>();
	
		try{
			User user=new User();
            user.setNAME(NAME);
            user.setIDTYPE(IDTYPE);
            user.setIDNUMBER(IDNUMBER);
            user.setHOURSEID(HOURSEID);
            user.setCARID(CARID);
            user.setPROPERTYPAYINFO(PROPERTYPAYINFO);
			int k=userService.insertUser(user);
		System.out.println("saveUserInfo return======="+k);
		if(k>0){
			//写入区块链中
			String data=JSON.toJSONString(user);
			String hash=SendToFactom.send(data);
			if(null != hash){
				Map<String,String> paramap=new HashMap<String,String>();
				paramap.put("HASHNUM", hash);
				paramap.put("IDNUMBER", user.getIDNUMBER());
				int g=userService.updateUserHash(paramap);
				System.out.println("g====="+g);
				if(g>0){
					code="0000";
					msg="业主信息记录成功";
				}else{
					code="0001";
					msg="业主信息记录失败";
				}
			}
		}else{
			//return
			code="9998";
			msg="业主信息写入失败";
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
	
	
	@RequestMapping(value="queryUserInfo",method=RequestMethod.GET)
	@ResponseBody
    public Map<String,Object> queryUserInfo(){
		System.out.println("========================================come in queryUserInfo===========");
		
		String code=null;
		String msg=null;
		List<User> list=new ArrayList<User>();
		Map<String,Object> rmap=new HashMap<String,Object>();
	
		try{
			list=userService.queryUserInfo();
		
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
	
	@RequestMapping(value="queryUserInfoByID",method=RequestMethod.GET)
	@ResponseBody
    public Map<String,Object> queryUserInfoByID(@RequestParam("IDNUMBER") String IDNUMBER){
		System.out.println("========================================come in queryUserInfoByID===========");
		
		String code=null;
		String msg=null;
		List<User> list=new ArrayList<User>();
		Map<String,Object> rmap=new HashMap<String,Object>();
	
		try{
			Map<String,String> map=new HashMap<String,String>();
			map.put("IDNUMBER", IDNUMBER);
			list=userService.queryUserInfoByID(map);
		
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
