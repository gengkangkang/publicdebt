package com.kevin.controller;

import java.util.Locale;

public class test {
	
	private final static char[] mChars = "0123456789ABCDEF".toCharArray(); 
	private final static String mHexStr = "0123456789ABCDEF";   

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		  String msg="00015226b080e5ca07eff0f028f2952e57e6ac3dcb2d85e5194d242d81c0a2e52754444c546d4101018c529142c5126b7740cd288ba4f3abb1f74017234d47889ba489081c4d39a3fe2e233b1cbb07896848f89b9dee775d2b9630e22121a149c2e60134359bc9bbbef7f3ce53d7346106ed357611b229cca99e57396de6b297b5952ce4e058fc07"; 
//		  System.out.println(msg.substring(14,78));
		
		String content="00e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b8550000";
		System.out.println(hexStr2Str(content));

	}
	
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

}
