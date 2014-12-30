package com.mobilepower.tong.hx.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class PasteEditText extends EditText {
	private Context context;

	public PasteEditText(Context context) {
		super(context);
		this.context = context;
	}

	public PasteEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public PasteEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}
}
