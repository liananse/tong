package com.mobilepower.tong.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mobilepower.tong.R;
import com.mobilepower.tong.TongApplication;
import com.mobilepower.tong.http.HHttpDataLoader;
import com.mobilepower.tong.http.HHttpDataLoader.HDataListener;
import com.mobilepower.tong.model.BaseInfo;
import com.mobilepower.tong.model.BuyListModel;
import com.mobilepower.tong.ui.adapter.BuyListAdapter;
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
		mAdapter = new BuyListAdapter(getActivity());
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
	private BuyListAdapter mAdapter;

	private View mEmptyView;
	private ImageView mEmptyImage;
	private TextView mTips1;
	private TextView mTips2;
	private TextView mEmptyBtn;

	private void initView(View mView) {
		mListView = (XListView) mView.findViewById(R.id.tong_list);

		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);

		mEmptyView = LayoutInflater.from(getActivity()).inflate(
				R.layout.empty_view, null);
		mEmptyImage = (ImageView) mEmptyView.findViewById(R.id.empty_image);
		mTips1 = (TextView) mEmptyView.findViewById(R.id.tips1);
		mTips2 = (TextView) mEmptyView.findViewById(R.id.tips2);
		mEmptyBtn = (TextView) mEmptyView.findViewById(R.id.empty_btn);

		mEmptyImage.setImageResource(R.drawable.buy_empty);
		mTips1.setText("你还没有购买记录～");
		mTips2.setText("主人忘带线了不着急，买买买！");
		mEmptyBtn.setText("去购买");
		mEmptyBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

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

		mDataLoader.getData(UConfig.BUY_HISTORY_GET_URL, params, getActivity(),
				new HDataListener() {

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
										mListView.removeFooterView(mEmptyView);
										mListView.setPullLoadEnable(true);
									} else {

										if (BuyListFragment.this.isRefresh) {
											mAdapter.refreshData(new ArrayList<BuyListModel>());

											mListView
													.removeFooterView(mEmptyView);
											mListView.addFooterView(mEmptyView);

											mListView.setPullLoadEnable(false);
										} else {

											if (BuyListFragment.this.isAdded()) {
												UToast.showShortToast(
														getActivity(),
														getResources()
																.getString(
																		R.string.shop_page_no_more_data));
											}
										}
									}
								} else {

									if (BuyListFragment.this.isRefresh) {
										mAdapter.refreshData(new ArrayList<BuyListModel>());
										mListView.removeFooterView(mEmptyView);
										mListView.addFooterView(mEmptyView);

										mListView.setPullLoadEnable(false);
									}

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
		public List<BuyListModel> data;
	}
}