package com.creditcloud.zmt.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.creditcloud.zmt.activity.LoginActivity;
import com.estate.BaseActivity;

public class RetryLoginReceiver extends BroadcastReceiver {

    public static String RETRYLOGIN_RECEIVER_ACTION = "android.intent.action.RETRYLOGIN_RECEIVER_ACTION";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(RETRYLOGIN_RECEIVER_ACTION)) {
            Intent login_intent = new Intent(context, LoginActivity.class);
            login_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(login_intent);
            BaseActivity.preferenceStorageService.setLogin(false, "", "", "", "", "");
        }
    }

}
