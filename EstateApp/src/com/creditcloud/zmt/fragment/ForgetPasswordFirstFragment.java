package com.creditcloud.zmt.fragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import com.android.core.utils.Text.StringUtils;
import com.android.core.view.TextImageButton;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.creditcloud.event.ApiResponse;
import com.creditcloud.event.request.CheckTelExistRequest;
import com.creditcloud.event.request.ImageCodeRequest;
import com.creditcloud.event.request.NewSmsCaptchaRequest;
import com.creditcloud.zmt.activity.ForgetPasswordActivity;
import com.creditcloud.zmt.event.response.GlobalResponse;
import com.creditcloud.zmt.utils.GlobalWlanErrorListener;
import com.creditcloud.event.response.ImageCodeResponse;
import com.estate.R;

/**
 * @author yangkx
 * @mark reset password
 */
public class ForgetPasswordFirstFragment extends BaseFragment {

	private EditText mobileNum;
	private EditText codeNum;
	private TextImageButton codeImage;
	private Button submit;
	public final static String TAG = "ForgetPasswordFirstFragment";
	private ForgetPasswordActivity activity;
	private String mobile, code, token;
	private float pixDensity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View reset_pwd_view = inflater.inflate(R.layout.fragment_updatepwd1,
				null);
		activity = (ForgetPasswordActivity) this.getActivity();

		findView(reset_pwd_view);
		getSymbolCode();

		return reset_pwd_view;
	}

	/**
	 * @mark For graphical verification code
	 */
	public void getSymbolCode() {
		ImageCodeRequest request = new ImageCodeRequest();
		activity.volleyHttpClient.get(request.getUrl(Method.GET),
				ImageCodeResponse.class,
				new Response.Listener<ImageCodeResponse>() {
					@Override
					public void onResponse(ImageCodeResponse response) {
						if (response != null
								&& !TextUtils.isEmpty(response.getCaptcha())) {
							byte[] bitmapArray = Base64.decode(
									response.getCaptcha()
											.substring(
													response.getCaptcha()
															.indexOf(",") + 1),
									Base64.DEFAULT);
							Bitmap bitmap = BitmapFactory.decodeByteArray(
									bitmapArray, 0, bitmapArray.length);

							// 根据手机分辨率动态的修改图形验证码的显示
							WindowManager wm = (WindowManager) (getActivity()
									.getSystemService(Context.WINDOW_SERVICE));
							DisplayMetrics dm = new DisplayMetrics();
							wm.getDefaultDisplay().getMetrics(dm);
							pixDensity = dm.density;

							// 分辨率是720p或者1080p的调整图形验证码的大小
							if (pixDensity > 300) {

								bitmap = bitmap.createScaledBitmap(bitmap, 230,
										80, true);
							} 
							else {
								bitmap = bitmap.createScaledBitmap(bitmap, 230,
										80, true);
							}

							codeImage.setImageBitmap(bitmap);
							codeImage.setText("");
							token = response.getToken();
						} else {
							activity.showToast("获取验证码失败，请点击重试");
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						activity.showToast("获取验证码失败，请点击重试");
						codeImage.setText("获取验证码");
						codeImage.setImageBitmap(null);
					}
				}, false);
	}

	/**
	 * @param view
	 * @mark find view by id
	 */
	private void findView(View view) {
		mobileNum = (EditText) view.findViewById(R.id.et_updatepwd_mobile);
		codeNum = (EditText) view.findViewById(R.id.et_code);
		codeImage = (TextImageButton) view.findViewById(R.id.img_code);
		codeImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				getSymbolCode();
			}
		});

		submit = (Button) view.findViewById(R.id.btn_next);
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				sendCode();
			}
		});
	};

	/**
	 * @mark click to reset_password
	 */
	private void sendCode() {
		mobile = mobileNum.getText().toString().trim();
		code = codeNum.getText().toString().trim();

		// storaged for ForgetPasswordSecondFragment
		activity.preferenceStorageService.setResetPwdMobile(mobile);

		if (TextUtils.isEmpty(mobile)) {
			activity.showToast(this.getString(R.string.regesiter_user_mobile));
			return;
		}
		if (TextUtils.isEmpty(code)) {
			activity.showToast(this.getString(R.string.resetPwd_imagecode));
			return;
		}
		if (isMobileNO(mobile)) {
			// 验证手机号码是否存在
			checkTelExist();
		} else {
			activity.showToast("请输入正确的手机号码");
			return;
		}
	}

	/**
	 * @mark verify the symbol code
	 */
	private void checkTelExist() {
		activity.sendCommend("加载中", activity.CMD_UPDATE_CURRENT_PROGRESS_SHOW);
		CheckTelExistRequest request = new CheckTelExistRequest(mobile);

		activity.volleyHttpClient.postWithParams(request.getUrl(Method.POST),
				GlobalResponse.class, request.getParams(),
				new Response.Listener<GlobalResponse>() {
					@Override
					public void onResponse(GlobalResponse response) {
						activity.sendCommend("加载中",
								activity.CMD_UPDATE_CURRENT_PROGRESS_CANCEL);
						if (response != null) {
							if (!response.isSuccess()) {
								verifySymbolCode();
							} else {
								if (response.getError() != null
										&& response.getError().size() > 0) {
									Log.e("ForgetPassword:", response
											.getError().get(0).getCode()
											+ " "
											+ response.getError().get(0)
													.getMessage()
											+ " "
											+ response.getError().get(0)
													.getType());
									activity.showToast(response.getError()
											.get(0).getMessage());
								} else {
									activity.showToast("该手机号码未注册");
									return;
								}
							}
						} else {
							activity.showToast("请求失败，请重试");
						}
					}
				}, new GlobalWlanErrorListener(activity) {
					@Override
					public void onErrorResponse(VolleyError error) {
						super.onErrorResponse(error);
						setWlanFalseMessage("请求失败，请重试");
					}
				}, false);
	}

	/**
	 * @mark verify the symbol code
	 */
	private void verifySymbolCode() {
		NewSmsCaptchaRequest request = new NewSmsCaptchaRequest(token, code);
		activity.volleyHttpClient.postWithParams(request.getUrl(Method.POST),
				GlobalResponse.class, request.getParams(),
				new Response.Listener<GlobalResponse>() {
					@Override
					public void onResponse(GlobalResponse response) {
						if (response != null) {
							activity.sendCommend("加载中",
									activity.CMD_UPDATE_CURRENT_PROGRESS_CANCEL);
							if (response.isSuccess()) {
								// 启动新的fragment展现view
								activity.changeFragment();
							} else {
								activity.showToast("图形码不一致");
								getSymbolCode();
							}
						}
					}
				}, new GlobalWlanErrorListener(activity) {
					@Override
					public void onErrorResponse(VolleyError error) {
						super.onErrorResponse(error);
						setWlanFalseMessage("获取验证码失败");
					}
				}, false);
	}

	/**
	 * @param phone
	 * @mark 根据移动联通电信号码段判断电话是否合法
	 * @return
	 */
	public boolean isMobileNO(String mobiles) {
		boolean flag = false;
		try {
			Pattern p = Pattern
					.compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(17[0,6-8])|(18[0-9]))\\d{8}$");
			Matcher m = p.matcher(mobiles);
			flag = m.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * @param email
	 * @mark 判断邮箱是否合法
	 * @return
	 */
	public boolean isEmail(String email) {
		if (null == email || "".equals(email))
			return false;
		// Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
		Pattern p = Pattern
				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");// 复杂匹配
		Matcher m = p.matcher(email);
		return m.matches();
	}
}
