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
package com.mobilepower.tong.ui.controller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.mobilepower.tong.TongApplication;
import com.mobilepower.tong.ui.view.RegisterStepOneView;
import com.squareup.otto.Bus;

public class RegisterStepOneViewController implements BaseViewController{

	private Bus bus;
	private final Context mContext;
	private final RegisterStepOneView mView;
	
	public RegisterStepOneViewController(Context paramContext) {
		bus = TongApplication.getBus();
		mContext = paramContext;
		mView = new RegisterStepOneView(mContext);
	}
	
	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return mView;
	}

	@Override
	public void onHide(ViewGroup paramViewGroup) {
		// TODO Auto-generated method stub
		this.bus.unregister(this);
	}

	@Override
	public void onShow(ViewGroup paramViewGroup) {
		// TODO Auto-generated method stub
		this.bus.register(this);
	}

}
