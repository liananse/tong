/*
 * Copyright 2014 zenghui.wang.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mobilepower.tong.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class RegisterStepOneView extends ViewGroup{
	
	public RegisterStepOneView(Context context) {
		super(context);
	}

	public RegisterStepOneView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onLayout(boolean arg0, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		int width = r - l;
		int height = b - t;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
	}

}
