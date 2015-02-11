package com.mobilepower.tong.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.mobilepower.tong.R;
import com.mobilepower.tong.TongApplication;
import com.mobilepower.tong.model.UserInfo;
import com.mobilepower.tong.push.TongPushUtils;
import com.mobilepower.tong.utils.UConstants;
import com.mobilepower.tong.utils.UTools;
import com.squareup.otto.Bus;

public class WelcomeActivity extends BaseActivity {

	private Bus bus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_activity);
		bus = TongApplication.getBus();

		if (!TongPushUtils.hasBind(getApplicationContext())) {
			PushManager
					.startWork(getApplicationContext(),
							PushConstants.LOGIN_TYPE_API_KEY, TongPushUtils
									.getMetaValue(WelcomeActivity.this,
											"api_key"));
			// Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
			// PushManager.enableLbs(getApplicationContext());

		} else {
			if (PushManager.isPushEnabled(getApplicationContext())) {
				PushManager.resumeWork(getApplicationContext());
			}
		}

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				gotoActivity();
			}
		}, 1500);
	}

	private void gotoActivity() {

		boolean isFirst = UTools.Storage.getSharedPreferences(this,
				UConstants.BASE_PREFS_NAME).getBoolean(UConstants.FIRST_LOGIN,
				true);

		if (isFirst) {
			SharedPreferences.Editor mEditor = UTools.Storage
					.getSharedPreEditor(this,
							UConstants.BASE_PREFS_NAME);
			mEditor.putBoolean(UConstants.FIRST_LOGIN, false);
			mEditor.commit();
			
			Intent intent = new Intent(this, IntroduceActivity.class);
			this.startActivity(intent);
			this.finish();
		} else {
			UserInfo mInfo = TongApplication.getMineInfo(this);

			if (mInfo != null) {
				if (mInfo.nickName != null && !mInfo.nickName.equals("")) {
					Intent intent = new Intent(this, MainTabActivity.class);
					this.startActivity(intent);
					this.finish();
				} else {
					Intent intent = new Intent(this,
							RegisterStepTwoActivity.class);
					this.startActivity(intent);
					this.finish();
				}
			} else {
				Intent intent = new Intent(this, SplashActivity.class);
				this.startActivity(intent);
				this.finish();
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		this.bus.register(this);
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
	}
}
