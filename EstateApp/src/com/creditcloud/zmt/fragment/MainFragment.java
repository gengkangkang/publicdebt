package com.creditcloud.zmt.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.core.utils.Screen.BaseTools;
import com.android.core.utils.Text.ObjectUtils;
import com.android.core.utils.Text.StringUtils;
import com.android.core.view.slideviewpager.AutoScrollViewPager;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.creditcloud.api.ApiConstants;
import com.creditcloud.model.Banner;
import com.creditcloud.model.Loan;
import com.creditcloud.zmt.activity.LoginActivity;
import com.creditcloud.zmt.activity.MainActivity;
import com.creditcloud.zmt.activity.RegesiterActivity;
import com.creditcloud.zmt.adapter.ImagePagerAdapter;
import com.creditcloud.zmt.utils.GlobalWlanErrorListener;
import com.creditcloud.zmt.utils.XinApiConstants;
import com.estate.R;
import com.estate.activity.ProductDescripActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/***
 * @author common
 * @mark 首页
 */
public class MainFragment extends BaseFragment implements OnClickListener, OnRefreshListener {

	private RelativeLayout mBannerLayout = null;
	private AutoScrollViewPager banner;
	private List<Banner> imageIdList;
	private Button login;
	private MainActivity activity;
	private TextView title;
	private TextView price;
	private TextView rate;
	private TextView limit;
	private Button buy;
	private ImagePagerAdapter adapter;
	private SwipeRefreshLayout swipeRefreshLayout;
	private Loan loan;
	private TextView status;
	private Button m_regesiter;
	private boolean pointPicLoadStatus = false;
	private LinearLayout pointGroup;
	private final float mBannerW = 1280f;
	private final float mBannerH = 400f;
//	private final float mBannerW = 1900f;
//	private final float mBannerH = 406f;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		activity = (MainActivity) this.getActivity();
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View main_view = inflater.inflate(R.layout.fragment_main, container, false);
		activity = (MainActivity) this.getActivity();
		// 获取界面的控件
		findView(main_view);
		activity.sendCommend("加载中", activity.CMD_UPDATE_CURRENT_PROGRESS_SHOW);
		// 获取轮播图等信息
		getInformation();
		
		MobclickAgent.onPageStart(this.getClass().getName());
		return main_view;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		// stop auto scroll when onPause
		swipeRefreshLayout.setRefreshing(false);
		banner.stopAutoScroll();
	}

	@Override
	public void onResume() {
		super.onResume();
		setLoginButton();
		banner.startAutoScroll();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (hidden) {
			MobclickAgent.onPageEnd(this.getClass().getName());
			swipeRefreshLayout.setRefreshing(false);
			setLoginButton();
			banner.stopAutoScroll();
		} else {
			MobclickAgent.onPageStart(this.getClass().getName());
			banner.startAutoScroll();
			if (activity.loginDialog != null && activity.loginDialog.isShowing()) {
				activity.loginDialog.dismiss();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_title_regesiter:
			Intent intent = new Intent(activity, RegesiterActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_title_right:
			loginOrLogout();
			break;
		case R.id.btn_buy:
			if (loan != null && !TextUtils.isEmpty(loan.getId())) {
				ProductDescripActivity.startAc(activity, loan.getId());
			} else {
				activity.showToast("信息获取失败，请刷新界面");
			}
			break;
		}
	}

	@Override
	public void onRefresh() {
		getInformation();
	}

	/**
	 * @mark 首页的注册/登录
	 */
	private void loginOrLogoutRegesiter() {
		if (activity.preferenceStorageService.isLogin()) {
			m_regesiter.setVisibility(View.GONE);
		} 
	}
	
	/**
	 * @获取轮播图
	 */
	private void getInformation() {
		banner.startAutoScroll();
		activity.volleyHttpClient.getByStringRequest(ApiConstants.URL_BASE + ApiConstants.API_BANNERS, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				
				if (!TextUtils.isEmpty(response)) {
					Type type = new TypeToken<ArrayList<Banner>>() {}.getType();
					Gson gson = new Gson();
					imageIdList = gson.fromJson(response, type);

					adapter.setImageIdList(imageIdList);


					if(!pointPicLoadStatus){
						for (int i = 0; i < imageIdList.size(); i++) {
							initIndicator(i);
						}
						pointPicLoadStatus = true;
					}
//					pointGroup.removeAllViews();
					adapter.notifyDataSetChanged();
				}

				swipeRefreshLayout.setRefreshing(false);
				activity.sendCommend("加载中", activity.CMD_UPDATE_CURRENT_PROGRESS_CANCEL);
			}
		}, new GlobalWlanErrorListener(activity) {
			@Override
			public void onErrorResponse(VolleyError error) {
				super.onErrorResponse(error);
				swipeRefreshLayout.setRefreshing(false);
			}
		}, true);

		activity.volleyHttpClient.getByStringRequest(ApiConstants.URL_BASE
				+ XinApiConstants.API_TUIJIAN, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				try {
					Type type = new TypeToken<ArrayList<Loan>>() {}.getType();
					Gson gson = new Gson();
					List<Loan> list = new ArrayList<Loan>();
					if (response != null) {
						list = gson.fromJson(response, type);
						if (list != null && list.size() > 0) {
							for (int i = 0; i < list.size(); i++) {
								if (list.get(i).getStatus().equals("OPENED")) {
									loan = list.get(i);
									loan = (Loan) ObjectUtils.isEmpty(loan, Loan.class);
									succedToGetToday(loan);
									return;
								}
							}
							loan = list.get(0);
							loan = (Loan) ObjectUtils.isEmpty(loan, Loan.class);
							succedToGetToday(loan);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {}
		}, true);
	}

	private void initIndicator(int i) {
		ImageView pointView = new ImageView(getActivity());
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.rightMargin = 20;
		pointView.setLayoutParams(layoutParams);
		pointView.setBackgroundResource(R.drawable.point_bg);
		if (i == 0) {
			pointView.setEnabled(true);
		} else {
			pointView.setEnabled(false);
		}
		pointGroup.addView(pointView);
	}

	/**
	 * @mark 获取界面的控件
	 * @param view
	 */
	private void findView(View view) {
		mBannerLayout = (RelativeLayout)view.findViewById(R.id.rl_banner);
		banner = (AutoScrollViewPager) view.findViewById(R.id.view_pager);

		pointGroup = (LinearLayout) view.findViewById(R.id.inll);
		swipeRefreshLayout = (SwipeRefreshLayout) view
				.findViewById(R.id.swipe_container);
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		swipeRefreshLayout.setOnRefreshListener(this);
		
		title = (TextView) view.findViewById(R.id.tv_main_title);
		price = (TextView) view.findViewById(R.id.tv_startmoney);
		rate = (TextView) view.findViewById(R.id.tv_shouyilv);
		limit = (TextView) view.findViewById(R.id.tv_licailimit);
		buy = (Button) view.findViewById(R.id.btn_buy);
		status = (TextView) view.findViewById(R.id.tv_mainstatus);
		buy.setOnClickListener(this);
		
		m_regesiter = (Button) view.findViewById(R.id.btn_title_regesiter);
		m_regesiter.setOnClickListener(this);
		
		login = (Button) view.findViewById(R.id.btn_title_right);
		login.setOnClickListener(this);
		adapter = new ImagePagerAdapter(activity, imageIdList);

		int w = BaseTools.getWindowWidth(getActivity());
		float scale = w / mBannerW;
		int h = (int) (mBannerH * scale);

		ViewGroup.LayoutParams params = mBannerLayout.getLayoutParams();
		params.width = w;
		params.height = h;
		mBannerLayout.setLayoutParams(params);

		banner.setAdapter(adapter);

		banner.setOnPageChangeListener(new MyOnPageChangeListener());
		banner.setInterval(5000);
		banner.startAutoScroll();
		banner.requestDisallowInterceptTouchEvent(true);
	}

	private void succedToGetToday(Loan loan) {
		title.setText(loan.getTitle());
		price.setText(String.format(
				this.getString(R.string.tab_touzi_limit),
				loan.getDuration().getTotalDays(),
				""
						+ StringUtils.addSeparateSign((int) loan
								.getLoanRequest().getInvestRule()
								.getMinAmount())));
		rate.setText(String.format(this.getString(R.string.font_float),
				loan.getRate() / 100.00)
				+ "%");

		if (loan.getStatus().equals("OPENED")) {
			buy.setText("立即购买");
		} else if (loan.getStatus().equals("SCHEDULED")) {
			buy.setText("即将开始");
		} else {
			buy.setText("已售罄");
		}
	}

	int lastPosition;

	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int position) {
			pointGroup.getChildAt(position).setEnabled(true);
			pointGroup.getChildAt(lastPosition).setEnabled(false);
			lastPosition = position;
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {}
	}

	private void loginOrLogout() {
		if (activity.preferenceStorageService.isLogin()) {
			activity.showLogOutDialog(activity);
		} else {
			Intent intent = new Intent(activity, LoginActivity.class);
			startActivity(intent);
		}
	}
	
	//判断登陆状态   立即注册按钮随之显示或隐藏
	public void setLoginButton() {
		if (activity.preferenceStorageService.isLogin()) {
			
			login.setText(this.getString(R.string.logout));
			m_regesiter.setVisibility(View.GONE);
		} else {
			
			login.setText(this.getString(R.string.login));
			m_regesiter.setVisibility(View.VISIBLE);
		}
	}
}
