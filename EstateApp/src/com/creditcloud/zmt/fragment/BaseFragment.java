package com.creditcloud.zmt.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BaseFragment extends Fragment{
	
	private final static String TAG = "BaseFragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, getClass().getSimpleName()+"onCreate()");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, getClass().getSimpleName()+"onCreateView()");
		return super.onCreateView(inflater, container, savedInstanceState);
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d(TAG, getClass().getSimpleName()+"onCreateAttach()");
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, getClass().getSimpleName()+"onActivityCreated()");
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.d(TAG, getClass().getSimpleName()+"onDestoryView()");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, getClass().getSimpleName()+"onDestory()");
	}
}
