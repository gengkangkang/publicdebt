package com.kevin.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * http请求工具类
 * @author gengkangkang
 *
 * 2016年1月9日 下午10:50:02
 * Email:  tjeagle@163.com
 */
public class HttpClientUtil {
	
	

	public static String postString(String url,String data,String inEncode,String outEncode) throws Exception{
				
		CloseableHttpClient httpclient = null;			
		HttpEntity httpEntity = null;
		String returnValue=null;
		try{
			 RequestConfig config = RequestConfig.custom().setConnectTimeout(90000).setSocketTimeout(90000).build();
			 httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

	    	StringEntity strEntity = new StringEntity(data, inEncode);
			HttpPost httppost=new HttpPost(url);
			httppost.setEntity(strEntity);
			httppost.setHeader("Content-Type", "application/json");
			
			System.out.println("执行post地址："+httppost.getURI()+" post参数："+data.toString());
			CloseableHttpResponse response=httpclient.execute(httppost);
			System.out.println("返回code=="+response.getStatusLine().getStatusCode());
		    httpEntity=response.getEntity();
		    if(httpEntity !=null){
		    	  returnValue=EntityUtils.toString(httpEntity,outEncode);
		    }
		  EntityUtils.consume(httpEntity);
		  response.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{			
			httpclient.close();
		}
		System.out.println("post返回值："+returnValue);							
       return returnValue;				
	}
	
	
	public static boolean postStringCode(String url,String data,String inEncode,String outEncode) throws Exception{
		
		CloseableHttpClient httpclient = null;			
		HttpEntity httpEntity = null;
		String returnValue=null;
		boolean flag=false;
		try{
			 RequestConfig config = RequestConfig.custom().setConnectTimeout(90000).setSocketTimeout(90000).build();
			 httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

	    	StringEntity strEntity = new StringEntity(data, inEncode);
			HttpPost httppost=new HttpPost(url);
			httppost.setEntity(strEntity);
			httppost.setHeader("Content-Type", "application/json");
			
			System.out.println("postStringCode执行post地址："+httppost.getURI()+" post参数："+data.toString());
			CloseableHttpResponse response=httpclient.execute(httppost);
			int code=response.getStatusLine().getStatusCode();
			System.out.println("postStringCode返回code=="+code);
			if(code==200){
				flag=true;
			}
		    httpEntity=response.getEntity();
		    if(httpEntity !=null){
		    	  returnValue=EntityUtils.toString(httpEntity,outEncode);
		    }
		  EntityUtils.consume(httpEntity);
		  response.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{			
			httpclient.close();
		}
		System.out.println("postStringCode post返回值："+flag);							
       return flag;				
	}
	
			
	
	public static String getUrl(String url,String inEncode,String outEncode) throws Exception{
				
		CloseableHttpClient httpclient = null;			
		HttpEntity httpEntity = null;
		String returnValue=null;
		try{		
			 RequestConfig config = RequestConfig.custom().setConnectTimeout(90000).setSocketTimeout(90000).build();
			 httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
				  
		    HttpGet httpget=new HttpGet(url);
		    				    
		    httpget.setHeader("Content-Type", "application/json");	
		    System.out.println("执行的get地址为： "+url);
			CloseableHttpResponse response=httpclient.execute(httpget);
		    httpEntity=response.getEntity();
		    if(httpEntity !=null){
		    	  returnValue=EntityUtils.toString(httpEntity,outEncode);
		    }
		  EntityUtils.consume(httpEntity);
		  response.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			httpclient.close();
		}
		System.out.println("get返回值："+returnValue);							
       return returnValue;				
	}

}
