package com.creditcloud.zmt.activity;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.core.utils.OnClickUtil;
import com.android.core.utils.Text.MatchUtils;
import com.android.core.utils.Text.StringUtils;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.creditcloud.api.ApiConstants;
import com.creditcloud.event.ApiResponse;
import com.creditcloud.event.request.SmsCaptchaRequest1;
import com.creditcloud.model.FundAccount;
import com.creditcloud.zmt.event.response.GlobalResponse;
import com.creditcloud.zmt.utils.GlobalWlanErrorListener;
import com.creditcloud.zmt.utils.StatusString;
import com.estate.BaseActivity;
import com.estate.R;
import com.estate.event.request.CheckCodeRequest;
import com.estate.event.request.DrawMoneyRequest;
import com.estate.event.request.RechargeRequest;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author yangkx
 * @mark 充值提现功能
 */
public class RechargeActivity extends BaseActivity implements OnCheckedChangeListener {

	private RadioGroup rg;
	private RelativeLayout recharge;
	private LinearLayout drawmoney;
	private TextView agree;
	private TextView chooseCard;
	private TextView avaliMoney;
	private TextView tv_drawMoney;
	private TextView mobileNum;
	private List<FundAccount> bank_list;
	private FundAccount chooseFundAccount;
	private EditText inputMoney;
	private EditText smsCode;
	private String avaliblemoney;
	private String drawMoney;
	private Map<String, Object> bankMap;
	private ImageView icon;
	public final static int REQUESTCODE = 1001;
	private CheckBox checkBox;
	private Button btn_code;
	private SmsContent content;
	private String mobileNo;
	private TextView notice;
	private CountDownTask task;
	
	@Override
	@TargetApi(14)
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		content = new SmsContent(new Handler());
		// 注册短信变化监听
		this.getContentResolver().registerContentObserver(
				Uri.parse("content://sms/"), true, content);
		setContentView(R.layout.activity_recharge);
		bankMap = preferenceStorageService.getBankName();
		
		if (bankMap == null) {
			getBankCard();
		}
		getIntents();
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(this.getClass().getName());
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getName());
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.getContentResolver().unregisterContentObserver(content);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if(task != null && !task.isCancelled()){
			task.cancel(true);
			task = null;
		}
	}
	
    /**
     * @param view
     * @mark 实现UI里定义的方法
     */
    public void back(View view) {
		if(task != null && !task.isCancelled()){
			task.cancel(true);
		}
		task = null;
		System.gc();
        finish();
    }

	private void getIntents() {
		Intent intent = this.getIntent();
		if (intent != null) {
			bank_list = (List<FundAccount>) intent
					.getSerializableExtra("banks");
			avaliblemoney = intent.getStringExtra("avalibleMoney");
			drawMoney = intent.getStringExtra("avalibleMoney");
		}
	}

	private void initView() {

		avaliMoney = (TextView) findViewById(R.id.tv_recharge_avaliblemoney);
		avaliMoney.setText(String.format(
				this.getString(R.string.avalidate_money), avaliblemoney));

		rg = (RadioGroup) findViewById(R.id.rg_recharge);
		rg.setOnCheckedChangeListener(this);

		drawmoney = (LinearLayout) findViewById(R.id.layout_recharge_drawmoney);

		recharge = (RelativeLayout) findViewById(R.id.layout_recharge_recharge);

		recharge.setVisibility(View.VISIBLE);
		drawmoney.setVisibility(View.GONE);

		agree = (TextView) findViewById(R.id.tv_agree);
		agree.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
		agree.setMovementMethod(ScrollingMovementMethod.getInstance());//textview自滚动   
		
		agree.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
//				MuPDFActivity.startAc(RechargeActivity.this, MuPDFActivity.ZHUANZHANG, "");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date curDate = new Date(System.currentTimeMillis());//获取当前时间 
				String date = format.format(curDate);
                Intent intent = new Intent(RechargeActivity.this, ProtocolActivity.class);
				intent.putExtra(ProtocolActivity.EXTRA_NAME_AUTHORIZE_NAME, preferenceStorageService.getUsername());
				intent.putExtra(ProtocolActivity.EXTRA_NAME_LOGIN_NAME, preferenceStorageService.getGroupId());
				intent.putExtra(ProtocolActivity.EXTRA_NAME_IDENTITY_CARD, preferenceStorageService.getCardNum());
				intent.putExtra(ProtocolActivity.EXTRA_NAME_ACCOUNT_NAME, bank_list.get(0).getAccount().getName());
				intent.putExtra(ProtocolActivity.EXTRA_NAME_BANK_NAME, bank_list.get(0).getAccount().getBank());
				intent.putExtra(ProtocolActivity.EXTRA_NAME_BANK_ACCOUNT, bank_list.get(0).getAccount().getAccount());
				intent.putExtra(ProtocolActivity.EXTRA_NAME_DATE, date);
				startActivity(intent);
			}
		});
		
		notice = (TextView) findViewById(R.id.tv_notice);
		notice.setMovementMethod(ScrollingMovementMethod.getInstance());

		icon = (ImageView) findViewById(R.id.iv_bankicon);

		mobileNum = (TextView) findViewById(R.id.tv_recharge_tel);
		chooseCard = (TextView) findViewById(R.id.tv_recharge_bankname);

		for (FundAccount account : bank_list) {
			if (account.isDefaultAccount()) {
				setChooseCardText(account);
				setBankIcon(account);
				setMobileText(account);
			}
		}

		inputMoney = (EditText) findViewById(R.id.et_recharge_inputmoney);

		tv_drawMoney = (TextView) findViewById(R.id.tv_recharge_price);
		tv_drawMoney.setText(String.format(this.getString(R.string.price_notice), drawMoney));
		smsCode  = (EditText) findViewById(R.id.et_code);
		checkBox = (CheckBox) findViewById(R.id.cb_agree);
		btn_code = (Button) findViewById(R.id.btn_getcode);
	}

	private void setChooseCardText(FundAccount account) {
		chooseCard.setText(setChooseCardFontText(account));
	}

	private void setBankIcon(FundAccount account) {
		int resId = StatusString.getBankIconResId(this, account.getAccount()
				.getBank());
		if (resId != 0) {
			icon.setBackgroundResource(resId);
			icon.setVisibility(View.VISIBLE);
		} else {
			icon.setVisibility(View.INVISIBLE);
		}
	}

	private void setMobileText(FundAccount account) {
		String mobile = account.getAccount().getBankMobile();
		mobileNo = mobile;
		if (mobile == null) {
			mobileNum.setText("该银行卡未绑定手机号");
		} else {
			mobileNum.setText(mobile.substring(0, 3) + "****"
					+ mobile.substring(7));
		}
	}

	private String setChooseCardFontText(FundAccount account) {
		if (bankMap == null) {
			return StatusString.getBankName(account.getAccount().getBank())
					+ " "
					+ "尾号"
					+ account
							.getAccount()
							.getAccount()
							.substring(
									account.getAccount().getAccount().length() - 4);
		}
		return (String) bankMap.get(account.getAccount().getBank())
				+ " "
				+ "尾号"
				+ account
						.getAccount()
						.getAccount()
						.substring(
								account.getAccount().getAccount().length() - 4);
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int checkId) {
		switch (checkId) {
		case R.id.rb_recharge_isrecharge:
			recharge.setVisibility(View.VISIBLE);
			drawmoney.setVisibility(View.GONE);
			checkBox.setVisibility(View.VISIBLE);
			agree.setVisibility(View.VISIBLE);
			agree.setText("我已阅读并同意《资金代扣及转账授权与承诺》");
			notice.setText("注意事项:\n1. 根据监管规定，资金需要遵循“同卡进出”的原则；\n2.中民i投仅允许用户账号绑定“一张”银行卡；\n3.如果特殊情况需要换卡，请先解绑当前卡片 ，再重新绑定新卡片；");
			break;

		case R.id.rb_recharge_isdrawmoney:
			recharge.setVisibility(View.GONE);
			drawmoney.setVisibility(View.VISIBLE);
			checkBox.setVisibility(View.GONE);
			agree.setVisibility(View.GONE);
			notice.setText("提现说明：\n1. 提现手续费将根据《会员服务协议》收取。当前暂免。\n2. 当前仅支持全额提现，请确认绑定银行卡的状态是否正常，以免影响到账时间。\n3. 提现申请提交成功后，提现金额将会在1-2个工作日内转入您绑定的银行卡账户中（遇节假日将顺延）。");
			break;
		}
	}

	public void submit(View view) {
		if (!OnClickUtil.isFastDoubleClick(500)) {
			int id = rg.getCheckedRadioButtonId();
			String code   = smsCode.getText().toString().trim();
			String mobile = mobileNum.getText().toString().trim();
			String money  = inputMoney.getText().toString().trim();
			
			switch (id) {
			case R.id.rb_recharge_isrecharge:
				if (checkInput(code, mobile) && rechargeCheck(money)) {
					if (Double.valueOf(money) > 2000000) {
						showToast("单次充值最高两百万");
					} else {
            			// 进行充值
						checkCode(money, code, "CONFIRM_CREDITMARKET_DEPOSIT");
					}
				}
				break;
			case R.id.rb_recharge_isdrawmoney:
				if (checkInput(code, mobile) && drawmondyCheck()) {
					
        			// 进行提现
					checkCode(drawMoney, code, "CONFIRM_CREDITMARKET_WITHDRAW");
					
				}
				break;
			}
		}
	}

	private boolean drawmondyCheck() {
		if (Double.parseDouble(drawMoney) <= 0) {
			showToast("提现金额为0元");
			return false;
		}
		return true;
	}

	private boolean rechargeCheck(String money) {

		if (TextUtils.isEmpty(money)) {
			showToast("请输入金额");
			return false;
		}
		if (Double.parseDouble(money) <= 0) {
			showToast("充值金额必须大于0元");
			return false;
		}

		return true;
	}

	public boolean checkInput(String code, String mobile) {

		if (TextUtils.isEmpty(code)) {
			showToast("请输入验证码");
			return false;
		}

		if (StringUtils.containsChinese(mobile)) {
			showToast("银行卡未绑定手机号,不能进行操作");
			return false;
		}

		if (!checkBox.isChecked()) {
			showToast("协议未选中");
			return false;
		}

		return true;
	}

	public void sendCode(View view) {

		String mobile = mobileNum.getText().toString().trim();
		String checkMoney = inputMoney.getText().toString().trim();

		if (StringUtils.containsChinese(mobile)) {
			showToast("银行卡未绑定手机号,不能进行操作");
			return;
		}

		/*if (!rechargeCheck(checkMoney)) {
			showToast("请输入大于零金额");
			return;
		}*/
		getCodeNetwork();
	}

	public static void startAc(Context context, List<FundAccount> banks,
			String avalibleMoney) {
		Intent intent = new Intent(context, RechargeActivity.class);
		intent.putExtra("banks", (Serializable) banks);
		intent.putExtra("avalibleMoney", avalibleMoney);
		intent.putExtra("withdrawAcount", avalibleMoney);
		context.startActivity(intent);
	}

	private void getBankCard() {
		StringRequest request = new StringRequest(ApiConstants.URL_BASE
				+ ApiConstants.API_BANKNAME, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				preferenceStorageService.setBankName(response);
				bankMap = preferenceStorageService.getBankName();
				for (FundAccount account : bank_list) {
					if (account.isDefaultAccount()) {
						setChooseCardText(account);
					}
				}
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
			}
		}, true);
		httpService.addToRequestQueue(request);
	}

	private void checkCode(final String money, final String code, String type) {
		String userId = preferenceStorageService.getUserId();
		CheckCodeRequest request = new CheckCodeRequest(userId, code, type);
		sendCommend("加载中", CMD_UPDATE_CURRENT_PROGRESS_SHOW);
		
		volleyHttpClient.postOAuthWithParams(request.getUrl(Method.POST),
				GlobalResponse.class, request.getParams(),
				new Response.Listener<GlobalResponse>() {

					@Override
					public void onResponse(GlobalResponse response) {
						sendCommend("加载中", CMD_UPDATE_CURRENT_PROGRESS_CANCEL);
						if (response != null) {
							if (response.isSuccess()) {
								int id = rg.getCheckedRadioButtonId();
								switch (id) {
								case R.id.rb_recharge_isrecharge:
									recharge(money, code);
									break;
								case R.id.rb_recharge_isdrawmoney:
									drawMoney(money, code);
									break;
								}

							} else {

								if (response.isError()) {
									showToast(response.getError().get(0)
											.getMessage());
								} else {
									showToast("验证码检测失败，请重试");
								}
							}
						}
					}
				}, new GlobalWlanErrorListener(this) {
					@Override
					public void onErrorResponse(VolleyError error) {
						super.onErrorResponse(error);
						setWlanFalseMessage("验证码检测失败，请重试");
					}
				}, false);
	}

	private void recharge(String money, String code) {
		sendCommend("加载中", CMD_UPDATE_CURRENT_PROGRESS_SHOW);
		String userId = preferenceStorageService.getUserId();
		RechargeRequest request = new RechargeRequest(userId, money, code);
		volleyHttpClient.postOAuthWithParams(request.getUrl(Method.POST),
				GlobalResponse.class, request.getParams(),
				new Response.Listener<GlobalResponse>() {

					@Override
					public void onResponse(GlobalResponse response) {
						sendCommend("加载中", CMD_UPDATE_CURRENT_PROGRESS_CANCEL);
						if (response != null) {
						    	if (response.isSuccess()) {
								showToast("充值成功");
		            			// 充值成功后取消定时任务
		            			if(task != null && !task.isCancelled()){
		            				task.cancel(true);
		            				task = null;
		            			}
								finish();
							} else {
								if (response.isError()) {
									if (response.getError().get(0).getMessage().equals("server_error")) {
										showToast("网络超时");
									} else {
										showToast(response.getError().get(0).getMessage());
									}
								} else {
									showToast("充值失败，请重试");
								}
							}

						}
					}
				}, new GlobalWlanErrorListener(this) {
					@Override
					public void onErrorResponse(VolleyError error) {
						super.onErrorResponse(error);
						setWlanFalseMessage("充值失败，请重试");
					}
				}, false);
	}

	private void drawMoney(String money, String code) {
		String userId = preferenceStorageService.getUserId();
		DrawMoneyRequest request = new DrawMoneyRequest(userId, money, code);
		volleyHttpClient.postOAuthWithParams(request.getUrl(Method.POST),
				GlobalResponse.class, request.getParams(),
				new Response.Listener<GlobalResponse>() {

					@Override
					public void onResponse(GlobalResponse response) {
						if (response != null) {
							if (response.isSuccess()) {
								sendCommend("加载中", CMD_UPDATE_CURRENT_PROGRESS_CANCEL);
								showToast("提现申请成功!提现金额将在1至2个工作日内到账");
		            			// 提现成功后取消定时任务
		            			if(task != null && !task.isCancelled()){
		            				task.cancel(true);
		            				task = null;
		            			}
								finish();
							} else {
								sendCommend("加载中", CMD_UPDATE_CURRENT_PROGRESS_CANCEL);
								if (response.isError()) {
									showToast(response.getError().get(0).getMessage());
								} else {
									showToast("提现失败，请重试");
								}
							}
						}
					}
				}, new GlobalWlanErrorListener(this) {
					@Override
					public void onErrorResponse(VolleyError error) {
						super.onErrorResponse(error);
						setWlanFalseMessage("提现失败，请重试");
					}
				}, false);

	}

	private void getCodeNetwork() {
		sendCommend("发送中", CMD_UPDATE_CURRENT_PROGRESS_SHOW);
		SmsCaptchaRequest1 request = null;
		String userId = preferenceStorageService.getUserId();
		int id = rg.getCheckedRadioButtonId();
		switch (id) {
		case R.id.rb_recharge_isrecharge:
			request = new SmsCaptchaRequest1(userId, "CONFIRM_CREDITMARKET_DEPOSIT");
			break;
		case R.id.rb_recharge_isdrawmoney:
			request = new SmsCaptchaRequest1(userId, "CONFIRM_CREDITMARKET_WITHDRAW");
			break;
		}

		volleyHttpClient.postOAuthWithParams(request.getUrl(Method.POST), ApiResponse.class, request.getParams(),
				new Response.Listener<ApiResponse>() {
					@Override
					public void onResponse(ApiResponse response) {
						sendCommend("发送中", CMD_UPDATE_CURRENT_PROGRESS_CANCEL);
						if (response.isSuccess()) {
							task = new CountDownTask();
							task.execute();
						} else {
							sendCommend(response.getErrorMessage(), CMD_SHOW_TOAST);
						}
					}
				}, new GlobalWlanErrorListener(this) {
					@Override
					public void onErrorResponse(VolleyError error) {
						super.onErrorResponse(error);
						setWlanFalseMessage("获取验证码失败，请重试");
					}
				}, false);
	}

	public class CountDownTask extends AsyncTask<Void, Integer, Void> {

		@Override
		protected void onPreExecute() {
			btn_code.setEnabled(false);
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {
			btn_code.setEnabled(true);
			btn_code.setText(RechargeActivity.this.getString(R.string.regesiter_getcode));
			Log.e("TASk", "postexecute");
			super.onPostExecute(result);
		}

		@Override
		protected Void doInBackground(Void... params) {
			Log.e("TASk", "doinbackground");
			for (int i = 60; i >= 0; i--) {
            	if (task.isCancelled()) {
            		return null;
            	}
				try {
					Thread.sleep(1000);
					publishProgress(i);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			Log.e("TASk", "onprogressupdate");
			btn_code.setText("重新发送(" + values[0] + ")");
			super.onProgressUpdate(values);
		}
	}

	class SmsContent extends ContentObserver {

		private Cursor cursor = null;

		public SmsContent(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {

			super.onChange(selfChange);
			// 读取收件箱中指定号码的短信
			cursor = managedQuery(Uri.parse("content://sms/inbox"),
					new String[] { "_id", "address", "read", "body" },
					" read=?", new String[] { "0" }, "_id desc");// 按id排序，如果按date排序的话，修改手机时间后，读取的短信就不准了
			if (cursor != null && cursor.getCount() > 0) {
				ContentValues values = new ContentValues();
				values.put("read", "1"); // 修改短信为已读模式
				cursor.moveToNext();
				int smsbodyColumn = cursor.getColumnIndex("body");
				String smsBody = cursor.getString(smsbodyColumn);
				Log.v("SMS", "smsBody = " + smsBody);
				if (TextUtils.isEmpty(smsCode.getText().toString())) {
					smsCode.setText(MatchUtils.getDynamicPassword(smsBody));
				}
			}

			// 在用managedQuery的时候，不能主动调用close()方法， 否则在Android 4.0+的系统上， 会发生崩溃
			if (Build.VERSION.SDK_INT < 14) {
				cursor.close();
			}
		}
	}

}
