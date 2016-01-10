package com.creditcloud.zmt.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.creditcloud.event.ApiResponse;
import com.creditcloud.event.request.UpdatePwdRequest;
import com.creditcloud.model.ErrorInfo;
import com.creditcloud.zmt.activity.ForgetPasswordActivity;
import com.creditcloud.zmt.event.response.GlobalResponse;
import com.creditcloud.zmt.utils.GlobalWlanErrorListener;
import com.estate.R;
import com.estate.event.request.SmsCaptchaRequest;

/**
 * @author yangkx
 * @mark handle message to reset the password
 */
public class ForgetPasswordSecondFragment extends BaseFragment {

    public final static String TAG = "ForgetPasswordSecondFragment";
    private EditText verifyCode, newPwd, verifyPwd;
    private Button imageButton;
    private Button btnNext;
    private String mobile, verify_code, new_passwd, verify_pwd;
    private ForgetPasswordActivity activity;
//    private CountDownTask task;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View reset_pwd_view = inflater.inflate(R.layout.fragment_updatepwd_input, null);

        activity = (ForgetPasswordActivity) getActivity();
        mobile = activity.preferenceStorageService.getResetPwdMobile();

        // initialize the view
        initView(reset_pwd_view);

        return reset_pwd_view;
    }

    /**
     * @param view
     * @mark initialize the view
     */
    public void initView(View view) {
        verifyCode = (EditText) view.findViewById(R.id.et_code);
        imageButton = (Button) view.findViewById(R.id.img_sms_code_2);
        newPwd = (EditText) view.findViewById(R.id.et_new_pwd);
        verifyPwd = (EditText) view.findViewById(R.id.et_verify_pwd);
        btnNext = (Button) view.findViewById(R.id.btn_next);

        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // click to refresh symbolCode
                handleSmsCaptcha();
            }
        });

        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // 获取控件显示的字符
                verify_code = verifyCode.getText().toString().trim();
                new_passwd  = newPwd.getText().toString().trim();
                verify_pwd  = verifyPwd.getText().toString().trim();

                // 获取空间内容
                if (TextUtils.isEmpty(verify_code)) {
                    activity.showToast("请输入短信验证码");
                    return;
                }

                // 输入密码验证
                if (TextUtils.isEmpty(new_passwd)) {
                    activity.showToast("密码为最少8位字母+数字组合，区分大小写");
                    return;
                } else if (new_passwd.length() < 8) {
                    activity.showToast("密码为最少8位字母+数字组合，区分大小写");
                    return;
                } else if (new_passwd.matches("^[A-Za-z]+$")) {
                	activity.showToast("密码为最少8位字母+数字组合，区分大小写");
                	return;
                } else if (new_passwd.matches("^[0-9]+$")) {
                	activity.showToast("密码为最少8位字母+数字组合，区分大小写");
                	return;
                }
                
                if (TextUtils.isEmpty(verify_pwd)) {
                    activity.showToast("请确保两次密码一致");
                    return;
                }
                if (!TextUtils.isEmpty(new_passwd) || !TextUtils.isEmpty(verify_pwd)) {
                    if (!new_passwd.equals(verify_pwd)) {
                        activity.showToast("请确保两次密码一致");
                        return;
                    }
                }

                // send verify code to mobile
                sendChangePwdReq();
            }
        });
    }

    /**
     * @mark handle the message
     */
    public void handleSmsCaptcha() {
        // 发送短信验证码
        SmsCaptchaRequest request = new SmsCaptchaRequest(mobile);
        activity.volleyHttpClient.get(request.getUrl(Method.GET), ApiResponse.class,
                new Response.Listener<ApiResponse>() {
                    @Override
                    public void onResponse(ApiResponse response) {
                        if (response != null) {
                            activity.sendCommend("加载中", activity.CMD_UPDATE_CURRENT_PROGRESS_CANCEL);
                            if (response.isSuccess()) {
                                // 启动新的fragment展现view
                                activity.task = new CountDownTask(activity);
                                activity.task.execute();
                            } else {
                                activity.showToast("");
                            }
                        }
                    }
                }, new GlobalWlanErrorListener(activity) {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        setWlanFalseMessage("获取验证码失败");
                    }
                },
                false);
    }

    /**
     * @author yangkx
     * @mark 异步倒计时
     */
    public class CountDownTask extends AsyncTask<Void, Integer, Void> {
        // activity变量用以承载实际对象
        private ForgetPasswordActivity activity;

        //构造方法
        public CountDownTask(ForgetPasswordActivity activity) {
            super();
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            Log.e("TASk", "preexecute");
            imageButton.setEnabled(false);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            imageButton.setEnabled(true);
            imageButton.setText("重新发送");
            Log.e("TASk", "postexecute");
            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.e("TASk", "doinbackground");
            for (int i = 60; i >= 0; i--) {
            	if (activity.task.isCancelled()) {
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
            imageButton.setText("重新发送(" + values[0] + "s)");
            super.onProgressUpdate(values);
        }

//        // 停止异步任务进程
//        protected void onProgessCancel() {
//            Log.e("TASk", "onProgessCancel");
//            if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
//                task.cancel(true);
//                task = null;
//            }
//        }
    }

    /**
     * @mark send verify code to mobile
     */
    private void sendChangePwdReq() {
        // 手机发出携带必须参数的请求去服务器修改密码
		activity.sendCommend("修改中", activity.CMD_UPDATE_CURRENT_PROGRESS_SHOW);
        UpdatePwdRequest request = new UpdatePwdRequest(mobile, verify_code, new_passwd);

        Log.d("验证图形码：", request.getUrl(Method.POST));
        activity.volleyHttpClient.postWithParams(request.getUrl(Method.POST), GlobalResponse.class,
                request.getParams(), new Response.Listener<GlobalResponse>() {
                    @Override
                    public void onResponse(GlobalResponse response) {
                        if (response != null) {
                            if (response.isSuccess()) {
//								activity.showToast("密码已经修改成功！");
                    			// 重置成功后取消定时任务
                        		if(activity.task != null && !activity.task.isCancelled()){
                        			activity.task.cancel(true);
                        			activity.task = null;
                        		}
                                activity.changeFragment2();

                            } else {
                                if (response.getError() != null && response.getError().size() > 0) {
                                    Log.e("ForgetPassword:", response.getError().get(0).getCode() + " " + response.getError().get(0).getMessage() + " " + response.getError().get(0).getType());
                                    activity.showToast(response.getError().get(0).getMessage());
                                }
                            }
                        } else {
                            activity.showToast("请求失败，请重试");
                        }
                        activity.sendCommend("修改中", activity.CMD_UPDATE_CURRENT_PROGRESS_CANCEL);
                    }
                }, new GlobalWlanErrorListener(activity) {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        setWlanFalseMessage("请求失败，请重试");
                    }
                }, false);
    }
}