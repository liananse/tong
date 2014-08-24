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

import com.baidu.location.LocationClient;
import com.mobilepower.tong.R;
import com.mobilepower.tong.TongApplication;
import com.mobilepower.tong.model.ShopInfo;
import com.mobilepower.tong.ui.adapter.NearbyShopAdapter;
import com.mobilepower.tong.ui.view.XListView;
import com.squareup.otto.Bus;

public class ShopPageActivity extends BaseActivity {

	private Bus bus;
	LocationClient mLocClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.shop_page_activity);
		bus = TongApplication.getBus();

		initView();

		initLocation();
		initData();
	}

	private XListView mListView;
	private NearbyShopAdapter mAdapter;

	private void initView() {
		mListView = (XListView) findViewById(R.id.shop_list);

		mAdapter = new NearbyShopAdapter(this);
		mListView.setAdapter(mAdapter);
	}

	private void initData() {
		List<ShopInfo> mNearbyList = new ArrayList<ShopInfo>();

		for (int i = 0; i < 10; i++) {
			ShopInfo mModel = new ShopInfo();
			mModel.shopAvatar = "http://ww2.sinaimg.cn/bmiddle/684ff39bgw1ejfep2t9bcj20sg0ixq50.jpg";
			mModel.name = "星巴克咖啡";
			mModel.shopDistance = "1km";
			mModel.address = "同方信息港A座3楼";

			mNearbyList.add(mModel);
		}

		mAdapter.refreshData(mNearbyList);
	}

	private void initLocation() {
		mLocClient = ((TongApplication) getApplication()).mLocationClient;

		if (!mLocClient.isStarted()) {
			mLocClient.start();
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
		if (mLocClient.isStarted()) {
			mLocClient.stop();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.bus.unregister(this);
	}

}
