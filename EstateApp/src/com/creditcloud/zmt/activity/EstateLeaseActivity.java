package com.creditcloud.zmt.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.creditcloud.zmt.BaseActivity;
import com.creditcloud.zmt.R;
import com.creditcloud.zmt.event.request.CheckBindCardRequest;
import com.creditcloud.zmt.event.response.GlobalResponse;
import com.creditcloud.zmt.utils.GlobalWlanErrorListener;

public class EstateLeaseActivity extends BaseActivity {
	private Button BtnAddCar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blockchain_activity_lease);
		initView();
	}

	private void initView() {
		BtnAddCar = (Button) findViewById(R.id.btn_add_car);
		BtnAddCar.setOnClickListener(this);
	}

	/**
	 * @mark 
	 * @param view
	 */
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_myaccount_wealthManager:
			break;
		case R.id.btn_add_car:
			addCarInfo();
			break;
		}
	}
	
	public void addCarInfo() {
		sendCommend("添加中", CMD_UPDATE_CURRENT_PROGRESS_SHOW);
		CheckBindCardRequest request = new CheckBindCardRequest(
			isBankType, isCardNum, isIDNumber, isUserName, isMobileNum, isProvince, isCity, userId, txtBandCardCode);
			
		Log.d("执行绑卡", request.getUrl(Method.POST));
		volleyHttpClient.postOAuthWithParams(request.getUrl(Method.POST), CarEntity.class, request.getParams(),
			new Response.Listener<CarEntity>() {
				@Override
				public void onResponse(CarEntity response) {
					sendCommend("绑卡中", CMD_UPDATE_CURRENT_PROGRESS_CANCEL);
					if (response != null) {
						if (response.isSuccess()) {
							preferenceStorageService.setCardNum(isIDNumber);
							preferenceStorageService.setUsername(isUserName);
							
							AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
							builder.setMessage(R.string.bindcardsuccess).setPositiveButton(R.string.bindcardbtn, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								if(task != null && !task.isCancelled()){ task.cancel(true); }
									task = null;
									System.gc();
							        finish();
								}
							}).show();
						}else{
	                        if (response.getError() != null && response.getError().size() > 0) {
	                            Log.e("添加银行卡:", response.getError().get(0).getCode() + " " + response.getError().get(0).getMessage() + " " + response.getError().get(0).getType());
	                            showToast(response.getError().get(0).getMessage());
	                        }
						}
					}
				}
			}, new GlobalWlanErrorListener(this){
				@Override
				public void onErrorResponse(VolleyError error) {
					super.onErrorResponse(error);
					setWlanFalseMessage("绑卡失败");
					// 恢复绑卡按钮
					bind.setEnabled(true);
				}
			}, false);
		}
	}
	
	/**
	 * @mark `actionbar的返回`功能
	 * @param view
	 */
	public void back(View view) {
		finish();
	}

}
