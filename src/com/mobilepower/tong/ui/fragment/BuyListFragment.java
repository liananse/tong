package com.mobilepower.tong.ui.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mobilepower.tong.R;
import com.mobilepower.tong.TongApplication;
import com.mobilepower.tong.http.HHttpDataLoader;
import com.mobilepower.tong.http.HHttpDataLoader.HDataListener;
import com.mobilepower.tong.model.BaseInfo;
import com.mobilepower.tong.model.BuyModel;
import com.mobilepower.tong.model.TongInfo;
import com.mobilepower.tong.ui.adapter.TongListAdapter;
import com.mobilepower.tong.ui.view.XListView;
import com.mobilepower.tong.ui.view.XListView.IXListViewListener;
import com.mobilepower.tong.utils.UConfig;
import com.mobilepower.tong.utils.UConstants;
import com.mobilepower.tong.utils.UToast;
import com.squareup.otto.Bus;

public class BuyListFragment extends Fragment implements IXListViewListener {

	Bus bus;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mAdapter = new TongListAdapter(getActivity());
		mAdapter.setFromWhere("buy");
		bus = TongApplication.getBus();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		this.bus.unregister(this);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		this.bus.register(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View mView = inflater.inflate(R.layout.fragment_buy, null);
		initView(mView);
		getHistoryList(true);
		return mView;
	}

	private XListView mListView;
	private TongListAdapter mAdapter;

	private void initView(View mView) {
		mListView = (XListView) mView.findViewById(R.id.tong_list);

		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);

		mListView.setAdapter(mAdapter);
	}

	private HHttpDataLoader mDataLoader = new HHttpDataLoader();

	private boolean isRefresh = true;
	private boolean isLoading = false;

	private String sortTime = "";

	private void getHistoryList(boolean isRefresh) {
		this.isRefresh = isRefresh;
		this.isLoading = true;

		Map<String, String> params = new HashMap<String, String>();

		if (isRefresh) {
			sortTime = "";
		}

		params.put("sortTime", sortTime);
		params.put("type", "3");

		mDataLoader.getData(UConfig.CHECK_HISTORY_LIST_URL, params,
				getActivity(), new HDataListener() {

					@Override
					public void onSocketTimeoutException(String msg) {
						// TODO Auto-generated method stub
						onStopLoad();
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
									BuyListFragment.this.sortTime = mResultModel.sortTime;

									if (mResultModel.data != null
											&& mResultModel.data.size() > 0) {
										if (BuyListFragment.this.isRefresh) {
											mAdapter.refreshData(mResultModel.data);
										} else {
											mAdapter.addData(mResultModel.data);
										}
									} else {
										UToast.showShortToast(
												getActivity(),
												getResources()
														.getString(
																R.string.shop_page_no_more_data));
									}
								} else {
									UToast.showShortToast(getActivity(),
											mResultModel.msg);
								}

							}

						} catch (JsonSyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						onStopLoad();
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

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (!this.isLoading) {
			getHistoryList(true);
		}
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (!this.isLoading) {
			getHistoryList(false);
		}
	}

	private void onStopLoad() {
		mListView.stopLoadMore();
		mListView.stopRefresh();
		this.isLoading = false;
	}

	class TempModel extends BaseInfo {
		public String sortTime;
		public List<TongInfo> data;
	}

	class TempBuyModel extends BaseInfo {
		public BuyModel buyModel;
	}
}