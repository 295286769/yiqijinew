package com.yiqiji.money.modules.homeModule.home.entity;

public class ShorturlInfo {
	private int code;
	private String msg;
	private UrlInfo data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public UrlInfo getData() {
		return data;
	}

	public void setData(UrlInfo data) {
		this.data = data;
	}

	public class UrlInfo {
		private String shorturl;

		public String getShorturl() {
			return shorturl;
		}

		public void setShorturl(String shorturl) {
			this.shorturl = shorturl;
		}

	}
}
