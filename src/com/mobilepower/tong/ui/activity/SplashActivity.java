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

import android.content.Intent;
import android.os.Bundle;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.mobilepower.tong.TongApplication;
import com.mobilepower.tong.model.UserInfo;
import com.mobilepower.tong.push.TongPushUtils;
import com.mobilepower.tong.ui.controller.SplashViewController;
import com.squareup.otto.Bus;

public class SplashActivity extends BaseActivity {

	private Bus bus;
	private SplashViewController mSplashViewController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		bus = TongApplication.getBus();

		if (!TongPushUtils.hasBind(getApplicationContext())) {
			System.out.println("startWork bind");
			PushManager.startWork(getApplicationContext(),
					PushConstants.LOGIN_TYPE_API_KEY,
					TongPushUtils.getMetaValue(SplashActivity.this, "api_key"));
			// Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
			// PushManager.enableLbs(getApplicationContext());

		} else {
			if (PushManager.isPushEnabled(getApplicationContext())) {
				PushManager.resumeWork(getApplicationContext());
			}
		}
		
		gotoActivity();
	}

	private void gotoActivity() {
		UserInfo mInfo = TongApplication.getMineInfo(this);

		if (mInfo != null) {
			if (mInfo.nickName != null && !mInfo.nickName.equals("")) {
				Intent intent = new Intent(this, MainTabActivity.class);
				this.startActivity(intent);
				this.finish();
			} else {
				Intent intent = new Intent(this, RegisterStepTwoActivity.class);
				this.startActivity(intent);
				this.finish();
			}
		} else {
			mSplashViewController = new SplashViewController(this);
			setContentView(mSplashViewController.getView());
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		this.bus.register(this);
		if (mSplashViewController != null) {
			mSplashViewController.onShow(null);
		}
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
		this.bus.unregister(this);
		if (mSplashViewController != null) {
			mSplashViewController.onHide(null);
		}
	}

}
