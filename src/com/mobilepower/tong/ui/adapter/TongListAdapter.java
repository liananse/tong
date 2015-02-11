package com.mobilepower.tong.ui.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mobilepower.tong.R;
import com.mobilepower.tong.TongApplication;
import com.mobilepower.tong.http.HHttpDataLoader;
import com.mobilepower.tong.http.HHttpDataLoader.HDataListener;
import com.mobilepower.tong.model.BaseInfo;
import com.mobilepower.tong.model.TongInfo;
import com.mobilepower.tong.ui.activity.LentCodeActivity;
import com.mobilepower.tong.ui.activity.MainTabActivity;
import com.mobilepower.tong.ui.event.BuyTongEvent;
import com.mobilepower.tong.ui.fragment.FLoadingProgressBarFragment;
import com.mobilepower.tong.utils.UConfig;
import com.mobilepower.tong.utils.UIntentKeys;
import com.mobilepower.tong.utils.UTimeUtils;

public class TongListAdapter extends BaseAdapter {

	List<TongInfo> mTongList;

	private Context mContext;
	private LayoutInflater mInflater;

	private String fromWhere = "";

	private HHttpDataLoader mDataLoader = new HHttpDataLoader();
	public void setFromWhere(String fromWhere) {
		this.fromWhere = fromWhere;
	}

	public TongListAdapter(Context context) {
		this(context, null);
	}

	public TongListAdapter(Context context, List<TongInfo> infos) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		if (infos != null) {
			this.mTongList = infos;
		} else {
			this.mTongList = new ArrayList<TongInfo>();
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (mTongList != null && mTongList.size() > 0) {
			return mTongList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		if (mTongList != null && mTongList.size() > 0) {
			return mTongList.get(arg0);
		}
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.tong_page_activity_list_item, null);

			holder = new ViewHolder();
			holder.mTongImage = (ImageView) convertView
					.findViewById(R.id.tong_image_view);
			holder.mTongItem = convertView.findViewById(R.id.tong_item);
			holder.mTongTime = (TextView) convertView
					.findViewById(R.id.tong_time);
			holder.mTongFrom = (TextView) convertView
					.findViewById(R.id.tong_from);
			holder.mTongLocation = (TextView) convertView
					.findViewById(R.id.tong_location);

			holder.mMoneyV = convertView.findViewById(R.id.money_v);
			holder.mTimeTips = (TextView) convertView
					.findViewById(R.id.time_tips);
			holder.mMoneyT = (TextView) convertView.findViewById(R.id.money_t);
			holder.mBuyV = convertView.findViewById(R.id.buy_btn_v);
			holder.mBuyT = (TextView) convertView.findViewById(R.id.buy_btn);
			holder.mLentT = (TextView) convertView.findViewById(R.id.lent_btn);
			holder.mRefreshT = (TextView) convertView
					.findViewById(R.id.refresh_status_btn);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final TongInfo mModel = mTongList.get(position);

		if (fromWhere.equals("borrow")) {
			holder.mTongImage.setImageResource(R.drawable.cdb);
			holder.mMoneyV.setVisibility(View.VISIBLE);
			holder.mTimeTips.setVisibility(View.VISIBLE);
			holder.mBuyV.setVisibility(View.VISIBLE);
			holder.mMoneyT.setText("￥" + mModel.overtimeMoney);
			holder.mTongTime.setText("时间: " + mModel.updateTime);
		} else if (fromWhere.equals("lent")) {
			holder.mTongImage.setImageResource(R.drawable.cdb);
			holder.mMoneyV.setVisibility(View.VISIBLE);
			holder.mTimeTips.setVisibility(View.INVISIBLE);
			holder.mMoneyT.setText("￥" + mModel.overtimeMoney);
			holder.mBuyV.setVisibility(View.GONE);
			holder.mTongTime.setText("借入时间: " + mModel.addTime + "\n归还时间: "
					+ mModel.returnTime);
		} else if (fromWhere.equals("lent_activity")) {
			holder.mTongImage.setImageResource(R.drawable.cdb);
			holder.mMoneyV.setVisibility(View.GONE);
			holder.mBuyV.setVisibility(View.GONE);
			holder.mTongTime.setText("时间: " + mModel.updateTime);
		}
		holder.mTongFrom.setText("编号: " + mModel.getCdb());

		if (mModel.name != null && !mModel.name.equals("")) {
			holder.mTongLocation.setText("从" + mModel.name + "借取。");
		} else if (mModel.shopModel != null && mModel.shopModel.address != null
				&& !mModel.shopModel.address.equals("")) {
			if (fromWhere.equals("lent")) {
				if (mModel.toUserId.equals("0")) {
					holder.mTongLocation.setText("归还地点: "
							+ mModel.shopModel.address);
				} else {
					holder.mTongLocation.setText("转借他人：" + mModel.name);
				}
			} else {
				holder.mTongLocation.setText("地点: " + mModel.shopModel.address);
			}
		} else {
			holder.mTongLocation.setText("从机器上借取。");
		}

		if (mModel.expires != null) {
			holder.mTimeTips.setText(UTimeUtils.computeHowLongLeft(mContext,
					Long.parseLong(mModel.expires)));
		}

		holder.mTongItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (fromWhere.equals("lent_activity")) {
					Intent i = new Intent(mContext, LentCodeActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable(UIntentKeys.TONG_INOF,
							(Serializable) mModel);
					i.putExtras(bundle);
					mContext.startActivity(i);
				}
			}
		});

		holder.mLentT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(mContext, LentCodeActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(UIntentKeys.TONG_INOF,
						(Serializable) mModel);
				i.putExtras(bundle);
				mContext.startActivity(i);
			}
		});

		holder.mBuyT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TongApplication.getBus().post(new BuyTongEvent(mModel));
			}
		});
		
		holder.mRefreshT.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final FLoadingProgressBarFragment mLoadingProgressBarFragment = new FLoadingProgressBarFragment();
				FragmentTransaction ft = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
				mLoadingProgressBarFragment.show(ft, "dialog");
				
				Map<String, String> params = new HashMap<String, String>();
				params.put("checkHistoryId", mModel.id);
				mDataLoader.postData(UConfig.CHECK_HISTORY_STATUS, params, mContext, new HDataListener() {
					
					@Override
					public void onSocketTimeoutException(String msg) {
						// TODO Auto-generated method stub
						mLoadingProgressBarFragment.dismiss();
					}
					
					@Override
					public void onFinish(String source) {
						// TODO Auto-generated method stub
						Gson gson = new Gson();
						try {
							TempModel mResultModel = gson.fromJson(source,
									TempModel.class);
							
							if (mResultModel != null && mResultModel.data != null) {
								if (mResultModel.data.type != 1) {
									mTongList.remove(mModel);
									TongListAdapter.this.notifyDataSetChanged();
								} else {
									mModel.expires = mResultModel.data.expires;
									mModel.overtimeMoney = mResultModel.data.overtimeMoney;
									mModel.preMoney = mResultModel.data.preMoney;
									mModel.returnTime = mResultModel.data.returnTime;
									mModel.updateTime = mResultModel.data.updateTime;
									TongListAdapter.this.notifyDataSetChanged();
								}
							}
						} catch (JsonSyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						mLoadingProgressBarFragment.dismiss();
					}
					
					@Override
					public void onFail(String msg) {
						// TODO Auto-generated method stub
						mLoadingProgressBarFragment.dismiss();
					}
					
					@Override
					public void onConnectTimeoutException(String msg) {
						// TODO Auto-generated method stub
						mLoadingProgressBarFragment.dismiss();
					}
				});
			}
		});
		return convertView;
	}

	static class ViewHolder {
		ImageView mTongImage;
		View mTongItem;
		TextView mTongTime;
		TextView mTongFrom;
		TextView mTongLocation;

		View mMoneyV;
		TextView mTimeTips;
		TextView mMoneyT;

		View mBuyV;
		TextView mBuyT;
		TextView mLentT;
		TextView mRefreshT;
	}

	public void refreshData(List<TongInfo> list) {
		mTongList.clear();
		mTongList.addAll(list);
		notifyDataSetChanged();
	}

	public void addData(List<TongInfo> list) {
		mTongList.addAll(list);
		notifyDataSetChanged();
	}
	
	class TempModel extends BaseInfo {
		public String sortTime;
		public TongInfo data;
	}

}
