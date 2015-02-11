package com.mobilepower.tong.model;

import java.io.Serializable;

public class TongInfo implements Serializable {
	public String addTime;
	public String cdb;
	public String deviceTerminal;
	public String expires;
	public String id;
	/**
	 * 用户留言
	 */
	public String message;
	public String name;
	public double overtimeMoney;
	public double preMoney;
	public String returnTime;
	public int shopId;
	public String sortTime;
	public String sourceUserId;
	/**
	 * 0 表示借入者没有留言 1表示有留言
	 */
	public int status;
	public String taskId;
	public String toUserId;
	/**
	 * 1 借入 2 归还 3 转借别人
	 */
	public int type;
	public String updateTime;
	public String userId;
	public String serverDate;

	public ShopInfo shopModel;
	public UserInfo user;

	public String getCdb() {
		return cdb.replaceAll("^(0+)", "");
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		TongInfo other = (TongInfo) o;
		if (other.id.equals(this.id)) {
			return true;
		} else {
			return false;
		}
	}
	
	
}
