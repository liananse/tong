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

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.mobilepower.tong.model.UserInfo;
import com.squareup.otto.Bus;

public class TongApplication extends Application {

	private static TongApplication instance;

	public static TongApplication getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		instance = this;

		bus = new Bus();
	}

	private static UserInfo mine;

	private static Bus bus;

	public static Bus getBus() {

		if (bus == null) {
			bus = new Bus();
		}
		return bus;
	}

	public static void initMineInfo(Context ctx, UserInfo model) {
		mine = model;
		// 初始化个人信息同时存到数据库中
		if (mine != null) {
			// DDBOpenHelper db = DDBOpenHelper.getInstance(ctx);
			// db.insertOnlyClassData(model, DDBOpenHelper.USER_TABLE_NAME);
		}
	}

	public static void clearMineInfo() {
		mine = null;
	}

	@SuppressWarnings("unchecked")
	public static UserInfo getMineInfo(Context ctx) {
		// 如果内存中个人信息为空，则从数据库中读取个人信息
		if (mine == null) {
			// DDBOpenHelper db = DDBOpenHelper.getInstance(ctx);
			// Object o = db.query(DDBOpenHelper.USER_TABLE_NAME,
			// UserInfo.class,
			// null, null, null);
			// List<UserInfo> mList = (ArrayList<UserInfo>) o;
			//
			// if (mList != null && mList.size() > 0) {
			// mine = mList.get(0);
			// }
		}
		return mine;
	}

	public static void relogin(Activity act) {
		// SharedPreferences sp = UTools.Storage.getSharedPreferences(act,
		// UConstants.BASE_PREFS_NAME);
		// String xiaomiRegId = sp.getString(UConstants.XIAOMI_REGID, "");
		//
		// // 清空数据
		// clearMineInfo();
		// UDataCleanManager.cleanApplicationData(act);
		//
		// SharedPreferences.Editor mEditor = UTools.Storage.getSharedPreEditor(
		// act, UConstants.BASE_PREFS_NAME);
		// mEditor.putString(UConstants.XIAOMI_REGID, xiaomiRegId);
		// mEditor.commit();
		//
		// // 跳转
		// UTools.activityhelper.clearAllBut(act);
		// Intent intent = new Intent();
		// intent.setClass(act, LoginAndRegisterActivity.class);
		// act.startActivity(intent);
		// act.finish();
	}
}
