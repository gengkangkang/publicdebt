package com.creditcloud.zmt.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.estate.BaseActivity;
import com.estate.R;

public class EstateCarActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blockchain_activity_car);
		initView();
	}

	/**
	 * @mark `actionbar的返回`功能
	 * @param view
	 */
	public void back(View view) {
		finish();
	}

	private void initView() {
//		radioGroup = (RadioGroup) findViewById(R.id.rg_main);
//		radioGroup.check(R.id.rb_tab_main);
	}

	/**
	 * @mark 
	 * @param view
	 */
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_myaccount_wealthManager:
			break;
		}
	}
}
