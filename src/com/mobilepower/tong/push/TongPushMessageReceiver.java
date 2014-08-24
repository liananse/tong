package com.mobilepower.tong.push;

import java.util.List;

import android.content.Context;

import com.baidu.frontia.api.FrontiaPushMessageReceiver;

public class TongPushMessageReceiver extends FrontiaPushMessageReceiver {

	/**
	 * 调用 PushManager.startWork 后,sdk 将对 push server 发起绑定请求,这个过程是异步 的。绑定请求的结果通过
	 * onBind 返回。
	 */
	@Override
	public void onBind(Context context, int errorCode, String appid,
			String userId, String channelId, String requestId) {
		String responseString = "onBind errorCode=" + errorCode + " appid="
				+ appid + " userId=" + userId + " channelId=" + channelId
				+ " requestId=" + requestId;
		
		// 绑定成功，设置已绑定flag，可以有效的减少不必要的绑定请求
        if (errorCode == 0) {
            TongPushUtils.setBind(context, true);
        }
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        System.out.println("onBind" + responseString);
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
		System.out.println("message " + message + " customString " + customContentString);
	}

	/**
	 * 接收通知点击的函数。注:推送通知被用户点击前,应用无法通过接口获取通知的 内容。
	 */
	@Override
	public void onNotificationClicked(Context context, String title,
			String description, String customContentString) {
		// TODO Auto-generated method stub
		System.out.println("onNotification " + title + " " + description + " " + customContentString);
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
