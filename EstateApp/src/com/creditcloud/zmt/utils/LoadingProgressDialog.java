package com.creditcloud.zmt.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.estate.R;

public class LoadingProgressDialog extends Dialog {
	private Window window;
	private String content;

	public LoadingProgressDialog(Context context, int theme,String content) {
		super(context, theme);
		window = getWindow();
		window.setWindowAnimations(R.style.animdialogstyle);
		this.content = content;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.loading_dialog);
		TextView t_content = (TextView) findViewById(R.id.t_content);
		t_content.setText(content);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Dialog#dismiss()
	 */
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Dialog#show()
	 */
	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
	}

}
