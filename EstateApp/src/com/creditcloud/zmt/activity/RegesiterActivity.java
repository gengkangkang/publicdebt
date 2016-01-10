package com.creditcloud.zmt.activity;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.android.core.utils.Text.MatchUtils;
import com.android.core.view.TextImageButton;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.creditcloud.event.ApiResponse;
import com.creditcloud.event.request.ImageCodeRequest;
import com.creditcloud.event.request.RegisterRequest;
import com.creditcloud.event.request.WealthManagerValidRequest;
import com.creditcloud.event.response.ImageCodeResponse;
import com.creditcloud.model.ApiData;
import com.creditcloud.model.ApiError;
import com.creditcloud.zmt.event.request.RegisterSmsCaptchaReq;
import com.creditcloud.zmt.event.response.RegisterResponse;
import com.creditcloud.zmt.utils.GlobalWlanErrorListener;
import com.estate.BaseActivity;
import com.estate.R;
import com.estate.event.response.WealthManagerValidResponse;
import com.estate.utils.pdf.MuPDFActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * @author yangkx
 * @mark 注册用户
 */
public class RegesiterActivity extends BaseActivity implements OnClickListener {
    private EditText userName;
    private EditText pwd;
    private EditText commitPwd;
    private EditText mobileNo;
    private EditText code;
    private Button btn_code;
    private CheckBox agree;
    private TextView tv_agree;
    private Button btn_regesiter;
    private String name, password, repeatPassword, wealth, imageNumber, number, validateCode;
    private SmsContent content;
    private String token;
    private TextImageButton codeImage;
    private EditText symbolNo;
    private CountDownTask task;
    private float pixDensity;
	private EditText wealthNum;
	private Button wealthNam;
	private String inchannel = "01";  // 标识android终端

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        content = new SmsContent(new Handler());
        // 注册短信变化监听
        this.getContentResolver().registerContentObserver(
                Uri.parse("content://sms/"), true, content);
        setContentView(R.layout.activity_regesiter);
        initView();
        getInfomation();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_getcode:
                getCode();
                break;
            case R.id.btn_goregesiter:
                regesiter();
                break;
            case R.id.tv_agree:
                MuPDFActivity.startAc(RegesiterActivity.this, MuPDFActivity.REGESITER, "");
                break;
        }
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

    /**
     * @mark 获取图形验证码
     */
    public void getInfomation() {
        ImageCodeRequest request = new ImageCodeRequest(System.currentTimeMillis());
        volleyHttpClient.get(request.getUrl(Method.GET),
            ImageCodeResponse.class,
            new Response.Listener<ImageCodeResponse>() {
                @Override
                public void onResponse(ImageCodeResponse response) {
                    if (response != null && !TextUtils.isEmpty(response.getCaptcha())) {
                         
                        byte[] bitmapArray;
                        bitmapArray = Base64.decode(response.getCaptcha()
                                            .substring(response.getCaptcha().indexOf(",") + 1), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
                        // 根据手机分辨率动态的修改图形验证码的显示
                		WindowManager wm = (WindowManager) (RegesiterActivity.this.getSystemService(Context.WINDOW_SERVICE));
                		DisplayMetrics dm = new DisplayMetrics();
                		wm.getDefaultDisplay().getMetrics(dm);
                		pixDensity = dm.density;
                		
                		// 分辨率是720p或者1080p的调整图形验证码的大小
                		if (pixDensity > 300) { 
                			bitmap = bitmap.createScaledBitmap(bitmap, 230, 80, true);
                		} else {
            				bitmap = bitmap.createScaledBitmap(bitmap, 230, 80, true); 
            			}
                        
                        codeImage.setImageBitmap(bitmap);
                        codeImage.setText("");
                        token = response.getToken();
                    } else {
                        showToast("获取验证码失败，请点击重试");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showToast("获取验证码失败，请点击重试");
                    codeImage.setText("获取验证码");
                    codeImage.setImageBitmap(null);
                }
            },
        false);
    }

    /**
     * @mark 初始化控件
     */
    private void initView() {
        userName  = (EditText) findViewById(R.id.et_regesiter_username);
        pwd       = (EditText) findViewById(R.id.et_regesiter_pwd);
        commitPwd = (EditText) findViewById(R.id.et_regesiter_commitpwd);
        mobileNo  = (EditText) findViewById(R.id.et_regesiter_mobile);
        wealthNum = (EditText) findViewById(R.id.et_regesiter_wealth);
        // 图形码内容
        symbolNo  = (EditText) findViewById(R.id.et_imagecode);
        
        // 图形码按钮
        codeImage = (TextImageButton) findViewById(R.id.img_code);
        codeImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                getInfomation();
            }
        });
        
        // 添加财富经理输入框失去光标焦点时事件
        wealthNum.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(wealthNum.hasFocus() == false){
					// 判断输入是否为空
					final String wealthManangerId = wealthNum.getText().toString().trim();
					if(wealthManangerId == null || "".equals(wealthManangerId)) return;
					
					WealthManagerValidRequest request = new WealthManagerValidRequest("empReferral",wealthManangerId);
					volleyHttpClient.postWithParams(request.getUrl(Method.POST),
						WealthManagerValidResponse.class, request.getParams(),
			            new Response.Listener<WealthManagerValidResponse>() {
			                @Override
			                public void onResponse(WealthManagerValidResponse response) {
			                    if (response != null) {
									sendCommend("获取财富经理中",CMD_UPDATE_CURRENT_PROGRESS_CANCEL);
			                    	if (response.isSuccess()) {
			                    		if(task != null && !task.isCancelled()){
			                    			task.cancel(true);
			                    			task = null;
			                    		}
			                    		// 成功时界面提示财富经理姓名
			                   			ApiData data = response.getData();
			                   			sendCommend("财富经理：" + data.getName(), CMD_SHOW_TOAST);
			                            return;
			                    	} else {
			                    		// 失败时提示财富经理不存在信息
			                          	if (response.getError() != null && response.getError().size() > 0) {
			                        		List<ApiError> errorList = response.getError();
			                        		ApiError errorInfo = errorList.get(0);
			                        		sendCommend(errorInfo.getMessage(), CMD_SHOW_TOAST);
			                        		showToast(errorInfo.getMessage());
			                        		wealthNum.requestFocus();
			                        		wealthNum.setSelection(wealthManangerId.length());
			                        		return;
			                          	}
			                    	}
			                    } else {
									sendCommend("注册中",CMD_UPDATE_CURRENT_PROGRESS_CANCEL);
			                   		showToast("请求失败，请重试");
			                   	}
			                }
			            }, new GlobalWlanErrorListener(this) {
			                @Override
			                public void onErrorResponse(VolleyError error) {
			                    super.onErrorResponse(error);
			                    setWlanFalseMessage("校验财富经理失败");
			                }
			            },
			        false);
				}
			}
		});
        
        // 短信码内容（图形码和短信码输入框名混名）
        code = (EditText) findViewById(R.id.et_code);
        code.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView arg0, int actionId, KeyEvent arg2) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (checkNull()) { regesiter(); }
                    return true;
                }
                return false;
            }
        });

        // 短信码按钮
        btn_code = (Button) findViewById(R.id.btn_getcode);
        btn_code.setOnClickListener(this);

        // 服务协议
        agree = (CheckBox) findViewById(R.id.cb_agree);
        tv_agree = (TextView) findViewById(R.id.tv_agree);
        tv_agree.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_agree.setOnClickListener(this);

        // 注册按钮
        btn_regesiter = (Button) findViewById(R.id.btn_goregesiter);
        btn_regesiter.setOnClickListener(this);
    }

    /**
     * @mark 获取短信码
     */
    private void getCode() {
        number      = mobileNo.getText().toString().trim();
        imageNumber = symbolNo.getText().toString().trim();
        
        if (TextUtils.isEmpty(number)) {
            sendCommend("请输入手机号码", CMD_SHOW_TOAST);
            return;
        }
        
        if (TextUtils.isEmpty(imageNumber)) {
            sendCommend("请输入图形验证码", CMD_SHOW_TOAST);
            return;
        }

        if (isMobileNO(number)) {
            getCodeNetwork(number);
        } else {
            sendCommend("请输入正确的手机号码", CMD_SHOW_TOAST);
        }
    }
    
   

    /**
     * @mark 执行注册
     */
    private void regesiter() {
        if (checkNull()) {
            if (agree.isChecked()) {
                if (password.equals(repeatPassword)) {
					regesiterNetwork();
                } else {
                    sendCommend("两次密码输入不一致", CMD_SHOW_TOAST);
                }
            } else {
                sendCommend("请勾选‘我已经阅读并同意《服务协议》’", CMD_SHOW_TOAST);
            }
        }
    }

    /**
     * @mark 校验是否为空
     */
    private boolean checkNull() {
        name = userName.getText().toString().trim();
        password = pwd.getText().toString().trim();
        repeatPassword = commitPwd.getText().toString().trim();
        number = mobileNo.getText().toString().trim();
        wealth = wealthNum.getText().toString().trim();
        validateCode = code.getText().toString().trim();
        imageNumber = symbolNo.getText().toString().trim();
        

        // 请输入至少四位数字或字母或下划线的密码,不能是单独的下划线,且不能为手机号和邮箱号
        if (TextUtils.isEmpty(name)) {
            sendCommend("用户名不能为空", CMD_SHOW_TOAST);
            return false;
        } else {
            if (name.length() < 4) {
                sendCommend("用户名最少4个字符", CMD_SHOW_TOAST);
                return false;
            }
            if (isMobileNO(name)) {
                sendCommend("用户名不能为手机号和邮箱号", CMD_SHOW_TOAST);
                return false;
            }
            if (isEmail(name)) {
                sendCommend("用户名不能为手机号和邮箱号", CMD_SHOW_TOAST);
                return false;
            }

        	Pattern p = Pattern.compile("(?:^[a-zA-Z]+[\\d_]*|^\\d+[a-zA-Z_]*|^_+[a-zA-Z0-9]+)\\w*$");
        	Matcher m = p.matcher(name);
        	if (!m.matches()) {
                sendCommend("用户名只能包含数字或英文字母以及下划线,不能是单独的下划线", CMD_SHOW_TOAST);
                return false;
        	}
        }
        
        // 输入密码验证
        if (TextUtils.isEmpty(password)) {
            showToast("密码为最少8位字母+数字组合，区分大小写");
            return false;
        } else if (password.length() < 8) {
            showToast("密码为最少8位字母+数字组合，区分大小写");
            return false;
        } else if (password.matches("^[A-Za-z]+$")) {
        	showToast("密码为最少8位字母+数字组合，区分大小写");
        	return false;
        } else if (password.matches("^[0-9]+$")) {
        	showToast("密码为最少8位字母+数字组合，区分大小写");
        	return false;
        }
        
        if (TextUtils.isEmpty(number)) {
            sendCommend("请输入有效的手机号码", CMD_SHOW_TOAST);
            return false;
        }
        
        if (TextUtils.isEmpty(imageNumber)) {
            sendCommend("请输入图形验证码", CMD_SHOW_TOAST);
            return false;
        }

        if (TextUtils.isEmpty(validateCode)) {
            sendCommend("请输入短信验证码", CMD_SHOW_TOAST);
            return false;
        }

        return true;
    }
    
    /**
     * @mark 获取短信码
     * @param num
     */
    private void getCodeNetwork(String num) {
		sendCommend("发送中", CMD_UPDATE_CURRENT_PROGRESS_SHOW);
        RegisterSmsCaptchaReq request = new RegisterSmsCaptchaReq(imageNumber, number, token);  // 图形码、电话号码、token
        volleyHttpClient.postWithParams(request.getUrl(Method.POST), ApiResponse.class, request.getParams(), new Response.Listener<ApiResponse>() {
            @Override
            public void onResponse(ApiResponse response) {
            	if (response != null) {
					sendCommend("发送中", CMD_UPDATE_CURRENT_PROGRESS_CANCEL);
            		if (response.isSuccess()) {
            			task = new CountDownTask();
            			task.execute();
            		} else {
                  	    if (response.getError() != null && response.getError().size() > 0) {
                		    Log.e("注册：", response.getError().get(0).getCode() + " " + response.getError().get(0).getMessage() + " " + response.getError().get(0).getType());
                		    
                		    if (response.getError().get(0).getMessage().equals("INVALID_CAPTCHA")) {
                		    	showToast("图形验证码无效");
        					} else {
                    		    showToast(response.getError().get(0).getMessage());
        					}
                	    }
            		}
            	} else {
					sendCommend("发送中", CMD_UPDATE_CURRENT_PROGRESS_CANCEL);
            		showToast("请求失败，请重试");
            	}
            }}, new GlobalWlanErrorListener(this) {
                @Override
                public void onErrorResponse(VolleyError error) {
                	super.onErrorResponse(error);
                	setWlanFalseMessage("获取验证码失败");
                }
        }, false);        
    }

    public class CountDownTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            Log.e("TASk", "preexecute");
            btn_code.setEnabled(false);
            super.onPreExecute();
        }    
		    
		@Override
        protected Void doInBackground(Void... params) {
            Log.e("TASk", "doinbackground");
            for (int i = 60; i >= 0; i--) {
            	if (task.isCancelled()) { return null; }
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
            btn_code.setText("重新发送(" + values[0] + ")");
            super.onProgressUpdate(values);
        }
        
        @Override
        protected void onPostExecute(Void result) {
            btn_code.setEnabled(true);
            btn_code.setText(RegesiterActivity.this.getString(R.string.regesiter_getcode));
            super.onPostExecute(result);
        }
    }

    /**
     * @mark 执行注册的方法
     */
    private void regesiterNetwork() {
		sendCommend("注册中", CMD_UPDATE_CURRENT_PROGRESS_SHOW);
        RegisterRequest request = new RegisterRequest(number, name, password, wealth, validateCode, inchannel);

        volleyHttpClient.postWithParams(request.getUrl(Method.POST),
            RegisterResponse.class, request.getParams(),
            new Response.Listener<RegisterResponse>() {
                @Override
                public void onResponse(RegisterResponse response) {
                    if (response != null) {
						sendCommend("注册中",CMD_UPDATE_CURRENT_PROGRESS_CANCEL);
                    	if (response.isSuccess()) {
                   			sendCommend("注册成功", CMD_SHOW_TOAST);
                    		// 注册成功后取消定时任务
                    		if(task != null && !task.isCancelled()){
                    			task.cancel(true);
                   				task = null;
                   			}
                    		startActivity(new Intent(RegesiterActivity.this, LoginActivity.class));
                    		finish();
                    	} else {
                          	if (response.getError() != null && response.getError().size() > 0) {
                        		Log.e("ForgetPassword:", response.getError().get(0).getCode() + " " + response.getError().get(0).getMessage() + " " + response.getError().get(0).getType());
                        		  
                        		if (response.getError().get(0).getMessage().equals("MOBILE_EXISTS")) {
                        			showToast("该手机号码已经注册");
                        		} else if (response.getError().get(0).getMessage().equals("MOBILE_CAPTCHA_INVALID")) {
                        			showToast("短信验证码无效");
                        		} else if (response.getError().get(0).getMessage().equals("LOGINNAME_EXISTS")) {
                        			showToast("该用户名已经注册");
                        		} else if (response.getError().get(0).getMessage().equals("LOGINNAME_INVALID")) {
                        			showToast("该用户名不合法");
                        		} else if (response.getError().get(0).getMessage().equals("MEALTH_ERROR")) {
                        			showToast("检查财富经理号出错");
                        		} else  {
                        			showToast(response.getError().get(0).getMessage());
                        		}
                        	} 
                          	// 清空图形码、短信码 
                          	symbolNo.setText("");
                          	code.setText("");
                          	// 执行注册失败自动更新图形码
                          	getInfomation();
                    	}
                    } else {
						sendCommend("注册中",CMD_UPDATE_CURRENT_PROGRESS_CANCEL);
                    	showToast("请求失败，请重试");
                    }
                }
            }, new GlobalWlanErrorListener(this) {
                @Override
                public void onErrorResponse(VolleyError error) {
                    super.onErrorResponse(error);
                    setWlanFalseMessage("注册失败");
                }
            },
        false);
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
                    new String[]{"_id", "address", "read", "body"},
                    " read=?", new String[]{"0"}, "_id desc");// 按id排序，如果按date排序的话，修改手机时间后，读取的短信就不准了
            if (cursor != null && cursor.getCount() > 0) {
                ContentValues values = new ContentValues();
                values.put("read", "1"); // 修改短信为已读模式
                cursor.moveToNext();
                int smsbodyColumn = cursor.getColumnIndex("body");
                String smsBody = cursor.getString(smsbodyColumn);
                Log.v("SMS", "smsBody = " + smsBody);

                if (TextUtils.isEmpty(code.getText().toString().trim())) {
                    code.setText(MatchUtils.getDynamicPassword(smsBody));
                }
            }

            // 在用managedQuery的时候，不能主动调用close()方法， 否则在Android 4.0+的系统上， 会发生崩溃
            if (Build.VERSION.SDK_INT < 14) { cursor.close(); }
        }
    }

    /**
     * @param phone
     * @mark 根据移动联通电信号码段判断电话是否合法
     * @return
     */
    public boolean isMobileNO(String mobiles){  
        boolean flag = false;
        try{
        	Pattern p = Pattern.compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(17[0,6-8])|(18[0-9]))\\d{8}$");
        	Matcher m = p.matcher(mobiles);
        	flag = m.matches();
        }catch(Exception e){
        	flag = false;
        }
        return flag;
    }

    /**
     * @param email
     * @mark 判断邮箱是否合法
     * @return
     */
    public boolean isEmail(String email){  
      if (null==email || "".equals(email)) return false;	
      //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配  
      Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配  
      Matcher m = p.matcher(email);  
      return m.matches();  
    }
}

