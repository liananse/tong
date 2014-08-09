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
package com.mobilepower.tong.utils;

/**
 * @author zenghui.wang
 * 
 */
public class UException extends Exception {

	private static final long serialVersionUID = 1L;
	private int statusCode = -1;

	public UException(String msg) {
		super(msg);
	}

	public UException(Exception cause) {
		super(cause);
	}

	public UException(String msg, int statusCode) {
		super(msg);
		this.statusCode = statusCode;

	}

	public UException(String msg, Exception cause) {
		super(msg, cause);
	}

	public UException(String msg, Exception cause, int statusCode) {
		super(msg, cause);
		this.statusCode = statusCode;

	}

	public int getStatusCode() {
		return this.statusCode;
	}
}