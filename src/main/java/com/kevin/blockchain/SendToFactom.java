package com.kevin.blockchain;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kevin.utils.Constants;
import com.kevin.utils.HttpClientUtil;
import com.kevin.utils.Tools;

/**
 * 将相关信息提交到factom
 * @author gengkangkang
 *
 * 2016年1月9日 下午11:27:00
 * Email:  tjeagle@163.com
 */
public class SendToFactom {
   public static final String send(String jsonData) throws Exception{
	   System.out.println("send to factom data is :"+jsonData);
	   Map<String,Object> sendpara=new HashMap<String,Object>();
	   sendpara.put("ChainID", Constants.ChainID);
	   String[] extIDs={"cm","cmiinv"};
	   sendpara.put("ExtIDs",extIDs);
	   sendpara.put("Content", jsonData);
	   String sendJson=JSON.toJSONString(sendpara);
	   System.out.println("sendJson===="+sendJson);
	   //1.compose-entry-submit
	   String back1= HttpClientUtil.postString(Constants.url1,sendJson,"UTF-8", "UTF-8");
	   JSONObject backObj=JSONObject.parseObject(back1);
	   JSONObject entryCommitObj=backObj.getJSONObject("EntryCommit");
	   JSONObject entryRevealObj=backObj.getJSONObject("EntryReveal");
	   //获取hash值
	   String CommitEntryMsg=entryCommitObj.getString("CommitEntryMsg");
	   String hash=Tools.getHash(CommitEntryMsg);
	   
	   //2.commit-entry
	   String entryCommitObjJson=entryCommitObj.toString();
	   System.out.println("entryCommitObjJson=="+entryCommitObjJson);
	   boolean flag=HttpClientUtil.postStringCode(Constants.url2, entryCommitObjJson,"UTF-8", "UTF-8");
	   System.out.println("flag=="+flag);

	   //3.reveal-chain
	   String entryRevealObjJson=entryRevealObj.toString();
	   System.out.println("entryRevealObjJson=="+entryRevealObjJson);
	   boolean flag1=HttpClientUtil.postStringCode(Constants.url3, entryRevealObjJson,"UTF-8", "UTF-8");
	   System.out.println("flag1=="+flag1);
	   
	   return hash;
	   
   }
}
