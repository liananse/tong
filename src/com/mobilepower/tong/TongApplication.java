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
package com.mobilepower.tong;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.baidu.frontia.FrontiaApplication;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.mobilepower.tong.db.DDBOpenHelper;
import com.mobilepower.tong.model.UserInfo;
import com.mobilepower.tong.ui.activity.SplashActivity;
import com.mobilepower.tong.utils.UConstants;
import com.mobilepower.tong.utils.UDataCleanManager;
import com.mobilepower.tong.utils.UTools;
import com.squareup.otto.Bus;

public class TongApplication extends FrontiaApplication {

	private static TongApplication instance;

	public static TongApplication getInstance() {
		return instance;
	}

	public LocationClient mLocationClient;
	public MyLocationListener mMyLocationListener;

	@Override
	public void onCreate() {
		super.onCreate();
		SDKInitializer.initialize(this);
		instance = this;
		// 百度push接口
		FrontiaApplication.initFrontiaApplication(this);
		bus = new Bus();

		initLocation();
	}

	private static UserInfo mine;

	private static Bus bus;

	public static Bus getBus() {

		if (bus == null) {
			bus = new Bus();
		}
		return bus;
	}

	private void initLocation() {
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);

		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
		int span = 1000;
		option.setScanSpan(span);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}

	public static void initMineInfo(Context ctx, UserInfo model) {
		mine = model;
		// 初始化个人信息同时存到数据库中
		if (mine != null) {
			DDBOpenHelper db = DDBOpenHelper.getInstance(ctx);
			db.insertOnlyClassData(model, DDBOpenHelper.USER_TABLE_NAME);
		}
	}

	public static void clearMineInfo() {
		mine = null;
	}

	@SuppressWarnings("unchecked")
	public static UserInfo getMineInfo(Context ctx) {
		// 如果内存中个人信息为空，则从数据库中读取个人信息
		if (mine == null) {
			DDBOpenHelper db = DDBOpenHelper.getInstance(ctx);
			Object o = db.query(DDBOpenHelper.USER_TABLE_NAME, UserInfo.class,
					null, null, null);
			List<UserInfo> mList = (ArrayList<UserInfo>) o;

			if (mList != null && mList.size() > 0) {
				mine = mList.get(0);
			}
		}
		return mine;
	}

	public static void updateMineInfo(String nickname, int age, String resume) {
		if (mine != null) {
			if (nickname != null && !nickname.isEmpty()) {
				mine.nickName = nickname;
			}
			if (age >= 0) {
				mine.age = age;
			}
			if (resume != null && !resume.isEmpty()) {
				mine.resume = resume;
			}
		}
	}

	public static void updateMineMoney(double money) {
		if (mine != null) {
			mine.money = money;
		}
	}

	public static void relogin(Activity act) {
		// SharedPreferences sp = UTools.Storage.getSharedPreferences(act,
		// UConstants.BASE_PREFS_NAME);
		// String xiaomiRegId = sp.getString(UConstants.XIAOMI_REGID, "");

		// 清空数据
		clearMineInfo();
		UDataCleanManager.cleanApplicationData(act);

		// SharedPreferences.Editor mEditor = UTools.Storage.getSharedPreEditor(
		// act, UConstants.BASE_PREFS_NAME);
		// mEditor.putString(UConstants.XIAOMI_REGID, xiaomiRegId);
		// mEditor.commit();

		// 跳转
		UTools.activityhelper.clearAllBut(act);
		Intent intent = new Intent();
		intent.setClass(act, SplashActivity.class);
		act.startActivity(intent);
		act.finish();
	}

	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			SharedPreferences.Editor mEditor = UTools.Storage
					.getSharedPreEditor(getApplicationContext(),
							UConstants.BASE_PREFS_NAME);
			mEditor.putString(UConstants.LOCATION_LATITUDE,
					String.valueOf(location.getLatitude()));
			mEditor.putString(UConstants.LOCATION_LONGITUDE,
					String.valueOf(location.getLongitude()));

			if (location.hasAddr() && location.getCity() != null) {
				mEditor.putString(UConstants.LOCATION_CITY, location.getCity());
			}
			mEditor.commit();

			Map<String, String> params = new HashMap<String, String>();
			params.put("x", String.valueOf(location.getLatitude()));
			params.put("y", String.valueOf(location.getLongitude()));

			// 可以请求店铺地址
			mLocationClient.stop();
		}

	}
}
