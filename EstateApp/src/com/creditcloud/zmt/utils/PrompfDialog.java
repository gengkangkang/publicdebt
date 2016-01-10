package com.creditcloud.zmt.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.estate.R;

public class PrompfDialog extends Dialog{

	Context context;
	private Button btn_cancle, btn_yes;
	private TextView  tv_content,uptv_title;
	private String ok;
	private String cancel;
	private String content;
	private String title;
	private View view_v;

	public PrompfDialog(Context context, int theme,String ok,String cancel,String content,String title) {
		super(context, theme);
		this.context = context;
		this.ok = ok;
		this.cancel = cancel;
		this.content = content;
		this.title = title;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_prompf);
		initViews();
	}

	private void initViews() {
		
		btn_yes = (Button) findViewById(R.id.btn_ok);
		btn_yes.setText(this.ok);
		btn_cancle = (Button) findViewById(R.id.btn_cancel);
		
		view_v = (View) findViewById(R.id.view_v);
		
		if(this.cancel== null){
			btn_cancle.setVisibility(View.GONE);
			view_v.setVisibility(View.GONE);
		}else{
			btn_cancle.setText(this.cancel);
		}
		tv_content = (TextView) findViewById(R.id.dialog_content);
		tv_content.setText(this.content);
		uptv_title = (TextView) findViewById(R.id.dialog_title);
		uptv_title.setText(this.title);
		btn_cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mListener != null) {
					mListener.BtnCancleOnClickListener(v);
				}
			}
		});
		btn_yes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mListener != null) {
					mListener.BtnYesOnClickListener(v);
				}
			}
		});
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
		if (mListener != null) {
			mListener.dismiss();
		}
	}

	public UpdateOnclickListener mListener = null;

	public void setUpdateOnClickListener(UpdateOnclickListener mListener) {
		this.mListener = mListener;
	}

	public interface UpdateOnclickListener {
		public void dismiss();

		public void BtnYesOnClickListener(View v);

		public void BtnCancleOnClickListener(View v);
	}
	
}
