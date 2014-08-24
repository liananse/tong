package com.mobilepower.tong.push;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

import com.mobilepower.tong.utils.UConstants;
import com.mobilepower.tong.utils.UTools;

public class TongPushUtils {

	// 获取ApiKey
	public static String getMetaValue(Context context, String metaKey) {
		Bundle metaData = null;
		String apiKey = null;
		if (context == null || metaKey == null) {
			return null;
		}
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			if (null != ai) {
				metaData = ai.metaData;
			}
			if (null != metaData) {
				apiKey = metaData.getString(metaKey);
			}
		} catch (NameNotFoundException e) {

		}
		return apiKey;
	}

	// 用share preference来实现是否绑定的开关。在ionBind且成功时设置true，unBind且成功时设置false
	public static boolean hasBind(Context context) {
		SharedPreferences sp = UTools.Storage.getSharedPreferences(context,
				UConstants.BASE_PREFS_NAME);
		String flag = sp.getString(UConstants.PUSH_BIND_FLAG, "");

		if ("ok".equalsIgnoreCase(flag)) {
			return true;
		}
		return false;
	}

	public static void setBind(Context context, boolean flag) {
		String flagStr = "not";
		if (flag) {
			flagStr = "ok";
		}

		SharedPreferences.Editor mEditor = UTools.Storage.getSharedPreEditor(
				context, UConstants.BASE_PREFS_NAME);
		mEditor.putString(UConstants.PUSH_BIND_FLAG, flagStr);
		mEditor.commit();

	}
}
