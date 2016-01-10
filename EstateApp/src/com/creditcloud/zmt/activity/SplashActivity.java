package com.creditcloud.zmt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.creditcloud.api.ApiConstants;
import com.estate.BaseActivity;
import com.estate.R;

/**
 * @author dell
 * @mark 鍚姩椤甸潰
 */
public class SplashActivity extends BaseActivity {

	private View view;
	private Animation animation;
	private boolean bankNameOver = false, cityOver = false, time = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = View.inflate(this, R.layout.activity_splash, null);
		setContentView(view);

		getAllInfomation();
		into();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.gc();
	}

	/**
	 * @mark 鑾峰彇鍒濆淇℃伅
	 */
	private void getAllInfomation() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					time = true;
					startAc();
				} catch (InterruptedException e) {
					e.printStackTrace();
					time = true;
					startAc();
				}
			}
		}).start();
	}

	private void startAc(){
		if(bankNameOver && cityOver && time){
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}

	public void into() {
		animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
		view.startAnimation(animation);
	}
}
