package com.creditcloud.zmt.receiver;

import com.android.core.utils.Toast.ToastUtil;
import com.estate.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class NetWorkReceiver extends BroadcastReceiver {

	private ConnectivityManager connectivityManager;

	private NetworkInfo info;

	public static Toast mToastUtil;

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			Log.d("mark", "网络状态已经改变");
			connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			info = connectivityManager.getActiveNetworkInfo();
			if (info != null && info.isAvailable()) {
				String name = info.getTypeName();
				if(name.equalsIgnoreCase("wifi")){
					Toast.makeText(context,"当前网络切换到Wifi", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(context,"当前网络切换到2G/3G/4G", Toast.LENGTH_LONG).show();
				}
				Log.d("mark", "当前网络名称：" + name);
			} else {
				Toast.makeText(context,context.getString(R.string.nonet), Toast.LENGTH_LONG).show();
				Log.d("mark", "没有可用网络");
			}
		}
	}

}
