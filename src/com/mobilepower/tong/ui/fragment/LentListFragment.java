package com.mobilepower.tong.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mobilepower.tong.R;
import com.mobilepower.tong.http.HHttpDataLoader;
import com.mobilepower.tong.http.HHttpDataLoader.HDataListener;
import com.mobilepower.tong.model.BaseInfo;
import com.mobilepower.tong.model.TongInfo;
import com.mobilepower.tong.ui.adapter.TongListAdapter;
import com.mobilepower.tong.ui.view.XListView;
import com.mobilepower.tong.ui.view.XListView.IXListViewListener;
import com.mobilepower.tong.utils.UConfig;
import com.mobilepower.tong.utils.UConstants;
import com.mobilepower.tong.utils.UToast;

public class LentListFragment extends Fragment implements IXListViewListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mAdapter = new TongListAdapter(getActivity());
		mAdapter.setFromWhere("lent");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View mView = inflater.inflate(R.layout.fragment_lent, null);
		initView(mView);
		getHistoryList(true);
		return mView;
	}

	private XListView mListView;
	private TongListAdapter mAdapter;

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

		mEmptyImage.setImageResource(R.drawable.return_empty);
		mTips1.setText("你还没有归还记录～");
		mTips2.setText("主人如果借了充电宝，记得及时归还哟！");
		mEmptyBtn.setText("去归还");
		mEmptyBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				returnBtnMethod();
			}
		});

		mListView.setAdapter(mAdapter);
	}
	
	private ReturnProcessDialog mReturnProcessDialog;

	private void returnBtnMethod() {

		if (mReturnProcessDialog == null) {
			mReturnProcessDialog = new ReturnProcessDialog();
		}

		FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

		if (!mReturnProcessDialog.isAdded()) {
			mReturnProcessDialog.show(ft, "return_process");
		}

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
		params.put("type", "2");

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
									LentListFragment.this.sortTime = mResultModel.sortTime;

									if (mResultModel.data != null
											&& mResultModel.data.size() > 0) {
										if (LentListFragment.this.isRefresh) {
											mAdapter.refreshData(mResultModel.data);
										} else {
											mAdapter.addData(mResultModel.data);
										}
										mListView.removeFooterView(mEmptyView);
										mListView.setPullLoadEnable(true);
									} else {

										if (LentListFragment.this.isRefresh) {
											mAdapter.refreshData(new ArrayList<TongInfo>());

											mListView
													.removeFooterView(mEmptyView);
											mListView.addFooterView(mEmptyView);

											mListView.setPullLoadEnable(false);
										} else {

											if (LentListFragment.this.isAdded()) {
												UToast.showShortToast(
														getActivity(),
														getResources()
																.getString(
																		R.string.shop_page_no_more_data));
											}
										}
									}
								} else {

									if (LentListFragment.this.isRefresh) {
										mAdapter.refreshData(new ArrayList<TongInfo>());

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
		public List<TongInfo> data;
	}

}
