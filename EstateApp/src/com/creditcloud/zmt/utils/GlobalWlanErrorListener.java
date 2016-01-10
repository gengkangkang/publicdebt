package com.creditcloud.zmt.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View.OnFocusChangeListener;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.creditcloud.model.ApiHttp400Error;
import com.creditcloud.model.ApiHttpError;
import com.creditcloud.utils.NetWorkUtils;
import com.creditcloud.zmt.receiver.RetryLoginReceiver;
import com.estate.BaseActivity;
import com.estate.R;


public class GlobalWlanErrorListener implements Response.ErrorListener {

    private BaseActivity activity;
    
    private String wlanFalseMessage;
    
    private OnFocusChangeListener listener;
    public GlobalWlanErrorListener(Context context) {
        this.activity = (BaseActivity) context;
    }
    public GlobalWlanErrorListener(OnFocusChangeListener listener){
    	this.listener = listener;
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        try {
            activity.sendCommend("加载中", activity.CMD_UPDATE_CURRENT_PROGRESS_CANCEL);
        } catch (Exception e) {
            return;
        }
        try {
            if (!NetWorkUtils.detect(activity)) {
                //无网情况提示
                activity.sendCommend(activity.getString(R.string.nonet), activity.CMD_SHOW_TOAST_SHORT);
            } else {
                //返回数据为空提示
                if (error == null || error.networkResponse == null) {
//                    activity.sendCommend(activity.getString(R.string.netlong), activity.CMD_SHOW_TOAST);
                    activity.sendCommend("登录过期，请重新登录", activity.CMD_SHOW_TOAST);
                } else {
                    if (error.networkResponse.statusCode == 400) {
                        ApiHttp400Error apiError = (ApiHttp400Error) activity.apiHttpErrorjsonToGson(error.networkResponse);
                        if (apiError != null && !TextUtils.isEmpty(apiError.getError()) && apiError.getError_description().getResult().equals("FAILED")) {
                            activity.sendCommend("用户名或密码错误", activity.CMD_SHOW_TOAST);
                        } else {
                            activity.sendCommend("用户名或手机号错误", activity.CMD_SHOW_TOAST);
                        }
                    } else {
                        ApiHttpError apiError = (ApiHttpError) activity.apiHttpErrorjsonToGson(error.networkResponse);
                        if (apiError != null && !TextUtils.isEmpty(apiError.getError())) {

                            if ("access_denied".equals(apiError.getError().toLowerCase())) {
                                Intent intent = new Intent(RetryLoginReceiver.RETRYLOGIN_RECEIVER_ACTION);
                                activity.sendBroadcast(intent);
                                activity.sendCommend("登录过期，请重新登录", activity.CMD_SHOW_TOAST);
                            } else if ("server_error".equals(apiError.getError().toLowerCase())) {
                                activity.sendCommend("服务器错误", activity.CMD_SHOW_TOAST);

                            } else if ("invalid_request".equals(apiError.getError().toLowerCase())) {
                                activity.sendCommend("请求无效", activity.CMD_SHOW_TOAST);

                            } else {
                                activity.sendCommend(apiError.getError(), activity.CMD_SHOW_TOAST);
                            }

                        } else {
                            if (!TextUtils.isEmpty(wlanFalseMessage)) {
                                activity.sendCommend(wlanFalseMessage, activity.CMD_SHOW_TOAST);
                            }
                        }
                    }
                }

            }
        } catch (Exception e) {
            activity.sendCommend("网络错误", activity.CMD_SHOW_TOAST_SHORT);
        }

        activity.sendCommend(activity.WLAN_FALSE, activity.CMD_UPDATE);

        Log.e("NetWorkError", "" + error);
    }

    public String getWlanFalseMessage() {
        return wlanFalseMessage;
    }

    public void setWlanFalseMessage(String wlanFalseMessage) {
        this.wlanFalseMessage = wlanFalseMessage;
    }
}
