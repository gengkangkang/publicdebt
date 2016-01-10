package com.creditcloud.zmt.utils;

import com.estate.R;

import android.content.Context;

public class StatusString {

	public static String getRepaymentMode(String mode) {
		String modeString = "";
		if (mode.equals("MonthlyInterest")) {
			modeString = "按月付息到期还本";
		}
		if (mode.equals("EqualInstallment")) {
			modeString = "按月等额本息";
		}
		if (mode.equals("EqualPrincipal")) {
			modeString = "按月等额本金";
		}
		if (mode.equals("BulletRepayment")) {
			modeString = "一次性还本付息";
		}
		if (mode.equals("EqualInterest")) {
			modeString = "按月平息";
		}
		return modeString;
	}

	public static String getTouziStatus(String status) {
		String statusString = "已完成";
		if (status.equals("OPENED")) {
			statusString = "投资";
		}
		if (status.equals("FINISHED")) {
			statusString = "已售罄";
		}
		if (status.equals("SETTLED")) {//资金已进入借款人账户
			statusString = "已起息";
		}
		if (status.equals("CLEARED")) {//还款成功结束
			statusString = "已还款";
		}
		if (status.equals("OVERDUE")) {//逾期未归还
			statusString = "还款中";
		}
		if (status.equals("BREACH")) {//贷款违约
			statusString = "审核中";
		}
		if (status.equals("ARCHIVED")) {//已存档
			statusString = "已完成";
		}
		if (status.equals("SCHEDULED")) {//
			statusString = "即将开始";
		}
		return statusString;
	}

	public static String getBankName(String acronym) {
		String bankName = acronym;
		if (acronym.equals("CMBC")) {
			bankName = "中国民生银行";
		}
		if (acronym.equals("ABC")) {
			bankName = "中国农业银行";
		}
		if (acronym.equals("CMB")) {
			bankName = "招商银行";
		}
		if (acronym.equals("HXB")) {
			bankName = "华夏银行";
		}
		if (acronym.equals("SPDB")) {
			bankName = "上海浦东发展银行";
		}
		if (acronym.equals("CITIC")) {
			bankName = "中信银行";
		}
		if (acronym.equals("CEB")) {
			bankName = "中国光大银行";
		}
		if (acronym.equals("PAB")) {
			bankName = "平安银行";
		}
		if (acronym.equals("CIB")) {
			bankName = "兴业银行";
		}
		if (acronym.equals("PSBC")) {
			bankName = "中国邮政储蓄银行";
		}
		if (acronym.equals("BOCOM")) {
			bankName = "交通银行";
		}
		if (acronym.equals("CCB")) {
			bankName = "中国建设银行";
		}
		if (acronym.equals("ICBC")) {
			bankName = "中国工商银行";
		}
		return bankName;
	}
	
	public static String getAcronym(String bankName) {
		String acronym = bankName;
		if (bankName.equals("中国民生银行")) {
			acronym = "CMBC";
		}
		if (bankName.equals("中国农业银行")) {
			acronym = "ABC";
		}
		if (bankName.equals("招商银行")) {
			acronym = "CMB";
		}
		if (bankName.equals("华夏银行")) {
			acronym = "HXB";
		}
		if (bankName.equals("上海浦东发展银行")) {
			acronym = "SPDB";
		}
		if (bankName.equals("中信银行")) {
			acronym = "CITIC";
		}
		if (bankName.equals("中国光大银行")) {
			acronym = "CEB";
		}
		if (bankName.equals("平安银行")) {
			acronym = "PAB";
		}
		if (bankName.equals("兴业银行")) {
			acronym = "CIB";
		}
		if (bankName.equals("中国邮政储蓄银行")) {
			acronym = "PSBC";
		}
		if (bankName.equals("交通银行")) {
			acronym = "BOCOM";
		}
		if (bankName.equals("中国建设银行")) {
			acronym = "CCB";
		}
		if (bankName.equals("中国工商银行")) {
			acronym = "ICBC";
		}
		return bankName;
	}

	public static int getBankIconResId(Context context, String acronym) {
		int resId = 0;
		if (acronym.equals("CMBC")) {
			resId = R.drawable.img_cmbc;
		}
		if (acronym.equals("ABC")) {
			resId = R.drawable.img_abc;
		}
		if (acronym.equals("CMB")) {
			resId = R.drawable.img_cmb;
		}
		if (acronym.equals("HXB")) {
			resId = R.drawable.img_hxb;
		}
		if (acronym.equals("SPDB")) {
			resId = R.drawable.img_spdb;
		}
		if (acronym.equals("CITIC")) {
			resId = R.drawable.img_ecitic;
		}
		if (acronym.equals("CEB")) {
			resId = R.drawable.img_ceb;
		}
		if (acronym.equals("PAB")) {
			resId = R.drawable.img_pingan;
		}
		if (acronym.equals("CIB")) {
			resId = R.drawable.img_cib;
		}
		if (acronym.equals("PSBC")) {
			resId = R.drawable.img_psbc;
		}
		if (acronym.equals("BOCOM")) {
			resId = R.drawable.img_boc;
		}
		if (acronym.equals("CCB")) {
			resId = R.drawable.img_ccb;
		}
		if (acronym.equals("ICBC")) {
			resId = R.drawable.img_icbc;
		}
		return resId;
	}

	public static String getMoneyLabel(String acronym) {
		String label = "";
		if (acronym.equals("INVEST")) {
			label = "投标";
		}
		if (acronym.equals("WITHDRAW")) {
			label = "取现";
		}
		if (acronym.equals("DEPOSIT")) {
			label = "充值";
		}
		if (acronym.equals("LOAN")) {
			label = "放款";
		}
		if (acronym.equals("LOAN_REPAY")) {
			label = "贷款还款";
		}
		if (acronym.equals("DISBURSE")) {
			label = "垫付还款";
		}
		if (acronym.equals("INVEST_REPAY")) {
			label = "投资还款";
		}
		if (acronym.equals("CREDIT_ASSIGN")) {
			label = "债权转让";
		}
		if (acronym.equals("TRANSFER")) {
			label = "转账扣款";
		}
		if (acronym.equals("REWARD_REGISTER")) {
			label = "注册奖励";
		}
		if (acronym.equals("REWARD_INVEST")) {
			label = "投标奖励";
		}
		if (acronym.equals("REWARD_DEPOSIT")) {
			label = "充值奖励";
		}
		if (acronym.equals("FEE_WITHDRAW")) {
			label = "提现手续费";
		}
		if (acronym.equals("FEE_AUTHENTICATE")) {
			label = "身份验证手续费";
		}
		if (acronym.equals("FEE_INVEST_INTEREST")) {
			label = "汇款利息管理费";
		}
		if (acronym.equals("FEE_LOAN_SERVICE")) {
			label = "借款服务费";
		}
		if (acronym.equals("FEE_LOAN_MANAGE")) {
			label = "借款管理费";
		}
		if (acronym.equals("FEE_LOAN_INTEREST")) {
			label = "还款利息管理费";
		}
		if (acronym.equals("FEE_LOAN_VISIT")) {
			label = "实地考察费";
		}
		if (acronym.equals("FEE_LOAN_GUARANTEE")) {
			label = "担保费";
		}
		if (acronym.equals("FEE_LOAN_RISK")) {
			label = "风险管理费";
		}
		if (acronym.equals("FEE_LOAN_OVERDUE")) {
			label = "逾期管理费";
		}
		if (acronym.equals("FEE_LOAN_PENALTY")) {
			label = "逾期罚息";
		}
		if (acronym.equals("FEE_LOAN_PENALTY_INVEST")) {
			label = "逾期罚息";
		}
		if (acronym.equals("FEE_DEPOSIT")) {
			label = "充值手续费";
		}
		if (acronym.equals("FEE_ADVANCE_REPAY")) {
			label = "提前还款违约金";
		}
		if (acronym.equals("FEE_ADVANCE_REPAY_INVEST")) {
			label = "提前还款违约金";
		}
		if (acronym.equals("FSS")) {
			label = "生利宝";
		}
		return label;

	}
}
