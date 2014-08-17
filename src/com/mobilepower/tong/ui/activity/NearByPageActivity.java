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

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import com.mobilepower.tong.R;
import com.mobilepower.tong.TongApplication;
import com.mobilepower.tong.model.UserInfo;
import com.mobilepower.tong.ui.adapter.NearbyUserAdapter;
import com.mobilepower.tong.ui.view.XListView;
import com.squareup.otto.Bus;

public class NearByPageActivity extends BaseActivity {

	private Bus bus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.nearby_page_activity);
		bus = TongApplication.getBus();
		
		initView();
		initData();
	}
	
	private XListView mListView;
	private NearbyUserAdapter mAdapter;
	private void initView() {
		mListView = (XListView) findViewById(R.id.nearby_list);
		
		mAdapter = new NearbyUserAdapter(this);
		mListView.setAdapter(mAdapter);
	}
	
	
	private void initData() {
		List<UserInfo> mNearbyList = new ArrayList<UserInfo>();
		
		for (int i = 0; i < 10; i++) {
			UserInfo mModel = new UserInfo();
			mModel.avatar = "http://ww2.sinaimg.cn/bmiddle/684ff39bgw1ejfep2t9bcj20sg0ixq50.jpg";
			mModel.nickName = "zenghui.wang";
			mModel.distance = "1km";
			mModel.resume = "i want you";
			
			mNearbyList.add(mModel);
		}
		
		mAdapter.refreshData(mNearbyList);
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
