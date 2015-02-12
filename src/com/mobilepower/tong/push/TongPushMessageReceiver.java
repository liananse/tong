package com.mobilepower.tong.push;

import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;

import com.baidu.frontia.api.FrontiaPushMessageReceiver;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mobilepower.tong.R;
import com.mobilepower.tong.model.PushMessageInfo;
import com.mobilepower.tong.ui.activity.MainTabActivity;
import com.mobilepower.tong.utils.UConstants;
import com.mobilepower.tong.utils.UTools;

public class TongPushMessageReceiver extends FrontiaPushMessageReceiver {

	/**
	 * 调用 PushManager.startWork 后,sdk 将对 push server 发起绑定请求,这个过程是异步 的。绑定请求的结果通过
	 * onBind 返回。
	 */
	@Override
	public void onBind(Context context, int errorCode, String appid,
			String userId, String channelId, String requestId) {
		// 绑定成功，设置已绑定flag，可以有效的减少不必要的绑定请求
		if (errorCode == 0) {
			TongPushUtils.setBind(context, true);
		}
		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		SharedPreferences.Editor mEditor = UTools.Storage.getSharedPreEditor(
				context, UConstants.BASE_PREFS_NAME);
		mEditor.putString(UConstants.BAIDU_USER_ID, userId);
		mEditor.putString(UConstants.BAIDU_CHANNEL_ID, channelId);
		mEditor.commit();
	}

	@Override
	public void onDelTags(Context context, int errorCode,
			List<String> sucessTags, List<String> failTags, String requestId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onListTags(Context context, int errorCode, List<String> tags,
			String requestId) {
		// TODO Auto-generated method stub

	}

	/**
	 * 接收透传消息
	 */
	@Override
	public void onMessage(Context context, String message,
			String customContentString) {
		// TODO Auto-generated method stub
		System.out.println("message " + message + " customString "
				+ customContentString);

		Gson gson = new Gson();

		try {
			PushMessageInfo mInfo = gson.fromJson(message,
					PushMessageInfo.class);

			if (mInfo != null) {
				NotificationCompat.Builder mBuilder;
				mBuilder = new NotificationCompat.Builder(context)
						.setSmallIcon(R.drawable.ic_launcher)
						.setContentTitle(mInfo.title)
						.setContentText(mInfo.description)
						.setDefaults(Notification.DEFAULT_ALL)
						.setAutoCancel(true);
				// Use Notifiaction.Default_all (sound light and so on)
				// Creates an explicit intent for an Activity in your app
				Intent resultIntent = new Intent(context, MainTabActivity.class);
				resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				resultIntent.putExtra(UConstants.FROM_WHERE,
						UConstants.FROM_NOTIFICATION);
				PendingIntent resultPendingIntent = PendingIntent.getActivity(
						context, 0, resultIntent,
						PendingIntent.FLAG_UPDATE_CURRENT);

				mBuilder.setContentIntent(resultPendingIntent);
				NotificationManager mNotificationManager = (NotificationManager) context
						.getSystemService(Context.NOTIFICATION_SERVICE);
				// mId allows you to update the notification later on.
				mNotificationManager.notify(1, mBuilder.build());
			}
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 接收通知点击的函数。注:推送通知被用户点击前,应用无法通过接口获取通知的 内容。
	 */
	@Override
	public void onNotificationClicked(Context context, String title,
			String description, String customContentString) {
		// TODO Auto-generated method stub
		System.out.println("onNotification " + title + " " + description + " "
				+ customContentString);
	}

	@Override
	public void onSetTags(Context context, int errorCode,
			List<String> sucessTags, List<String> failTags, String requestId) {
		// TODO Auto-generated method stub

	}

	/**
	 * PushManager.stopWork() 的回调函数
	 */
	@Override
	public void onUnbind(Context context, int errorCode, String requestId) {
		// TODO Auto-generated method stub
		// 解绑定成功，设置未绑定flag，
		if (errorCode == 0) {
			TongPushUtils.setBind(context, false);
		}
	}

}
