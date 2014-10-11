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
import java.util.List;
import java.util.Map;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.baidu.location.LocationClient;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mobilepower.tong.R;
import com.mobilepower.tong.TongApplication;
import com.mobilepower.tong.http.HHttpDataLoader;
import com.mobilepower.tong.http.HHttpDataLoader.HDataListener;
import com.mobilepower.tong.model.BaseInfo;
import com.mobilepower.tong.model.ShopInfo;
import com.mobilepower.tong.ui.adapter.NearbyShopAdapter;
import com.mobilepower.tong.ui.view.XListView;
import com.mobilepower.tong.ui.view.XListView.IXListViewListener;
import com.mobilepower.tong.utils.UConfig;
import com.mobilepower.tong.utils.UConstants;
import com.mobilepower.tong.utils.UTools;
import com.squareup.otto.Bus;

public class ShopPageActivity extends BaseActivity implements IXListViewListener{

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
		getShopListData(true);
	}

	private XListView mListView;
	private NearbyShopAdapter mAdapter;

	private void initView() {
		mListView = (XListView) findViewById(R.id.shop_list);

		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		
		mAdapter = new NearbyShopAdapter(this);
		mListView.setAdapter(mAdapter);
	}
	
	private HHttpDataLoader mDataLoader = new HHttpDataLoader();
	
	private boolean isRefresh = true;
	private boolean isLoading = false;
	private void getShopListData(boolean isRefresh) {
		
		this.isRefresh = isRefresh;
		this.isLoading = true;
		
		SharedPreferences sp = UTools.Storage.getSharedPreferences(this,
				UConstants.BASE_PREFS_NAME);
		
		String lat = sp.getString(UConstants.LOCATION_LATITUDE, "22.537976");
		String lng = sp.getString(UConstants.LOCATION_LONGITUDE, "113.943617");
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("lat", lat);
		params.put("lng", lng);
		params.put("sortTime", "0");
		mDataLoader.getData(UConfig.SHOP_GET_URL, params, this, new HDataListener() {
			
			@Override
			public void onSocketTimeoutException(String msg) {
				// TODO Auto-generated method stub
				onStopLoad();
			}
			
			@Override
			public void onFinish(String source) {
				// TODO Auto-generated method stub
				onStopLoad();
				Gson gson = new Gson();
				
				try {
					TempModel mResultModel = gson.fromJson(source, TempModel.class);
					
					if (mResultModel != null) {
						if (mResultModel.result == UConstants.SUCCESS) {
							if (mResultModel.data != null && mResultModel.data.size() > 0) {
								if (ShopPageActivity.this.isRefresh) {
									mAdapter.refreshData(mResultModel.data);
								} else {
									mAdapter.addData(mResultModel.data);
								}
							}
						} else {
							
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
				onStopLoad();
			}
			
			@Override
			public void onConnectTimeoutException(String msg) {
				// TODO Auto-generated method stub
				onStopLoad();
			}
		});
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

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (!this.isLoading) {
			getShopListData(true);
		}
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (!this.isLoading) {
			getShopListData(false);
		}
	}
	
	private void onStopLoad()
	{
		mListView.stopLoadMore();
		mListView.stopRefresh();
		
		this.isLoading = false;
	}
	
	class TempModel extends BaseInfo{
		public String sortTime;
		public List<ShopInfo> data;
	}

}
