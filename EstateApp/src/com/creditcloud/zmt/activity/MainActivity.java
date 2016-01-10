package com.creditcloud.zmt.activity;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.creditcloud.zmt.fragment.MainFragment;
import com.estate.BaseActivity;
import com.estate.R;
import com.estate.fragment.MoreFragment;
import com.estate.fragment.MyFragment;
import com.estate.fragment.TouziFragment;
import com.umeng.analytics.MobclickAgent;

import java.util.Timer;
import java.util.TimerTask;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends BaseActivity implements OnRefreshListener {

	private FragmentManager fragmentManager;
	// 下方选择组件
	public static RadioGroup radioGroup;

	/**
	 * 首页Fragment
	 */
	public MainFragment mainFragment;
	
	/**
	 * 投资Fragment
	 */
	public TouziFragment touziFragment;

	/**
	 * 更多Fragment
	 */
	public MoreFragment moreFragment;

	private int checkId = 0;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		fragmentManager = getFragmentManager();
		initListener();
		setInstanceByIndex(R.id.rb_tab_main);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		 MobclickAgent.onPageStart("MainActivity"); 
	}

	/**
	 * init listener
	 */
	private void initListener() {
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				Log.d("CheckChange", "" + checkedId);
				setInstanceByIndex(checkedId);
			}
		});
	}

	/**
	 * init view
	 */
	private void initView() {
		radioGroup = (RadioGroup) findViewById(R.id.rg_main);
		radioGroup.check(R.id.rb_tab_main);
	}

	/**
	 * 将所有的Fragment都置为隐藏状态
	 * 
	 * @param transaction
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (mainFragment != null) {
			Log.d("BaseFragment", "mainFragment.hide()");
			transaction.hide(mainFragment);
		}
		if (touziFragment != null) {
			transaction.hide(touziFragment);
			Log.d("BaseFragment", "touziFragment.hide()");
		}
		if (moreFragment != null) {
			transaction.hide(moreFragment);
			Log.d("BaseFragment", "moreFragment.hide()");
		}
	}

	public void setInstanceByIndex(int index) {

		FragmentTransaction transaction = fragmentManager.beginTransaction();
		hideFragments(transaction);
		checkId = index;
		switch (index) {
		case R.id.rb_tab_touzi:

			if (touziFragment == null) {
				touziFragment = new TouziFragment();
				transaction.add(R.id.main_tab_fragment, touziFragment, "touzi");
			} else {
				transaction.show(touziFragment);
			}
			break;
		case R.id.rb_tab_my:
			break;
		case R.id.rb_tab_more:
			if (moreFragment == null) {
				moreFragment = new MoreFragment();
				transaction.add(R.id.main_tab_fragment, moreFragment, "more");
			} else {
				transaction.show(moreFragment);
			}
			break;
		default:
			if (mainFragment == null) {
				mainFragment = new MainFragment();
				transaction.add(R.id.main_tab_fragment, mainFragment, "main");
			} else {
				transaction.show(mainFragment);
			}
			break;
		}

		transaction.commit();
	}

	@Override
	public void onRefresh() {
		switch (checkId) {
		case R.id.rb_tab_touzi:

		case R.id.rb_tab_my:

			break;
		case R.id.rb_tab_more:

			break;
		default:

			break;

		}
	}

	@Override
	public void update(Object id) {
		if (id instanceof Integer) {
			int code = (Integer) id;
			switch (code) {
			case WLAN_FALSE:
				if (fragmentMyListener != null) {
					fragmentMyListener.refreshFalse();
				}
				break;
			}
		}
		// if (fragmentTouziListener != null) {
		// fragmentTouziListener.refresh();
		// }

		super.update(id);
	}

	public interface FragmentTouziListener {
		public void refreshClose();
	}

	public FragmentTouziListener fragmentTouziListener;

	public FragmentTouziListener getFragmentTouziListener() {
		return fragmentTouziListener;
	}

	public void setFragmentTouziListener(
			FragmentTouziListener fragmentTouziListener) {
		this.fragmentTouziListener = fragmentTouziListener;
	}

	public interface FragmentMyListener {
		public void refreshFalse();
	}

	public FragmentMyListener fragmentMyListener;

	public FragmentMyListener getFragmentMyListener() {
		return fragmentMyListener;
	}

	public void setFragmentMyListener(FragmentMyListener fragmentMyListener) {
		this.fragmentMyListener = fragmentMyListener;
	}

	private boolean isExit;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!isExit) {
				if (checkId == R.id.rb_tab_main) {
					isExit = true;
					Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_LONG).show();
					Timer tExit = new Timer();
					tExit.schedule(new TimerTask() {
						@Override
						public void run() {
							isExit = false; // 取消退出
						}
					}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
					return true;
				} else {
					if (moreFragment != null && moreFragment.isVisible()) {
					} else {
						radioGroup.check(R.id.rb_tab_main);
					}
					return true;
				}
			} else {
				System.exit(0);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 注销用户
	 * 
	 * @param view
	 */
	public void btnLogout(View view) {
		if(view != null){
			showLogOutDialog(this);
			return;
		}
		preferenceStorageService.setLogin(false, "", "", "", "", "");
		mainFragment.setLoginButton();
		if (moreFragment != null) {
		}
		sendCommend("退出成功", CMD_SHOW_TOAST);
	}

}
