package com.mobilepower.tong.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilepower.tong.R;
import com.mobilepower.tong.model.BuyListModel;

public class BuyListAdapter extends BaseAdapter {

	List<BuyListModel> mTongList;

	private Context mContext;
	private LayoutInflater mInflater;

	public BuyListAdapter(Context context) {
		this(context, null);
	}

	public BuyListAdapter(Context context, List<BuyListModel> infos) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		if (infos != null) {
			this.mTongList = infos;
		} else {
			this.mTongList = new ArrayList<BuyListModel>();
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

		final BuyListModel mModel = mTongList.get(position);

		holder.mTongImage.setImageResource(R.drawable.icon_borrow_press);
		holder.mMoneyV.setVisibility(View.VISIBLE);
		holder.mTimeTips.setVisibility(View.INVISIBLE);
		holder.mBuyV.setVisibility(View.GONE);
		holder.mTongFrom.setText("编号: " + mModel.terminal);
		holder.mTongTime.setText("时间: " + mModel.updateTime);
		holder.mMoneyT.setText("￥" + mModel.cost);

		holder.mTongLocation.setVisibility(View.GONE);
//		if (mModel.name != null && !mModel.name.equals("")) {
//			holder.mTongLocation.setText("从" + mModel.name + "借得。");
//		} else if (mModel.shopModel != null && mModel.shopModel.address != null
//				&& !mModel.shopModel.address.equals("")) {
//			holder.mTongLocation.setText("地点: " + mModel.shopModel.address
//					+ mModel.shopModel.address);
//		} else {
//
//		}

		holder.mTongItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});

		holder.mLentT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});

		holder.mBuyT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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

	public void refreshData(List<BuyListModel> list) {
		mTongList.clear();
		mTongList.addAll(list);
		notifyDataSetChanged();
	}

	public void addData(List<BuyListModel> list) {
		mTongList.addAll(list);
		notifyDataSetChanged();
	}

}