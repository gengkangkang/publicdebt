package com.creditcloud.zmt.activity;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.creditcloud.zmt.fragment.ForgetPasswordFirstFragment;
import com.creditcloud.zmt.fragment.ForgetPasswordSecondFragment;
import com.creditcloud.zmt.fragment.ForgetPasswordSecondFragment.CountDownTask;
import com.estate.BaseActivity;
import com.estate.R;
import com.estate.fragment.UpdatePwdHintFragment;
import com.umeng.analytics.MobclickAgent;

/**
 * @author yangkx
 * @mark reset password
 */
public class ForgetPasswordActivity extends BaseActivity{
	
	private ForgetPasswordFirstFragment fragment1;
	private ForgetPasswordSecondFragment fragment2;
	private UpdatePwdHintFragment fragment3;
	private FragmentManager fragmentManager;
	public CountDownTask task;
	
	@Override
	@TargetApi(14)
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_updatepwd);
		fragment1 = new ForgetPasswordFirstFragment();
		fragment2 = new ForgetPasswordSecondFragment();
		fragment3 = new UpdatePwdHintFragment();
		
		// initialize
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
	private void initView() {
		fragmentManager = this.getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(R.id.fg_updatepwd, fragment1, ForgetPasswordFirstFragment.TAG);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	/**
	 * @mark 同一个actitvty内更改fragment
	 */
	public void changeFragment(){
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.fg_updatepwd, fragment2, ForgetPasswordSecondFragment.TAG);
		transaction.commit();
	}
	
	public void changeFragment2() {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.fg_updatepwd, fragment3, UpdatePwdHintFragment.TAG);
		transaction.commit();
	}
		
	@Override
	public void update(Object id) {
		super.update(id);
		if(id instanceof Integer){
			int code = (Integer)id;
			switch(code){
			case WLAN_FALSE:
				fragment1.getSymbolCode();
				break;
			}
		}
	}
}
