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
package com.mobilepower.tong.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.mobilepower.tong.R;
import com.mobilepower.tong.ui.view.CustomAvatarView;

public class UserInfoActivity extends BaseActivity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info_activity);
		
		initActionBar();
		initView();
		initData();
	}
	
	private View mBackBtn;

	/**
	 * 初始化actionBar
	 */
	private void initActionBar() {
		mBackBtn = findViewById(R.id.back_btn);

		mBackBtn.setOnClickListener(this);
	}
	
	private CustomAvatarView mAvatarView;
	private TextView mNickName;
	
	private void initView() {
		mAvatarView = (CustomAvatarView) findViewById(R.id.user_info_avatar);
		mNickName = (TextView) findViewById(R.id.user_info_nickname);
	}
	
	private Drawable maleDrawable;
	private Drawable femaleDrawable;
	
	private void initData() {
		maleDrawable = getResources().getDrawable(R.drawable.flag_gender_male);
		maleDrawable.setBounds(0, 0, maleDrawable.getMinimumWidth(),
				maleDrawable.getMinimumHeight());
		
		femaleDrawable = getResources().getDrawable(R.drawable.flag_gender_female);
		femaleDrawable.setBounds(0, 0, femaleDrawable.getMinimumWidth(),
				femaleDrawable.getMinimumHeight());
		
		mAvatarView.setImageUrl("http://ww2.sinaimg.cn/bmiddle/684ff39bgw1ejfep2t9bcj20sg0ixq50.jpg");
		
		mNickName.setCompoundDrawables(null, null, maleDrawable, null);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == mBackBtn) {
			this.finish();
		}
	}
	
}
