package com.estate;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.core.utils.SharePreferenceStorageService;
import com.android.core.utils.Toast.ToastUtil;
import com.android.core.utils.Toast.ToastUtilHaveRight;
import com.android.core.view.SplashDialog;
import com.android.core.view.pulltorefresh.PullToRefreshBase.Mode;
import com.android.core.view.pulltorefresh.PullToRefreshListView;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.creditcloud.api.HttpService;
import com.creditcloud.api.VolleyHttpClient;
import com.creditcloud.model.ApiHttp400Error;
import com.creditcloud.model.ApiHttpError;
import com.creditcloud.model.HttpError;
import com.creditcloud.zmt.activity.LoginActivity;
import com.creditcloud.zmt.activity.MainActivity;
import com.creditcloud.zmt.utils.ActivityCollector;
import com.creditcloud.zmt.utils.PrompfDialog;
import com.estate.activity.AddCardActivity;
import com.estate.activity.NotificationUpdateActivity;
import com.estate.utils.wheelview.ArrayWheelAdapter;
import com.estate.utils.wheelview.OnWheelChangedListener;
import com.estate.utils.wheelview.WheelView;
import com.google.gson.Gson;

import java.io.File;
import java.lang.ref.WeakReference;

public class BaseActivity extends FragmentActivity {
	private final static String TAG = "BaseActivity";


	// 网络请求队列
	protected RequestQueue mQueue;

	// 加载中
	public final static int CMD_UPDATE_CURRENT_PROGRESS_SHOW = 3;
	// 取消加载中
	public final static int CMD_UPDATE_CURRENT_PROGRESS_CANCEL = 4;
	public final static int CMD_SHOW_TOAST = 5;
	public final static int CMD_SHOW_TOAST_SHORT = 6;
	public final static int CMD_UPDATE = 7;
	public final static int CMD_SHOW_TOAST_RIGHT = 8;

	// 网络连接失败
	public static final int WLAN_FALSE = 100001;

	protected Context mContext;

	protected UIHandler mHandler = null;

	public static ToastUtil mToastUtil;

	public static ToastUtilHaveRight mToastUtilRight;

	public SplashDialog mDialog;

	public GlobalPhone phone;

	public static HttpService httpService;
	// 判断是不是第一次
	public static SharePreferenceStorageService preferenceStorageService;
	public VolleyHttpClient volleyHttpClient;

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, getClass().getSimpleName() + " onCreate()");
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.hide();
		}
		// requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		this.mContext = BaseActivity.this;

		ActivityCollector.addActivity(this);

		mQueue = Volley.newRequestQueue(this);

		if (mToastUtil == null) {
			mToastUtil = new ToastUtil(this);
		}

		if (mHandler == null) {
			mHandler = new UIHandler(new WeakReference<BaseActivity>(this));
		}

		phone = (GlobalPhone) this.getApplication();

		httpService = HttpService.newInstance(getApplicationContext());
		preferenceStorageService = SharePreferenceStorageService.newInstance(getApplicationContext());
		volleyHttpClient = VolleyHttpClient.newInstance(httpService);
	}
	
	// 点击空白区域 自动隐藏软键盘
    public boolean onTouchEvent(MotionEvent event) {
          if(null != this.getCurrentFocus()){
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super .onTouchEvent(event);
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, getClass().getSimpleName() + " onDestory()");
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}

		ActivityCollector.removeActivity(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, getClass().getSimpleName() + " onResume()");
	}

	protected void sendJsonRequest(JsonRequest request, boolean isShowDialog,
			boolean needAccount) {
		// 是否需要登录
		if (needAccount) {
			return;
		}
		mQueue.add(request);
	}

	protected void sendImageRequest(ImageRequest request, boolean isShowDialog,
			boolean needAccount) {
		// 是否需要登录
		if (needAccount) {
			return;
		}
		mQueue.add(request);
	}

	public void sendCommend(Object message, int what) {
		Message msg = mHandler.obtainMessage(what);
		msg.obj = message;
		mHandler.sendMessage(msg);
	}

	public void showPromptDialog(String message, DialogInterface.OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setMessage(message);
		builder.setCancelable(false);
		if (listener != null) {
			builder.setPositiveButton(
					mContext.getResources().getString(R.string.btn_ok),
					listener);
		}
		builder.create().show();
	}

	protected static class UIHandler extends Handler {

		public WeakReference<BaseActivity> mActivity;

		public UIHandler(WeakReference<BaseActivity> activity) {
			this.mActivity = activity;
		}

		@Override
		public void handleMessage(Message msg) {
			final BaseActivity activity = mActivity.get();
			switch (msg.what) {
			// case CMD_UPDATE_CURRENT:
			// activity.dialog(String.valueOf(msg.obj), null);
			// break;
			// case CMD_UPDATE_CURRENT_ERROR:
			// activity.dialog(String.valueOf(msg.obj), null);
			// break;
			case CMD_UPDATE_CURRENT_PROGRESS_SHOW:
				if (activity.mDialog == null) {
					activity.mDialog = new SplashDialog(activity,
							R.style.dialog, String.valueOf(msg.obj));
					activity.mDialog.show();
					activity.mDialog.setCanceledOnTouchOutside(false);
					// // 注意此处要放在show之后 否则会报异常
					// activity.mDialog
					// .setContentView(R.layout.loading_process_dialog_anim);
					// false设置点击其他地方不能取消进度条
					// mDialog.setCancelable(false);
				} else if (!activity.mDialog.isShowing()) {
					activity.mDialog.setMsg(msg.obj.toString());
					activity.mDialog.show();
				}
				break;
			case CMD_UPDATE_CURRENT_PROGRESS_CANCEL:
				if (activity.mDialog != null && activity.mDialog.isShowing()) {
					activity.mDialog.dismiss();
				}
				break;
			case CMD_UPDATE:
				activity.update(msg.obj);
				break;
			case CMD_SHOW_TOAST_RIGHT:
				if (msg.obj instanceof Integer) {
					int res = ((Integer) msg.obj).intValue();
					activity.showToast(res);
					return;
				}
				String msgstrright = String.valueOf(msg.obj);
				activity.showToastRight(msgstrright);
				break;
			case CMD_SHOW_TOAST:
				if (msg.obj instanceof Integer) {
					int res = ((Integer) msg.obj).intValue();
					activity.showToast(res);
					return;
				} else {
					String msgstr = String.valueOf(msg.obj);
					activity.showToast(msgstr);
				}
				break;
			case CMD_SHOW_TOAST_SHORT:
				if (msg.obj instanceof Integer) {
					int res = ((Integer) msg.obj).intValue();
					activity.showToastShort(res);
					return;
				} else {
					String msgstr = String.valueOf(msg.obj);
					activity.showToastShort(msgstr);
				}
				break;

			}
			super.handleMessage(msg);

		}
	}

	public void update(Object id) {
	}

	public void showToast(String msg) {
		try {
			if (mToastUtil == null) {
				mToastUtil = new ToastUtil(this);
			}
			mToastUtil.show(msg);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void showToastRight(String msg) {
		try {
			if (mToastUtilRight == null) {
				mToastUtilRight = new ToastUtilHaveRight(this);
			}
			mToastUtilRight.show(msg);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void showToast(int res) {
		try {
			if (mToastUtil == null) {
				mToastUtil = new ToastUtil(this);
			}
			mToastUtil.show(res);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void showToastShort(int res) {
		try {
			if (mToastUtil == null) {
				mToastUtil = new ToastUtil(this);
			}
			mToastUtil.showShort(res);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void showToastShort(String res) {
		try {
			if (mToastUtil == null) {
				mToastUtil = new ToastUtil(this);
			}
			mToastUtil.showShort(res);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
	}

	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
	};

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
	}

	/**
	 * 创建和获取缓存路径 
	 * 
	 * @param context
	 * @param uniqueName
	 * @return
	 */
	public File getDiskCacheDir(String uniqueName) {
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())
		// || !Environment.isExternalStorageRemovable()
		) {
			try {
				cachePath = getExternalCacheDir().getPath();
			} catch (Exception e) {
				cachePath = getCacheDir().getPath();
			}
		} else {
			cachePath = getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + uniqueName);
	}

	/**
	 * 获取版本号
	 * 
	 * @param context
	 * @return
	 */
	public int getAppVersion() {
		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 1;
	}

	public HttpError apiHttpErrorjsonToGson(NetworkResponse response) {
		try {
			Gson gson = new Gson();
			String json = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			if (response.statusCode == 400) {
				return gson.fromJson(json, ApiHttp400Error.class);
			} else {
				return gson.fromJson(json, ApiHttpError.class);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setPullList(PullToRefreshListView listview) {
		listview.setReleaseLabel(getString(R.string.releaselabel_end),
				Mode.PULL_FROM_END);
		listview.setRefreshingLabel(getString(R.string.refreshinglabel_end),
				Mode.PULL_FROM_END);
		listview.setPullLabel(getString(R.string.pulllabel_end),
				Mode.PULL_FROM_END);
		listview.setReleaseLabel(getString(R.string.releaselabel_start),
				Mode.PULL_FROM_START);
		listview.setRefreshingLabel(getString(R.string.refreshinglabel_start),
				Mode.PULL_FROM_START);
		listview.setPullLabel(getString(R.string.pulllabel_start),
				Mode.PULL_FROM_START);
		listview.setLastUpdatedLabel("");
	}

	// public void setPullListNoLabel(PullToRefreshListView listview){
	// listview.setReleaseLabel("到顶了", Mode.PULL_FROM_START);
	// listview.setRefreshingLabel("到顶了", Mode.PULL_FROM_START);
	// listview.setPullLabel("到顶了", Mode.PULL_FROM_START);
	// listview.setReleaseLabel("到底了", Mode.PULL_FROM_END);
	// listview.setRefreshingLabel("到底了", Mode.PULL_FROM_END);
	// listview.setPullLabel("到底了", Mode.PULL_FROM_END);
	// listview.setLastUpdatedLabel("");
	// }

	public void setPullListNoLabel(PullToRefreshListView listview) {
		// lv_ordermenu.setReleaseLabel("到顶了", Mode.PULL_FROM_START);
		// lv_ordermenu.setRefreshingLabel("到顶了", Mode.PULL_FROM_START);
		// lv_ordermenu.setPullLabel("到顶了", Mode.PULL_FROM_START);
		listview.setReleaseLabel("到底了没有信息了", Mode.PULL_FROM_END);
		listview.setRefreshingLabel("到底了没有信息了", Mode.PULL_FROM_END);
		listview.setPullLabel("到底了没有信息了", Mode.PULL_FROM_END);
		listview.setLastUpdatedLabel("");
	}

	private Dialog chooseSingeDialog = null;

	protected void showChooseDateDialog(final String[] date, final String title) {
		View view = getLayoutInflater()
				.inflate(R.layout.wheelview_dialog, null);
		chooseSingeDialog = new Dialog(this,
				R.style.transparentFrameWindowStyle);
		chooseSingeDialog.setContentView(view, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		Window window = chooseSingeDialog.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.main_menu_animstyle);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = getWindowManager().getDefaultDisplay().getHeight();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

		// 设置显示位置
		chooseSingeDialog.onWindowAttributesChanged(wl);
		TextView tv = (TextView) view.findViewById(R.id.wheelview_dialog_tv);
		tv.setText(title);
		final WheelView byWhat = (WheelView) view.findViewById(R.id.empty);
		byWhat.setAdapter(new ArrayWheelAdapter<String>(date));
		byWhat.setVisibleItems(5);
		Button btn = (Button) view.findViewById(R.id.wheelview_dialog_btn);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getChooseDialog(byWhat.getCurrentItem(),
						date[byWhat.getCurrentItem()], title);
				chooseSingeDialog.dismiss();

			}
		});
		// 设置点击外围解散
		chooseSingeDialog.setCanceledOnTouchOutside(true);
		chooseSingeDialog.show();
	}

	protected void getChooseDialog(int index, String message, String title) {

	}

	private Dialog chooseDoubleDialog = null;

	protected void showChooseDateDialog(final String[] date,
			final String[][] secondDate, final String title) {
		View view = getLayoutInflater().inflate(
				R.layout.doublewheelview_dialog, null);
		chooseDoubleDialog = new Dialog(this,
				R.style.transparentFrameWindowStyle);
		chooseDoubleDialog.setContentView(view, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		Window window = chooseDoubleDialog.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.main_menu_animstyle);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = getWindowManager().getDefaultDisplay().getHeight();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

		// 设置显示位置
		chooseDoubleDialog.onWindowAttributesChanged(wl);
		TextView tv = (TextView) view.findViewById(R.id.wheelview_dialog_tv);
		tv.setText(title);
		final WheelView wv_first = (WheelView) view.findViewById(R.id.wv_first);
		final WheelView wv_second = (WheelView) view
				.findViewById(R.id.wv_second);
		wv_first.setAdapter(new ArrayWheelAdapter<String>(date));
		wv_first.setVisibleItems(5);
		wv_first.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				wv_second.setAdapter(new ArrayWheelAdapter<String>(
						secondDate[newValue]));
				// wv_second.setCurrentItem(secondDate[newValue].length / 2);
				wv_second.setCurrentItem(0);
			}
		});
		wv_second.setVisibleItems(5);
		wv_first.setCurrentItem(1);
		wv_first.setCurrentItem(0);
		Button btn = (Button) view.findViewById(R.id.wheelview_dialog_btn);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getChooseDialog(
						0,
						date[wv_first.getCurrentItem()]
								+ ","
								+ secondDate[wv_first.getCurrentItem()][wv_second
										.getCurrentItem()], title);
				chooseDoubleDialog.dismiss();

			}
		});
		// 设置点击外围解散
		chooseDoubleDialog.setCanceledOnTouchOutside(true);
		chooseDoubleDialog.show();
	}

	/**
	 * 检验是否可以投标
	 * 
	 * @param status
	 * @return
	 */
	protected boolean isCanTouzi(String status) {
		if (status.equals("OPENED")) {
			return true;
		}
		return false;

	}

	public PrompfDialog loginDialog;

	public void showLoginDialog(final Context context) {
		if (loginDialog == null) {
			loginDialog = new PrompfDialog(context,
					R.style.transparentFrameWindowStyle, "登  录", "关  闭",
					"您还没有登录，请登录", "中民i投");
			loginDialog.setCanceledOnTouchOutside(false);
			loginDialog.setCancelable(false);
			loginDialog.setUpdateOnClickListener(new PrompfDialog.UpdateOnclickListener() {
						// 这里的逻辑和后面的逻辑正好相反 所以使用！
						@Override
						public void dismiss() {
						}

						@Override
						public void BtnYesOnClickListener(View v) {
							Intent intent = new Intent(context,
									LoginActivity.class);
							startActivity(intent);
							loginDialog.dismiss();
						}

						@Override
						public void BtnCancleOnClickListener(View v) {

							if (context instanceof MainActivity) {
								MainActivity.radioGroup.check(R.id.rb_tab_main);
							}
							loginDialog.dismiss();
						}

					});
			Window window = loginDialog.getWindow();
			window.setGravity(Gravity.CENTER);
			loginDialog.show();
			// WindowManager m = this.getWindowManager();
			//
			// // 获取屏幕宽、高用
			// Display d = m.getDefaultDisplay();
			// delete_dialog.getWindow().setLayout((int) (d.getWidth() * 0.7),
			// (int) (d.getHeight() * (0.3)));
		} else {
			loginDialog.show();
		}
	}

	PrompfDialog addCardDialog;

	public void showAddCardDialog(final Context context) {
		if (addCardDialog == null) {
			addCardDialog = new PrompfDialog(context, R.style.transparentFrameWindowStyle,
					"绑  定", "关  闭", "您还没有绑定银行卡，请绑定", "中民i投");
			addCardDialog.setCanceledOnTouchOutside(false);
			addCardDialog.setUpdateOnClickListener(new PrompfDialog.UpdateOnclickListener() {
						// 这里的逻辑和后面的逻辑正好相反 所以使用！
						@Override
						public void dismiss() {
						}

						@Override
						public void BtnYesOnClickListener(View v) {
							Intent intent = new Intent(BaseActivity.this, AddCardActivity.class);
							startActivity(intent);
							addCardDialog.dismiss();
						}

						@Override
						public void BtnCancleOnClickListener(View v) {
							addCardDialog.dismiss();
						}

					});
			Window window = addCardDialog.getWindow();
			window.setGravity(Gravity.CENTER);
			addCardDialog.show();
			// WindowManager m = this.getWindowManager();
			//
			// // 获取屏幕宽、高用
			// Display d = m.getDefaultDisplay();
			// delete_dialog.getWindow().setLayout((int) (d.getWidth() * 0.7),
			// (int) (d.getHeight() * (0.3)));
		} else {
			addCardDialog.show();
		}
	}

	PrompfDialog logOutDialog;

	public void showLogOutDialog(final MainActivity context) {
		if (logOutDialog == null) {
			logOutDialog = new PrompfDialog(context,
					R.style.transparentFrameWindowStyle, "退  出", "关  闭",
					"您确定要退出登录账户吗？", "中民i投");
			logOutDialog.setCanceledOnTouchOutside(false);
			logOutDialog
					.setUpdateOnClickListener(new PrompfDialog.UpdateOnclickListener() {
						// 这里的逻辑和后面的逻辑正好相反 所以使用！
						@Override
						public void dismiss() {
						}

						@Override
						public void BtnYesOnClickListener(View v) {
							context.btnLogout(null);
							logOutDialog.dismiss();
							
							
						}

						@Override
						public void BtnCancleOnClickListener(View v) {
							logOutDialog.dismiss();
						}

					});
			Window window = logOutDialog.getWindow();
			window.setGravity(Gravity.CENTER);
			logOutDialog.show();
			// WindowManager m = this.getWindowManager();
			//
			// // 获取屏幕宽、高用
			// Display d = m.getDefaultDisplay();
			// delete_dialog.getWindow().setLayout((int) (d.getWidth() * 0.7),
			// (int) (d.getHeight() * (0.3)));
		} else {
			logOutDialog.show();
		}
	}

	PrompfDialog updateDialog;

	public void showUpdateDialog(final MainActivity context,
			final String versionPath, final String versionName,
			final String versionCode) {
		if (updateDialog == null) {
			updateDialog = new PrompfDialog(context,
					R.style.transparentFrameWindowStyle, "更  新", "关  闭",
					"检测到最新版本，需要更新吗？", "中民i投");
			updateDialog.setCanceledOnTouchOutside(false);
			updateDialog
					.setUpdateOnClickListener(new PrompfDialog.UpdateOnclickListener() {
						// 这里的逻辑和后面的逻辑正好相反 所以使用！
						@Override
						public void dismiss() {
						}

						@Override
						public void BtnYesOnClickListener(View v) {
							Intent it = new Intent(context,
									NotificationUpdateActivity.class);
							it.putExtra("versionPath", versionPath);
							it.putExtra("versionName", versionName);
							it.putExtra("versionCode", versionCode);
							startActivity(it);
							phone.setDownload(true);
							updateDialog.dismiss();
						}

						@Override
						public void BtnCancleOnClickListener(View v) {
							updateDialog.dismiss();
						}

					});
			Window window = updateDialog.getWindow();
			window.setGravity(Gravity.CENTER);
			updateDialog.show();
			// WindowManager m = this.getWindowManager();
			//
			// // 获取屏幕宽、高用
			// Display d = m.getDefaultDisplay();
			// delete_dialog.getWindow().setLayout((int) (d.getWidth() * 0.7),
			// (int) (d.getHeight() * (0.3)));
		} else {
			updateDialog.show();
		}
	}

}
