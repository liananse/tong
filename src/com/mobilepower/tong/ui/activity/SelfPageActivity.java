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

import java.util.HashMap;
import java.util.Map;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.NotificationCompat;
import com.easemob.util.EasyUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mobilepower.tong.R;
import com.mobilepower.tong.TongApplication;
import com.mobilepower.tong.http.HHttpDataLoader;
import com.mobilepower.tong.http.HHttpDataLoader.HDataListener;
import com.mobilepower.tong.hx.utils.CommonUtils;
import com.mobilepower.tong.model.BaseInfo;
import com.mobilepower.tong.model.UserInfo;
import com.mobilepower.tong.ui.event.ExitEvent;
import com.mobilepower.tong.ui.fragment.ExitDialog;
import com.mobilepower.tong.utils.UConfig;
import com.mobilepower.tong.utils.UConstants;
import com.mobilepower.tong.utils.URequestCodes;
import com.mobilepower.tong.utils.UTools;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class SelfPageActivity extends BaseActivity implements OnClickListener {

	private Bus bus;

	private NewMessageBroadcastReceiver msgReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.self_page_activity);
		bus = TongApplication.getBus();
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		initView();
		initData();
		// 注册一个接收消息的BroadcastReceiver
		msgReceiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager
				.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(3);
		registerReceiver(msgReceiver, intentFilter);
		getUserInfo();
	}

	private View mSelfInfo;
	private ImageView mAvatarView;
	private TextView mNickName;
	private View mRechargeBtn;
	private TextView mSelfResume;
	private TextView mSelfYue;
	private TextView mSelfYa;
	// 按钮 选项
	private View mChatBtn;
	private View mFriendBtn;
	private View mScoreDesBtn;
	private View mAboutBtn;
	private View mInfoBtn;
	private View mSelfEditBtn;
	private View mExitBtn;
	private CheckBox mWantPush;
	private CheckBox mNearbyUser;

	private TextView mUnReadMsgCount;
	private Drawable maleDrawable;
	private Drawable femaleDrawable;
	private void initView() {
		maleDrawable = getResources().getDrawable(R.drawable.flag_gender_male);
		maleDrawable.setBounds(0, 0, maleDrawable.getMinimumWidth(),
				maleDrawable.getMinimumHeight());

		femaleDrawable = getResources().getDrawable(
				R.drawable.flag_gender_female);
		femaleDrawable.setBounds(0, 0, femaleDrawable.getMinimumWidth(),
				femaleDrawable.getMinimumHeight());
		mSelfInfo = findViewById(R.id.self_page_info);
		mSelfInfo.setOnClickListener(this);
		mAvatarView = (ImageView) findViewById(R.id.self_page_avatar);
		mAvatarView.setOnClickListener(this);
		mNickName = (TextView) findViewById(R.id.self_info_nickname);
		mSelfResume = (TextView) findViewById(R.id.self_info_resume);
		mSelfYue = (TextView) findViewById(R.id.self_info_score);
		mSelfYa = (TextView) findViewById(R.id.self_info_ya);
		mRechargeBtn = findViewById(R.id.self_page_charge_btn);
		mChatBtn = findViewById(R.id.self_page_chat_btn);
		mFriendBtn = findViewById(R.id.self_page_friend_btn);
		mScoreDesBtn = findViewById(R.id.self_page_score_des_btn);
		mAboutBtn = findViewById(R.id.self_page_about_btn);
		mInfoBtn = findViewById(R.id.self_page_info_btn);
		mSelfEditBtn = findViewById(R.id.self_page_edit_btn);
		mExitBtn = findViewById(R.id.self_page_exit_btn);
		mWantPush = (CheckBox) findViewById(R.id.setting_want_info_push);
		mNearbyUser = (CheckBox) findViewById(R.id.setting_nearby_user);

		mUnReadMsgCount = (TextView) findViewById(R.id.unread_msg_number);

		mRechargeBtn.setOnClickListener(this);
		mChatBtn.setOnClickListener(this);
		mFriendBtn.setOnClickListener(this);
		mScoreDesBtn.setOnClickListener(this);
		mAboutBtn.setOnClickListener(this);
		mInfoBtn.setOnClickListener(this);
		mSelfEditBtn.setOnClickListener(this);
		mExitBtn.setOnClickListener(this);
	}

	private void initData() {
		// mAvatarView
		// .setImageUrl("http://ww2.sinaimg.cn/bmiddle/684ff39bgw1ejfep2t9bcj20sg0ixq50.jpg");

		UserInfo mInfo = TongApplication.getMineInfo(this);

		if (mInfo != null) {
			mNickName.setText(mInfo.nickName);
			if (mInfo.sex == 1) {
				mNickName.setCompoundDrawables(null, null, maleDrawable, null);
			} else {
				mNickName
						.setCompoundDrawables(null, null, femaleDrawable, null);
			}
			mNickName.setCompoundDrawablePadding(10);
			
			if (mInfo.resume != null && !mInfo.resume.equals("")) {
				mSelfResume.setVisibility(View.VISIBLE);
				mSelfResume.setText(mInfo.resume);
			} else {
				mSelfResume.setVisibility(View.GONE);
				mSelfResume.setText("");
			}
			mSelfYue.setText(getResources().getString(R.string.self_page_yue,
					mInfo.money));

			mSelfYa.setText(getResources().getString(R.string.self_page_ya,
					mInfo.preMoney));
		}
	}

	private HHttpDataLoader mDataLoader = new HHttpDataLoader();

	private void getUserInfo() {
		Map<String, String> params = new HashMap<String, String>();

		mDataLoader.getData(UConfig.USER_GET_URL, params, this,
				new HDataListener() {

					@Override
					public void onSocketTimeoutException(String msg) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFinish(String source) {
						// TODO Auto-generated method stub
						Gson gson = new Gson();
						try {
							TempModel mResultModel = gson.fromJson(source,
									new TypeToken<TempModel>() {
									}.getType());

							if (mResultModel != null) {
								if (mResultModel.result == UConstants.SUCCESS) {
									UserInfo mSelf = mResultModel.user;

									mNickName.setText(mSelf.nickName);
									mSelfYue.setText(getResources()
											.getString(R.string.self_page_yue,
													mSelf.money));

									mSelf.access_token = UTools.OS
											.getAccessToken(SelfPageActivity.this);
									// 将用户个人信息存数据库
									TongApplication.initMineInfo(
											SelfPageActivity.this, mSelf);

									initData();
								}
							}

						} catch (JsonSyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFail(String msg) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onConnectTimeoutException(String msg) {
						// TODO Auto-generated method stub

					}
				});
	}

	class TempModel extends BaseInfo {
		public UserInfo user;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		this.bus.register(this);
		// initData();
		getUserInfo();
		refresh();
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

		try {
			unregisterReceiver(msgReceiver);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ExitDialog mExitDialog;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == mAvatarView) {
			// Intent intent = new Intent();
			// intent.setClass(this, UserInfoActivity.class);
			// this.startActivity(intent);
		} else if (v == mChatBtn) {
			Intent intent = new Intent();
			intent.setClass(this, ChatActivity.class);
			this.startActivity(intent);
		} else if (v == mFriendBtn) {
			Intent intent = new Intent();
			intent.setClass(this, FriendsActivity.class);
			this.startActivity(intent);
		} else if (v == mScoreDesBtn) {
			Intent intent = new Intent();
			intent.setClass(this, ScoreDescActivity.class);
			this.startActivity(intent);
		} else if (v == mAboutBtn) {
			Intent intent = new Intent();
			intent.setClass(this, AboutActivity.class);
			this.startActivity(intent);
		} else if (v == mInfoBtn) {
			Intent intent = new Intent();
			intent.setClass(this, ChatListActivity.class);
			this.startActivity(intent);
		} else if (v == mExitBtn) {
			if (mExitDialog == null) {
				mExitDialog = new ExitDialog();
			}

			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();

			if (!mExitDialog.isAdded()) {
				mExitDialog.show(ft, "cancel_ok");
			}

		} else if (v == mRechargeBtn) {
			Intent intent = new Intent();
			intent.setClass(this, RechargeActivity.class);
			this.startActivityForResult(intent, URequestCodes.RECHARGE);
		} else if (v == mSelfEditBtn) {
			// Intent intent = new Intent();
			// intent.setClass(this, EditInfoActivity.class);
			// this.startActivityForResult(intent, URequestCodes.EDIT_INFO);
		} else if (v == mSelfInfo) {
			Intent intent = new Intent();
			intent.setClass(this, EditInfoActivity.class);
			this.startActivityForResult(intent, URequestCodes.EDIT_INFO);
		}
	}

	@Subscribe
	public void exit(ExitEvent paramEvent) {
		if (paramEvent != null) {
			logOut();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case URequestCodes.RECHARGE:
			if (resultCode == RESULT_OK) {
				getUserInfo();
			}
			break;
		case URequestCodes.EDIT_INFO:
			if (resultCode == RESULT_OK) {
				initData();
			}
			break;
		default:
			break;
		}

	}

	private void logOut() {
		TongApplication.relogin(this);
	}

	/**
	 * 新消息广播接收者
	 * 
	 * 
	 */
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 主页面收到消息后，主要为了提示未读，实际消息内容需要到chat页面查看

			String from = intent.getStringExtra("from");
			// 消息id
			String msgId = intent.getStringExtra("msgid");
			EMMessage message = EMChatManager.getInstance().getMessage(msgId);
			// 2014-10-22 修复在某些机器上，在聊天页面对方发消息过来时不立即显示内容的bug
			if (ChatActivity.activityInstance != null) {
				if (message.getChatType() == ChatType.GroupChat) {
					if (message.getTo().equals(
							ChatActivity.activityInstance.getToChatUsername()))
						return;
				} else {
					if (from.equals(ChatActivity.activityInstance
							.getToChatUsername()))
						return;
				}
			}

			// 注销广播接收者，否则在ChatActivity中会收到这个广播
			abortBroadcast();

			notifyNewMessage(message);
			// 刷新bottom bar消息未读数
			refresh();

		}
	}

	public void refresh() {
		int count = getUnreadMsgCountTotal();
		if (count > 0) {
			mUnReadMsgCount.setText(String.valueOf(count));
			mUnReadMsgCount.setVisibility(View.VISIBLE);
		} else {
			mUnReadMsgCount.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 获取未读消息数
	 * 
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
		return unreadMsgCountTotal;
	}

	private static final int notifiId = 11;
	protected NotificationManager notificationManager;

	/**
	 * 当应用在前台时，如果当前消息不是属于当前会话，在状态栏提示一下 如果不需要，注释掉即可
	 * 
	 * @param message
	 */
	protected void notifyNewMessage(EMMessage message) {
		// 如果是设置了不提醒只显示数目的群组(这个是app里保存这个数据的，demo里不做判断)
		// 以及设置了setShowNotificationInbackgroup:false(设为false后，后台时sdk也发送广播)
		if (!EasyUtils.isAppRunningForeground(this)) {
			return;
		}

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(getApplicationInfo().icon)
				.setWhen(System.currentTimeMillis()).setAutoCancel(true);

		String ticker = CommonUtils.getMessageDigest(message, this);
		if (message.getType() == Type.TXT)
			ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
		// 设置状态栏提示
		mBuilder.setTicker(message.getFrom() + ": " + ticker);

		Notification notification = mBuilder.build();
		notificationManager.notify(notifiId, notification);
		notificationManager.cancel(notifiId);
	}

}
