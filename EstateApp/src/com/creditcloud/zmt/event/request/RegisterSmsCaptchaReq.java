package com.creditcloud.zmt.event.request;

import com.creditcloud.api.ApiConstants;
import com.creditcloud.event.ApiRequest;

/** 
 * @author yangkx
 * @mark 发送手机验证码短信
 */
public class RegisterSmsCaptchaReq extends ApiRequest {
	public RegisterSmsCaptchaReq(String captcha_answer, String mobile,String captcha_token) {
		super(ApiConstants.API_SMS_CAPTCHA + "?captcha_token=" + captcha_token + "&captcha_answer=" + captcha_answer);
		params.put("mobile", mobile); //手机号
		params.put("smsFlag", "01");
	}
}