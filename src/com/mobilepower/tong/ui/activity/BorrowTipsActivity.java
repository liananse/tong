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
package com.mobilepower.tong.ui.activity;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mobilepower.tong.R;
import com.mobilepower.tong.http.HHttpDataLoader;
import com.mobilepower.tong.http.HHttpDataLoader.HDataListener;
import com.mobilepower.tong.model.BaseInfo;
import com.mobilepower.tong.utils.UConfig;
import com.mobilepower.tong.utils.UConstants;
import com.mobilepower.tong.utils.UIntentKeys;

public class BorrowTipsActivity extends BaseActivity{

	private String mScanTaskId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.borrow_tips_activity);
		
		mScanTaskId = getIntent().getStringExtra(UIntentKeys.SCAN_TASK_ID);
		if (mScanTaskId == null) {
			mScanTaskId = "";
		}
		
	}
	
	private HHttpDataLoader mDataLoader = new HHttpDataLoader();
	
	private boolean isSuccess = false;
	
	private void repeatGetTaskResult() {
		if (isSuccess) {
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("taskId", mScanTaskId);
		
		mDataLoader.postData(UConfig.SCAN_TASK_GET_URL, params, this, new HDataListener() {
			
			@Override
			public void onSocketTimeoutException(String msg) {
				// TODO Auto-generated method stub
				repeatGetTaskResult();
			}
			
			@Override
			public void onFinish(String source) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				
				try {
					TempModel mResult = gson.fromJson(source, TempModel.class);
					
					if (mResult != null) {
						if (mResult.result == UConstants.SUCCESS) {
							isSuccess = true;
							return;
						}
					}
					
				} catch (JsonSyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				repeatGetTaskResult();
			}
			
			@Override
			public void onFail(String msg) {
				// TODO Auto-generated method stub
				repeatGetTaskResult();
			}
			
			@Override
			public void onConnectTimeoutException(String msg) {
				// TODO Auto-generated method stub
				repeatGetTaskResult();
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	class TempModel extends BaseInfo {
		
	}
	
}
