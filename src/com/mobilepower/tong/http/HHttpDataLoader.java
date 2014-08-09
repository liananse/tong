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
package com.mobilepower.tong.http;

import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.mobilepower.tong.utils.UConstants;

public class HHttpDataLoader {

	/**
	 * @author zenghui.wang 2013-6-28
	 */
	class HDataLoaderTask extends AsyncTask<String, Integer, String> {

		private String mUrl;
		private Map<String, String> mParams;
		Context mContext;
		HDataListener mHDataListener;

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			mUrl = params[0];
			String method = params[1];

			if (TextUtils.isEmpty(method)) {
				method = "GET";
			}

			try {
				String source = "";
				if (method.equals("POST")) {
					source = HHttpUtility.post(mContext, mUrl, this.mParams);
				} else {
					source = HHttpUtility.get(mContext, mUrl, this.mParams);
				}
				return source;
			} catch (Exception e) {
				e.printStackTrace();
			}

			return "";
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (this.mHDataListener != null) {
				if (result.equals("ConnecTimeoutException")) {
					this.mHDataListener.onConnectTimeoutException(result);
				} else if (result.equals("SocketTimeoutException")) {
					this.mHDataListener.onSocketTimeoutException(result);
				} else if (result != null && !result.equals("")) {
					this.mHDataListener.onFinish(result);

					if (UConstants.isDataLoaderDebug) {
						System.out.println(mUrl + ":" + result);
					}
				} else {
					mHDataListener.onFail("Failure");
				}
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		public HDataLoaderTask(Context context, HDataListener dataListener,
				Map<String, String> params) {
			this.mContext = context;
			this.mHDataListener = dataListener;
			this.mParams = params;
		}
	}

	public void getData(String url, Map<String, String> params,
			Context context, HDataListener listener) {
		HDataLoaderTask task = new HDataLoaderTask(context, listener, params);
		task.execute(url, "GET");
	}

	public void postData(String url, Map<String, String> params,
			Context context, HDataListener listener) {
		HDataLoaderTask task = new HDataLoaderTask(context, listener, params);
		task.execute(url, "POST");
	}

	public interface HDataListener {

		/**
		 * @author zenghui.wang
		 * 
		 *         2013-1-25
		 */
		public void onFinish(String source);

		/**
		 * @author zenghui.wang
		 * 
		 *         2013-1-25
		 */
		public void onFail(String msg);

		public void onSocketTimeoutException(String msg);

		public void onConnectTimeoutException(String msg);
	}

}
