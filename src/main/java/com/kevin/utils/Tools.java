package com.kevin.utils;

import java.util.Locale;

/**
 * 相关工具类
 * @author gengkangkang
 *
 * 2016年1月9日 下午11:52:39
 * Email:  tjeagle@163.com
 */
public class Tools {
	private final static char[] mChars = "0123456789ABCDEF".toCharArray(); 
	private final static String mHexStr = "0123456789ABCDEF"; 
	
	public static String hexStr2Str(String hexStr) {
		hexStr = hexStr.toString().trim().replace(" ", "").toUpperCase(Locale.US);
		char[] hexs = hexStr.toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int iTmp = 0x00;
		;
		for (int i = 0; i < bytes.length; i++) {
			iTmp = mHexStr.indexOf(hexs[2 * i]) << 4;
			iTmp |= mHexStr.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (iTmp & 0xFF);
		}
		return new String(bytes);
	}

	public static String getHash(String CommitEntryMsg){
		return CommitEntryMsg.substring(14,78);
	}
}
