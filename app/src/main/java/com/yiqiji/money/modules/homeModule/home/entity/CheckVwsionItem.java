package com.yiqiji.money.modules.homeModule.home.entity;

public class CheckVwsionItem {

	private String version;// 服务端最新版本
	private String isforce;// 是否强制更新:1.是，0.否,
	private String uptext; // 更新内容:一起记2.0全新发布",
	private String downurl; //

	public String getDownurl() {
		return downurl;
	}

	public void setDownurl(String downurl) {
		this.downurl = downurl;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getIsforce() {
		return isforce;
	}

	public void setIsforce(String isforce) {
		this.isforce = isforce;
	}

	public String getUptext() {
		return uptext;
	}

	public void setUptext(String uptext) {
		this.uptext = uptext;
	}

}
