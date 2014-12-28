package com.mobilepower.tong.ui.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mobilepower.tong.R;
import com.mobilepower.tong.http.HHttpDataLoader;
import com.mobilepower.tong.http.HHttpDataLoader.HDataListener;
import com.mobilepower.tong.model.BaseInfo;
import com.mobilepower.tong.model.ShopInfo;
import com.mobilepower.tong.model.TongInfo;
import com.mobilepower.tong.ui.adapter.ShopPageMapUserAdapter;
import com.mobilepower.tong.ui.view.XListView;
import com.mobilepower.tong.utils.UConfig;
import com.mobilepower.tong.utils.UConstants;
import com.mobilepower.tong.utils.UIntentKeys;
import com.mobilepower.tong.utils.UToast;

public class ShopPageMapActivity extends BaseActivity implements
		OnClickListener {

	private ShopInfo mInfo;
	BitmapDescriptor mapTag = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_gcoding);

	private ShopPageMapUserAdapter mAdapter;
	
	private XListView mList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_page_map_activity);
		mInfo = (ShopInfo) getIntent().getSerializableExtra(
				UIntentKeys.SHOP_INTO);

		mAdapter = new ShopPageMapUserAdapter(this);
		initActionBar();
		initView();
		initData();
	}

	private View mBackBtn;

	/**
	 * 初始化actionBar
	 */
	private void initActionBar() {
		mBackBtn = findViewById(R.id.back_btn);

		mBackBtn.setOnClickListener(this);
	}

//	private CustomAvatarView mShopAvatar;
	private ImageView mShopAvatar;
	private TextView mShopName;
	private TextView mShopTel;
	private TextView mShopAddress;

	private LinearLayout mShopLayout;
	MapView mMapView;
	BaiduMap mBaiduMap;

	private void initView() {

		mShopAvatar = (ImageView) findViewById(R.id.shop_avatar);
		mShopName = (TextView) findViewById(R.id.shop_name);
		mShopTel = (TextView) findViewById(R.id.shop_tel);
		mShopAddress = (TextView) findViewById(R.id.shop_address);

		mShopLayout = (LinearLayout) findViewById(R.id.shop_page_layout);
		mList = (XListView) findViewById(R.id.user_list);
		
		mList.setPullLoadEnable(false);
		mList.setPullRefreshEnable(false);
		
		mList.setAdapter(mAdapter);
	}

	private void initData() {
		if (mInfo != null) {
			mShopAvatar.setImageResource(getRandomRes());
			mShopName.setText(mInfo.name);
			mShopTel.setText(mInfo.tel);
			mShopAddress.setText(mInfo.address);
			
			LatLng p = new LatLng(mInfo.lat, mInfo.lng);

			// 设置默认中心点，及缩放级别
			BaiduMapOptions mBaiduMapOptions = new BaiduMapOptions()
					.mapStatus(new MapStatus.Builder().zoom(17).target(p).build());
			mBaiduMapOptions.zoomControlsEnabled(false);
			mMapView = new MapView(this, mBaiduMapOptions);

			mShopLayout.addView(mMapView);

			mBaiduMap = mMapView.getMap();
			mBaiduMap.setMaxAndMinZoomLevel(19, 13);
			
			OverlayOptions ooTag = new MarkerOptions().position(p)
					.icon(mapTag).zIndex(9);
			mBaiduMap.addOverlay(ooTag);
		}
		
		getHistoryListData(true);
	}

	private HHttpDataLoader mDataLoader = new HHttpDataLoader();

	private boolean isRefresh = true;
	private boolean isLoading = false;
	private String sortTime = "";
	private void getHistoryListData(boolean isRefresh) {

		if (mInfo == null) {
			return;
		}
		
		this.isRefresh = isRefresh;
		this.isLoading = true;

		Map<String, String> params = new HashMap<String, String>();
		
		if (this.isRefresh) {
			sortTime = "";
		}
		params.put("shopId", mInfo.id + "");
		params.put("sortTime", sortTime);
		mDataLoader.getData(UConfig.CHECK_HISTORY_GET_BY_SHOP, params, this,
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
									TempModel.class);
							if (mResultModel != null) {
								if (mResultModel.result == UConstants.SUCCESS) {
									// 本次返回的sortTime
									ShopPageMapActivity.this.sortTime = mResultModel.sortTime;

									if (mResultModel.data != null
											&& mResultModel.data.size() > 0) {
										if (ShopPageMapActivity.this.isRefresh) {
											mAdapter.refreshData(mResultModel.data);
										} else {
											mAdapter.addData(mResultModel.data);
										}
									} else {
										UToast.showShortToast(
												ShopPageMapActivity.this,
												getResources()
														.getString(
																R.string.shop_page_no_more_data));
									}
								} else {
									UToast.showShortToast(
											ShopPageMapActivity.this,
											mResultModel.msg);
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
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mMapView.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mMapView.onDestroy();
		mMapView = null;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == mBackBtn) {
			this.finish();
		}
	}

	public int getRandomRes() {
		int randomRes = (int) (Math.random() * 4);
		
		int res = R.drawable.icon_bar;
		if (randomRes == 0) {
			res = R.drawable.icon_cinema;
		} else if (randomRes == 1) {
			res = R.drawable.icon_bar;
		} else if (randomRes == 2) {
			res = R.drawable.icon_shop;
		} else if (randomRes == 3) {
			res = R.drawable.icon_coffe;
		}
		
		return res;
	}
	
	class TempModel extends BaseInfo {
		public String sortTime;
		public List<TongInfo> data;
	}

}
