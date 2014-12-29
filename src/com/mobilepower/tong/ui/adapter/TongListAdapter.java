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
import com.mobilepower.tong.TongApplication;
import com.mobilepower.tong.model.TongInfo;
import com.mobilepower.tong.ui.event.BuyTongEvent;
import com.mobilepower.tong.utils.UTimeUtils;

public class TongListAdapter extends BaseAdapter {

	List<TongInfo> mTongList;

	private Context mContext;
	private LayoutInflater mInflater;

	private String fromWhere = "";

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

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final TongInfo mModel = mTongList.get(position);

		if (fromWhere.equals("borrow")) {
			holder.mTongImage.setImageResource(R.drawable.icon_borrow_press);
			holder.mMoneyV.setVisibility(View.VISIBLE);
			holder.mBuyV.setVisibility(View.VISIBLE);
			holder.mMoneyT.setText("￥" + mModel.overtimeMoney);
		} else if (fromWhere.equals("lent")) {
			holder.mTongImage.setImageResource(R.drawable.icon_lent_press);
			holder.mMoneyV.setVisibility(View.GONE);
			holder.mBuyV.setVisibility(View.GONE);
		}
		holder.mTongFrom.setText("编号: " + mModel.deviceTerminal);
		holder.mTongTime.setText("时间: " + mModel.updateTime);
		holder.mTongLocation.setText("地点: " + mModel.shopModel.address
				+ mModel.shopModel.address);

		// holder.mTimeTips.setText(UTimeUtils.computeHowLongLeft(mContext,
		// Long.parseLong("1419842888964")));
		holder.mTimeTips.setText(UTimeUtils.computeHowLongAgo(mContext,
				Long.parseLong(mModel.expires)));

		System.out.println("current time " + System.currentTimeMillis());

		// holder.mTongLocation.setText(UTimeUtils.computeHowLongLeft(mContext,
		// Long.parseLong(mModel.expires)));

		// holder.mTongItem.setVisibility(View.VISIBLE);
		// if (mModel.type == 1) {
		// holder.mTongFrom.setText("从 " + mModel.shopModel.name + " 借入");
		// holder.mTongImage.setImageResource(R.drawable.icon_borrow_press);
		// } else if (mModel.type == 2) {
		// holder.mTongFrom.setText("在 " + mModel.shopModel.name + " 归还");
		// holder.mTongImage.setImageResource(R.drawable.icon_lent_press);
		// } else if (mModel.type == 3) {
		// // 转借他人
		// holder.mTongFrom.setText("转借他人");
		// holder.mTongImage.setImageResource(R.drawable.icon_lent_press);
		// } else if (mModel.type == -1) {
		// // 借入失败
		// holder.mTongFrom.setText("借入失败");
		// holder.mTongImage.setImageResource(R.drawable.icon_lent_press);
		// } else if (mModel.type == -2) {
		// // 未知状态
		// holder.mTongFrom.setText("未知状态");
		// holder.mTongImage.setImageResource(R.drawable.icon_lent_press);
		// } else {
		// holder.mTongItem.setVisibility(View.GONE);
		// }

		holder.mBuyT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TongApplication.getBus().post(new BuyTongEvent(mModel));
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

}
