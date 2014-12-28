package com.mobilepower.tong.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import com.mobilepower.tong.R;
import com.squareup.picasso.Picasso;

@SuppressLint("DrawAllocation")
public class CustomAvatarView extends ViewGroup {

	/**
	 * 头像
	 */
	private final RoundedImageView mAvatarView;
	/**
	 * 头像边框
	 */
	private final ProgressWheel mAvatarRim;

	public CustomAvatarView(Context context) {
		super(context);
		mAvatarView = new RoundedImageView(context);
//		mAvatarView.setOval(true);
		mAvatarView.setScaleType(ScaleType.CENTER_CROP);
		mAvatarView.setImageResource(R.drawable.mm_trans);
		mAvatarView.setCornerRadius((float)8);

		mAvatarRim = new ProgressWheel(context);
		mAvatarRim.setBarWidth(0);
		mAvatarRim
				.setContourColor(getResources().getColor(R.color.transparent));
		mAvatarRim.setRimColor(getResources().getColor(R.color.blue));
		mAvatarRim.setRimWidth(getResources().getDimensionPixelSize(
				R.dimen.self_page_avatar_rim));
		mAvatarRim.setPaddingBottom(0);
		mAvatarRim.setPaddingLeft(0);
		mAvatarRim.setPaddingRight(0);
		mAvatarRim.setPaddingTop(0);

		addView(mAvatarView);
//		addView(mAvatarRim);
	}

	public CustomAvatarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mAvatarView = new RoundedImageView(context);
//		mAvatarView.setOval(true);
		mAvatarView.setScaleType(ScaleType.CENTER_CROP);
		mAvatarView.setImageResource(R.drawable.mm_trans);
		mAvatarView.setCornerRadius((float)8);

		mAvatarRim = new ProgressWheel(context);
		mAvatarRim.setBarWidth(0);
		mAvatarRim
				.setContourColor(getResources().getColor(R.color.transparent));
		mAvatarRim.setRimColor(getResources().getColor(R.color.blue));
		mAvatarRim.setRimWidth(getResources().getDimensionPixelSize(
				R.dimen.self_page_avatar_rim));
		mAvatarRim.setPaddingBottom(0);
		mAvatarRim.setPaddingLeft(0);
		mAvatarRim.setPaddingRight(0);
		mAvatarRim.setPaddingTop(0);

		addView(mAvatarView);
//		addView(mAvatarRim);
	}

	@Override
	protected void onLayout(boolean arg0, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		int width = r - l;
		int height = b - t;

		int avatarWidht = this.mAvatarView.getMeasuredWidth();
		int avatarHeight = this.mAvatarView.getMeasuredHeight();

		l = (width - avatarWidht) / 2;
		t = (height - avatarHeight) / 2;

		this.mAvatarView.layout(l, t, l + avatarWidht, t + avatarHeight);
		this.mAvatarRim.layout(l, t, l + avatarWidht, t + avatarHeight);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
		int viewHeight = MeasureSpec.getSize(heightMeasureSpec);

		int viewSize;
		if (viewWidth > viewHeight) {
			viewSize = viewHeight;
		} else {
			viewSize = viewWidth;
		}

		this.mAvatarView.measure(
				MeasureSpec.makeMeasureSpec(viewSize, MeasureSpec.EXACTLY),
				MeasureSpec.makeMeasureSpec(viewSize, MeasureSpec.EXACTLY));

		this.mAvatarRim.measure(
				MeasureSpec.makeMeasureSpec(viewSize, MeasureSpec.EXACTLY),
				MeasureSpec.makeMeasureSpec(viewSize, MeasureSpec.EXACTLY));

		LayoutParams params = new LayoutParams(viewSize, viewSize);
		mAvatarRim.setLayoutParams(params);
	}

	public void setImageUrl(String url) {
		Picasso.with(getContext()).load(url)
				.placeholder(R.drawable.mm_trans)
				.error(R.drawable.mm_trans)
				.into(mAvatarView);
	}

}
