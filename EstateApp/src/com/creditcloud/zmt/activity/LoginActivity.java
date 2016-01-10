package com.creditcloud.zmt.activity;

import java.io.UnsupportedEncodingException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.creditcloud.event.request.LoginRequest;
import com.creditcloud.zmt.event.response.LoginResponse;
import com.creditcloud.zmt.utils.GlobalWlanErrorListener;
import com.estate.BaseActivity;
import com.estate.R;
import com.umeng.analytics.MobclickAgent;

import cache.DataCache;

/**
 * @author yangkx
 * @mark 登录功能
 */
public class LoginActivity extends BaseActivity {

    private String name;           // 承载名字
    private String pwd;            // 承载密码
    private ImageView back;        // 返回按钮
    private EditText userName;     // 登录用户名
    private EditText password;     // 登录密码
    private TextView forgotPwd;    // 忘记密码“链接”
    
    // 记录的电话号码
    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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

    /**
     * @mark 获取界面控件的指向
     */
    private void initView() {
        back = (ImageView) findViewById(R.id.btn_back);
        userName = (EditText) findViewById(R.id.et_login_username);
        password = (EditText) findViewById(R.id.et_login_password);
        forgotPwd = (TextView) findViewById(R.id.tv_forgetpwd);

        // 设定`返回`监听
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 设定`忘记密码`监听
        forgotPwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
        // 设定`回车`监听
        password.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (checkNull()) {
                        loginAction();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * @return true/false
     * @mark 验证空值
     */
    private boolean checkNull() {
        name = userName.getText().toString().trim();
        pwd = password.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            sendCommend(this.getString(R.string.regesiter_user_hintname),
                    CMD_SHOW_TOAST);
            return false;
        }
        if (TextUtils.isEmpty(pwd)) {
            sendCommend(this.getString(R.string.regesiter_user_hintpwd),
                    CMD_SHOW_TOAST);
            return false;
        }
        return true;
    }

    /**
     * @param view
     * @mark 响应界面`注册`事件
     */
    public void gotoRegesiter(View view) {
        Intent intent = new Intent(LoginActivity.this, RegesiterActivity.class);
        startActivity(intent);
    }

    /**
     * @param view
     * @mark 响应界面`登录`事件
     */
    public void gotoLogin(View view) {
        if (checkNull()) {
            loginAction();
        }
    }

    /**
     * @mark 执行网络请求进行登录
     */
    private void loginAction() {
        sendCommend("登录中", CMD_UPDATE_CURRENT_PROGRESS_SHOW);
        LoginRequest request = new LoginRequest(name, pwd);

        for (String key : request.getParams().keySet()) {
        }

        volleyHttpClient.postWithParams(request.getUrl(Method.POST), LoginResponse.class, request.getParams(), new Response.Listener<LoginResponse>() {
            @Override
            public void onResponse(LoginResponse response) {
                if (!response.isError()) {

                    sendCommend("登录成功", CMD_SHOW_TOAST);
                    String userName = response.getUser().getName();
                    String loginName = response.getUser().getLoginName();
                    String userId = response.getUser().getId();
                    String mobile = response.getUser().getMobile();
                    String cardNum = response.getUser().getIdNumber();
                    try {
                        if (!TextUtils.isEmpty(userName)) {
                            userName = new String(response.getUser().getName().getBytes("ISO-8859-1"), "UTF-8");
                        } else {
                            userName = "";
                        }
                        if (!TextUtils.isEmpty(loginName)) {
                            loginName = new String(response.getUser().getLoginName().getBytes("ISO-8859-1"), "UTF-8");
                        } else {
                            loginName = "";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    preferenceStorageService.setLogin(true, userName, userId, mobile, loginName, cardNum);//不知道为何将groupid存成loginname
                    DataCache.getDataCache().saveToCache("access_token", response.getAccess_token());

                    finish();
                    sendCommend("登录中", CMD_UPDATE_CURRENT_PROGRESS_CANCEL);
                } else {
                    sendCommend(response.getErrorMessage(), CMD_SHOW_TOAST);
                    sendCommend("登录中", CMD_UPDATE_CURRENT_PROGRESS_CANCEL);
                }
            }
        }, new GlobalWlanErrorListener(this) {
            @Override
            public void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
                setWlanFalseMessage("登录失败，请重试");
            }
        }, false);

    }
   
   
}
