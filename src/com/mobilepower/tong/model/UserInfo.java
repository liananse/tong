package com.mobilepower.tong.model;

public class UserInfo {
	public String nickName = "";
	public String resume = "";
	public int age = 0;
	public String access_token;
	
	public String lastLoginTime;
	public String lat;
	public String lng;
	public String mobile;
	public int money;
	public String phonecode;
	public String pushToken;
	public String registerTime;
	public int sex;
	public int source;
	public int tokenType;
	public String updateTime;
	public int userType;

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

}
