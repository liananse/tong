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

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilepower.tong.R;
import com.mobilepower.tong.ui.activity.LoginActivity;
import com.mobilepower.tong.ui.activity.RegisterStepOneActivity;

public class SplashView extends ViewGroup {

	private Context mContext;
	private Resources mRes;

	private ImageView mSplashLogo;
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
		mSplashLogo.setBackgroundResource(R.color.gray);

		margin = this.mRes
				.getDimensionPixelSize(R.dimen.activity_horizontal_margin);
		btnPadding = this.mRes
				.getDimensionPixelSize(R.dimen.splash_btn_padding);

		mLoginBtn = new TextView(context);
		mLoginBtn.setBackgroundResource(R.drawable.blue_btn_bg);
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
		mRegisterBtn.setTextColor(mRes.getColor(android.R.color.white));
		mRegisterBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				mRes.getDimensionPixelSize(R.dimen.splash_btn_text_size));
		mRegisterBtn.setSoundEffectsEnabled(false);

		this.setBackgroundResource(R.color.activity_bg);

		mLoginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, LoginActivity.class);
				mContext.startActivity(intent);
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

		int logoBgHeight = t - padding;

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

		int logoWidth = smallSize / 2;

		l = (width - logoWidth) / 2;
		t = (logoBgHeight - logoWidth) / 2;

		this.mSplashLogo.layout(l, t, l + logoWidth, t + logoWidth);

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

}
