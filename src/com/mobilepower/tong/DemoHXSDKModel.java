package com.mobilepower.tong;

import android.content.Context;

import com.mobilepower.tong.hx.model.DefaultHXSDKModel;

public class DemoHXSDKModel extends DefaultHXSDKModel {

	public DemoHXSDKModel(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	// demo will use HuanXin roster
	public boolean getUseHXRoster() {
		// TODO Auto-generated method stub
		return true;
	}

	// demo will switch on debug mode
	public boolean isDebugMode() {
		return true;
	}
}
