package com.mobilepower.tong.ui.fragment;

import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.mobilepower.tong.R;
import com.mobilepower.tong.TongApplication;
import com.mobilepower.tong.ui.event.ExitEvent;

public class ExitDialog extends DialogFragment implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		int style = DialogFragment.STYLE_NO_TITLE, theme = R.style.dialog;
		setStyle(style, theme);
		this.setCancelable(false);
	}

	private View mCancel;
	private View mOk;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View mView = inflater.inflate(R.layout.dialog_tips_cancel_ok, null);

		mCancel = mView.findViewById(R.id.cancel);
		mOk = mView.findViewById(R.id.sure);

		mCancel.setOnClickListener(this);
		mOk.setOnClickListener(this);

		this.getDialog().setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					ExitDialog.this.dismiss();
					return true;
				} else {
					return false;
				}
			}
		});
		return mView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == mCancel) {
			this.dismiss();
		} else if (v == mOk) {
			TongApplication.getBus().post(new ExitEvent());
			this.dismiss();
		}
	}
}