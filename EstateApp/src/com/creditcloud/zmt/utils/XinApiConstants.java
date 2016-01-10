package com.creditcloud.zmt.utils;

/**
 * @author yangkx
 * @mark http网络交互接口详表
 */
public class XinApiConstants {

	/**
	 * 首页推荐
	 */
	public final static String API_TUIJIAN ="/v2/loans/getRecommendLoans?product1st=FUDAI&product2nd=DEFAULT";
	
	/**
	 * 充值
	 */
	public final static String API_RECHARGE = "/v2/chinapay/deposit/%1$S";
	/**
	 * 提现
	 */
	public final static String API_DRAWMONEY = "/v2/chinapay/withdraw/%1$S";
	
	/**
	 * 我的产品明细列表
	 */
	public final static String API_PRODUCTDETAILLIST = "/v2/user/%1$s/invest/list/%2$S/10?status=FINISHED&status=PROPOSED&status=FROZEN&status=SETTLED&status=OVERDUE&status=BREACH&status=CLEARED";
	
	/**
	 * 我的理财明细列表
	 */
	public final static String API_MONEYDETAILLIST = "/v2/user/%1$s/funds?startDate=%2$s&endDate=%3$s&access_token=%4$s&type=ALL&allStatus=true&allOperation=true&page=1&pageSize=50";
	
	/**
	 * 检测银行卡签名
	 */
	public final static String API_CHECKBINDCARD = "/v2/chinapay/backBindCardWithSmsCaptcha/%1$s";  // 短信验证绑卡

	/**
	 * 绑卡接口
	 */
	public final static String API_BINDCARD = "/v2/chinapaymobile/bindCard";

	/**
	 * 绑定财富经理
	 */
	public final static String API_BINDWEALTHMANAGER = "/v5/tag_updateRefereeByUserId?access_token=%1$s";

	/**
	 * 获取用户是否绑定财富经理
	 */
	public final static String API_BINDORNOTWEALTHMANAGER = "/v5/tag_getRefereeByUserId?access_token=%1$s";

	/**
	 * 验证是否存在此财富经理
	 */
	public final static String API_EXISTORNOTWEALTHMANAGER = "/v2/users/check/empReferral?access_token=%1$s";

	/**
	 * 投标
	 */
	public final static String API_TENDER = "/v2/invest/tender/%1$s";
	
	/**
	 * 项目文档
	 */
	public final static String API_DOCUMENT = "/v2/loan/%1$s/documents";
	
	/**
	 * 手机短信验证码获取
	 */
	public final static String API_SMSCAPTCHA = "/v2/users/smsCaptcha/changePwd?mobile=%1$s";
	
	/**
	 * 绑卡验证码获取
	 */
//	public final static String API_SMSBINDCARD = "/smsCaptchaWithFlag/%1$s?access_token=%2$s;
			
	/**
	 * 检测验证码是否正确
	 */
	public final static String Api_CHECKCODE ="/v2/checkSMSCaptcha/%1$s";
}
