package com.mobilepower.tong.ui.fragment;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mobilepower.tong.R;
import com.mobilepower.tong.TongApplication;
import com.mobilepower.tong.dimencode.ScanActivity;
import com.mobilepower.tong.http.HHttpDataLoader;
import com.mobilepower.tong.http.HHttpDataLoader.HDataListener;
import com.mobilepower.tong.model.BaseInfo;
import com.mobilepower.tong.model.BuyModel;
import com.mobilepower.tong.model.TongInfo;
import com.mobilepower.tong.ui.adapter.TongListAdapter;
import com.mobilepower.tong.ui.event.BuySureTongEvent;
import com.mobilepower.tong.ui.event.BuyTongEvent;
import com.mobilepower.tong.ui.view.XListView;
import com.mobilepower.tong.ui.view.XListView.IXListViewListener;
import com.mobilepower.tong.utils.UConfig;
import com.mobilepower.tong.utils.UConstants;
import com.mobilepower.tong.utils.UIntentKeys;
import com.mobilepower.tong.utils.UTimeUtils;
import com.mobilepower.tong.utils.UToast;
import com.mobilepower.tong.utils.UTools;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class BorrowListFragment extends Fragment implements IXListViewListener {

	Bus bus;
	boolean hasShowTwoHoursTips = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mAdapter = new TongListAdapter(getActivity());
		mAdapter.setFromWhere("borrow");
		bus = TongApplication.getBus();

		hasShowTwoHoursTips = false;
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
		View mView = inflater.inflate(R.layout.fragment_borrow, null);
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

		mEmptyImage.setImageResource(R.drawable.borrow_empty);
		mTips1.setText("你还没有借入记录～");
		mTips2.setText("主人快去来电吧借充电宝吧，免费哟！");
		mEmptyBtn.setText("去体验");
		mEmptyBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				borrowBtnMethod();
			}
		});

		mListView.setAdapter(mAdapter);
	}
	
	private BorrowProcessDialog mBorrowProcessDialog;
	
	private void borrowBtnMethod() {

		boolean isFirst = UTools.Storage.getSharedPreferences(getActivity(),
				UConstants.BASE_PREFS_NAME).getBoolean(UConstants.FIRST_BORROW,
				true);

		if (isFirst) {
			SharedPreferences.Editor mEditor = UTools.Storage
					.getSharedPreEditor(getActivity(), UConstants.BASE_PREFS_NAME);
			mEditor.putBoolean(UConstants.FIRST_BORROW, false);
			mEditor.commit();

			if (mBorrowProcessDialog == null) {
				mBorrowProcessDialog = new BorrowProcessDialog();
			}

			FragmentTransaction ft = getActivity().getSupportFragmentManager()
					.beginTransaction();

			if (!mBorrowProcessDialog.isAdded()) {
				mBorrowProcessDialog.show(ft, "borrow_process");
			}
		} else {
			Intent intent = new Intent();
			intent.setClass(getActivity(), ScanActivity.class);
			intent.putExtra(UIntentKeys.FROM_WHERE, "borrow");
			startActivity(intent);
		}

	}

	private CancelOkDialog mCancelOkDialog;

	@Subscribe
	public void BuyClick(BuyTongEvent paramEvent) {
		if (paramEvent != null) {
			if (mCancelOkDialog == null) {
				mCancelOkDialog = new CancelOkDialog();
			}

			Bundle bundle = new Bundle();
			bundle.putSerializable(UIntentKeys.TONG_INOF,
					(Serializable) paramEvent.mInfo);

			FragmentTransaction ft = getActivity().getSupportFragmentManager()
					.beginTransaction();

			if (!mCancelOkDialog.isAdded()) {
				mCancelOkDialog.setArguments(bundle);
				mCancelOkDialog.show(ft, "cancel_ok");
			}
		}
	}

	@Subscribe
	public void BuySureClick(BuySureTongEvent paramEvent) {
		if (paramEvent != null && paramEvent.mInfo != null) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("historyId", paramEvent.mInfo.id);

			mDataLoader.postData(UConfig.BUY_CHONGDIANBAO_URL, params,
					getActivity(), new HDataListener() {

						@Override
						public void onSocketTimeoutException(String msg) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onFinish(String source) {
							// TODO Auto-generated method stub
							Gson gson = new Gson();
							try {
								TempBuyModel mResultModel = gson.fromJson(
										source, TempBuyModel.class);
								if (mResultModel != null) {
									if (mResultModel.result == UConstants.SUCCESS) {
										UToast.showShortToast(getActivity(),
												mResultModel.msg);
									} else {
										UToast.showShortToast(getActivity(),
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
		params.put("type", "1");

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
									BorrowListFragment.this.sortTime = mResultModel.sortTime;

									if (mResultModel.data != null
											&& mResultModel.data.size() > 0) {
										if (BorrowListFragment.this.isRefresh) {
											mAdapter.refreshData(mResultModel.data);
											checkIfShowTwoHoursTips(mResultModel.data);
										} else {
											mAdapter.addData(mResultModel.data);
											checkIfShowTwoHoursTips(mResultModel.data);
										}
										mListView.removeFooterView(mEmptyView);
										mListView.setPullLoadEnable(true);
									} else {

										if (BorrowListFragment.this.isRefresh) {
											mAdapter.refreshData(new ArrayList<TongInfo>());

											mListView
													.removeFooterView(mEmptyView);
											mListView.addFooterView(mEmptyView);

											mListView.setPullLoadEnable(false);
										} else {

											if (BorrowListFragment.this
													.isAdded()) {
												UToast.showShortToast(
														getActivity(),
														getResources()
																.getString(
																		R.string.shop_page_no_more_data));
											}
										}
									}
								} else {

									if (BorrowListFragment.this.isRefresh) {
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

	public void checkIfShowTwoHoursTips(List<TongInfo> mList) {
		if (mList != null && mList.size() > 0) {
			for (int i = 0; i < mList.size(); i++) {
				try {
					Bundle bundle = new Bundle();
					bundle.putSerializable(UIntentKeys.TONG_INOF,
							(Serializable) mList.get(i));

					long freeEndTime = UTimeUtils.stringToLong(
							mList.get(i).addTime, "yyyy-MM-dd HH:mm:ss")
							+ 24
							* 60 * 60 * 1000;

					long diffTime = freeEndTime - System.currentTimeMillis();

					// 免费时间未到并且小于两个小时
					if (diffTime > 0 && diffTime / (60 * 60 * 1000) <= 2) {
						FragmentTransaction ft = getActivity()
								.getSupportFragmentManager().beginTransaction();
						String timeLeftTips = "";
						if (diffTime / (60 * 60 * 1000) == 2) {
							timeLeftTips = "还剩2小时";
						} else if (diffTime / (60 * 60 * 1000) < 2
								&& diffTime / (60 * 60 * 1000) > 1) {
							timeLeftTips = "还剩1小时"
									+ (diffTime / (60 * 1000) - (60 * 60 * 1000))
									+ "分";
						} else if (diffTime / (60 * 60 * 1000) == 1) {
							timeLeftTips = "还剩1小时";
						} else if (diffTime / (60 * 60 * 1000) < 1) {
							timeLeftTips = "还剩" + (diffTime / (60 * 1000))
									+ "分";
						}

						TwoHoursTips mTwoHoursTips = new TwoHoursTips();
						bundle.putString("timeLeftTips", timeLeftTips);
						mTwoHoursTips.setArguments(bundle);
						mTwoHoursTips.show(ft, "two_hour_tips");
					}

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
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
