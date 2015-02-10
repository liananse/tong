package com.mobilepower.tong.model;

import java.io.Serializable;

public class BuyListModel implements Serializable{
	public String addTime;
	public String checkHistoryId;
	public double cost;
	public String id;
	/**
	 * 1 iPhone5 2 iPhone4 3 android
	 */
	public int lineType;
	public int shopId;
	public String sortTime;
	public int status;
	public String terminal;
	/**
	 * 0 充电宝 1 线
	 */
	public int type;
	public String updateTime;
	public String userId;
	public String serverDate;
	public ShopInfo shopModel;
}
