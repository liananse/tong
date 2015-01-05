/*
 * Copyright 2014 zenghui.wang.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mobilepower.tong.ui.view;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mobilepower.tong.R;
import com.mobilepower.tong.TongApplication;
import com.mobilepower.tong.http.HHttpDataLoader;
import com.mobilepower.tong.http.HHttpDataLoader.HDataListener;
import com.mobilepower.tong.model.BaseInfo;
import com.mobilepower.tong.model.UserInfo;
import com.mobilepower.tong.ui.activity.MainTabActivity;
import com.mobilepower.tong.ui.activity.RegisterStepOneActivity;
import com.mobilepower.tong.ui.activity.RegisterStepTwoActivity;
import com.mobilepower.tong.ui.activity.SplashActivity;
import com.mobilepower.tong.ui.fragment.FLoadingProgressBarFragment;
import com.mobilepower.tong.utils.UConfig;
import com.mobilepower.tong.utils.UConstants;
import com.mobilepower.tong.utils.UToast;
import com.mobilepower.tong.utils.UTools;

public class SplashView extends ViewGroup {

	private Context mContext;
	private Resources mRes;

	private ImageView mSplashLogo;

	private EditText mMobileEt;
	private EditText mPasswordEt;

	private TextView mLoginBtn;
	private TextView mRegisterBtn;

	private int padding;
	private int margin;
	private int btnPadding;

	public SplashView(Context context) {
		super(context);
		initView(context);

	}

	public SplashView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	private void initView(Context context) {
		this.mContext = context;
		this.mRes = this.mContext.getResources();

		mSplashLogo = new ImageView(context);
		// 暂时先用灰色色块代替
		// mSplashLogo.setBackgroundResource(R.color.gray);
		mSplashLogo.setImageResource(R.drawable.logo);

		margin = this.mRes
				.getDimensionPixelSize(R.dimen.activity_horizontal_margin);
		btnPadding = this.mRes
				.getDimensionPixelSize(R.dimen.splash_btn_padding);

		mMobileEt = new EditText(context);
		mMobileEt.setTextColor(getResources().getColor(R.color.black));
		mMobileEt.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				mRes.getDimensionPixelSize(R.dimen.splash_btn_text_size));
		mMobileEt.setBackgroundResource(R.drawable.username_and_password_et_bg);
		mMobileEt.setSingleLine(true);
		mMobileEt.setPadding(btnPadding, btnPadding, btnPadding, btnPadding);
		mMobileEt.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		mMobileEt.setHint(getResources().getString(R.string.login_mobile_tips));
		mMobileEt.setInputType(InputType.TYPE_CLASS_PHONE);

		mPasswordEt = new EditText(context);
		mPasswordEt.setTextColor(getResources().getColor(R.color.black));
		mPasswordEt.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				mRes.getDimensionPixelSize(R.dimen.splash_btn_text_size));
		mPasswordEt
				.setBackgroundResource(R.drawable.username_and_password_et_bg);
		mPasswordEt.setSingleLine(true);
		mPasswordEt.setPadding(btnPadding, btnPadding, btnPadding, btnPadding);
		mPasswordEt.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		mPasswordEt.setHint(getResources().getString(
				R.string.login_password_tips));
		mPasswordEt.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);

		mLoginBtn = new TextView(context);
		mLoginBtn.setBackgroundResource(R.drawable.login_and_register_btn_bg);
		mLoginBtn.setGravity(Gravity.CENTER);
		mLoginBtn.setPadding(btnPadding, btnPadding, btnPadding, btnPadding);
		mLoginBtn.setSingleLine(true);
		mLoginBtn.setText(R.string.splash_login);
		mLoginBtn.setTextColor(mRes.getColor(android.R.color.white));
		mLoginBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				mRes.getDimensionPixelSize(R.dimen.splash_btn_text_size));
		mLoginBtn.setSoundEffectsEnabled(false);

		mRegisterBtn = new TextView(context);
		mRegisterBtn.setBackgroundResource(R.drawable.blue_btn_bg);
		mRegisterBtn.setGravity(Gravity.CENTER);
		mRegisterBtn.setPadding(btnPadding, btnPadding, btnPadding, btnPadding);
		mRegisterBtn.setSingleLine(true);
		mRegisterBtn.setText(R.string.splash_register);
		mRegisterBtn.setTextColor(mRes.getColor(android.R.color.black));
		mRegisterBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				mRes.getDimensionPixelSize(R.dimen.splash_btn_text_size));
		mRegisterBtn.setSoundEffectsEnabled(false);

		this.setBackgroundResource(R.color.activity_bg);

		mLoginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// Intent intent = new Intent(mContext, LoginActivity.class);
				// mContext.startActivity(intent);
				loginMethod();
			}
		});

		mRegisterBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext,
						RegisterStepOneActivity.class);
				mContext.startActivity(intent);
			}
		});

		addView(mSplashLogo);
		addView(mMobileEt);
		addView(mPasswordEt);
		addView(mLoginBtn);
		addView(mRegisterBtn);
	}

	@Override
	protected void onLayout(boolean arg0, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		int width = r - l;
		int height = b - t;

		int btnWidth = this.mLoginBtn.getMeasuredWidth();

		int loginBtnHeight = this.mLoginBtn.getMeasuredHeight();
		int registerBtnHeight = this.mRegisterBtn.getMeasuredHeight();

		l = (width - btnWidth) / 2;
		t = (height - padding - registerBtnHeight - margin - loginBtnHeight);

		int logoBgHeight = t - margin;

		this.mLoginBtn.layout(l, t, l + btnWidth, t + loginBtnHeight);

		t = t + loginBtnHeight + margin;

		this.mRegisterBtn.layout(l, t, l + btnWidth, t + registerBtnHeight);

		int smallSize;

		if (logoBgHeight > width) {
			smallSize = width;
		} else {
			smallSize = logoBgHeight;
		}

		// 暂定logo为正方形，高宽为最小边的二分之一，且位于中间位置

		int logoWidth = smallSize / 4;

		l = (width - logoWidth) / 2;

		int mobileHeight = this.mMobileEt.getMeasuredHeight();
		int passwordHeight = this.mPasswordEt.getMeasuredHeight();

		logoBgHeight = logoBgHeight - passwordHeight - margin - mobileHeight
				- margin;

		t = (logoBgHeight - logoWidth) / 2;

		this.mSplashLogo.layout(l, t, l + logoWidth, t + logoWidth);

		t = logoBgHeight + margin;
		l = (width - btnWidth) / 2;

		this.mMobileEt.layout(l, t, l + btnWidth, t + mobileHeight);

		t = t + mobileHeight + margin;

		this.mPasswordEt.layout(l, t, l + btnWidth, t + passwordHeight);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);

		// 按钮距屏幕边缘的宽度为屏幕的六分之一
		padding = width / 6;
		// 按钮宽度为屏幕宽度的三分之二
		int btnWidth = (width * 2) / 3;

		this.mMobileEt.measure(
				MeasureSpec.makeMeasureSpec(btnWidth, MeasureSpec.EXACTLY),
				MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST));
		this.mPasswordEt.measure(
				MeasureSpec.makeMeasureSpec(btnWidth, MeasureSpec.EXACTLY),
				MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST));
		this.mLoginBtn.measure(
				MeasureSpec.makeMeasureSpec(btnWidth, MeasureSpec.EXACTLY),
				MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST));
		this.mRegisterBtn.measure(
				MeasureSpec.makeMeasureSpec(btnWidth, MeasureSpec.EXACTLY),
				MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST));

		this.mSplashLogo.measure(
				MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST),
				MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST));
	}

	private HHttpDataLoader mDataLoader = new HHttpDataLoader();

	private void loginMethod() {

		if (mMobileEt.getText().toString().trim().equals("")
				|| !UTools.OS.isMobile(mMobileEt.getText().toString().trim())) {
			UToast.showShortToast(mContext, "手机号码不能为空或者格式不准确。");
			return;
		}

		if (mPasswordEt.getText().toString().trim().equals("")
				|| !UTools.OS.isPassword(mPasswordEt.getText().toString()
						.trim())) {
			UToast.showShortToast(mContext, "密码不能为空或者小于6位。");
			return;
		}
		// dialog show
		final FLoadingProgressBarFragment mLoadingProgressBarFragment = new FLoadingProgressBarFragment();
		FragmentTransaction ft = ((SplashActivity) mContext)
				.getSupportFragmentManager().beginTransaction();
		mLoadingProgressBarFragment.show(ft, "dialog");

		// 设置不可点击
		mLoginBtn.setEnabled(false);

		// 参数
		// SharedPreferences sp = UTools.Storage.getSharedPreferences(mContext,
		// UConstants.BASE_PREFS_NAME);

		Map<String, String> params = new HashMap<String, String>();
		params.put("mobile", mMobileEt.getText().toString());
		params.put("pwd", mPasswordEt.getText().toString());
		// params.put("xiaomiUserId", sp.getString(UConstants.XIAOMI_REGID,
		// ""));
		// params.put("x", sp.getString(UConstants.LOCATION_LATITUDE, "0.0"));
		// params.put("y", sp.getString(UConstants.LOCATION_LONGITUDE, "0.0"));
		mDataLoader.postData(UConfig.USER_LOGIN_URL, params, mContext,
				new HDataListener() {

					@Override
					public void onSocketTimeoutException(String msg) {
						// TODO Auto-generated method stub
						UToast.showSocketTimeoutToast(mContext);
						mLoadingProgressBarFragment.dismiss();
						mLoginBtn.setEnabled(true);
					}

					@Override
					public void onFinish(String source) {
						// TODO Auto-generated method stub
						mLoadingProgressBarFragment.dismiss();

						Gson gson = new Gson();
						try {
							TempModel mResultModel = gson.fromJson(source,
									new TypeToken<TempModel>() {
									}.getType());

							if (mResultModel != null) {
								if (mResultModel.result == UConstants.SUCCESS) {
									UserInfo mSelf = mResultModel.user;
									mSelf.access_token = mResultModel.access_token;
									// 将用户个人信息存数据库
									TongApplication.initMineInfo(mContext,
											mSelf);
									// 将accesstoken同时放到sharedpreferences中
									SharedPreferences.Editor mEditor = UTools.Storage
											.getSharedPreEditor(mContext,
													UConstants.BASE_PREFS_NAME);
									mEditor.putString(
											UConstants.SELF_ACCESS_TOKEN,
											mResultModel.access_token);
									mEditor.commit();

									if (mSelf.nickName != null && !mSelf.nickName.equals("")) {
										Intent intent = new Intent(mContext,
												MainTabActivity.class);
										mContext.startActivity(intent);
										if (mContext instanceof SplashActivity) {
											((SplashActivity) mContext).finish();
										}
									} else {
										Intent intent = new Intent(mContext, RegisterStepTwoActivity.class);
										mContext.startActivity(intent);
										if (mContext instanceof SplashActivity) {
											((SplashActivity) mContext).finish();
										}
									}
									
								} else {
									UToast.showShortToast(mContext,
											mResultModel.msg);
								}
							} else {
								UToast.showOnFail(mContext);
							}

						} catch (JsonSyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							UToast.showDataParsingError(mContext);
						}
						mLoginBtn.setEnabled(true);
					}

					@Override
					public void onFail(String msg) {
						// TODO Auto-generated method stub
						UToast.showOnFail(mContext);
						mLoadingProgressBarFragment.dismiss();
						mLoginBtn.setEnabled(true);
					}

					@Override
					public void onConnectTimeoutException(String msg) {
						// TODO Auto-generated method stub
						UToast.showConnectTimeoutToast(mContext);
						mLoadingProgressBarFragment.dismiss();
						mLoginBtn.setEnabled(true);
					}
				});
	}

	class TempModel extends BaseInfo {
		public UserInfo user;
		public String access_token;
	}

}
