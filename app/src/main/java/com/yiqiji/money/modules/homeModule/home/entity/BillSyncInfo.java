package com.yiqiji.money.modules.homeModule.home.entity;

import java.util.List;

public class BillSyncInfo {
	private int code;
	private String msg;
	private List<DataInfo> data;

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

	public List<DataInfo> getData() {
		return data;
	}

	public void setData(List<DataInfo> data) {
		this.data = data;
	}

	public class DataInfo {
		private String pkid;
		private int billid;

		public String getPkid() {
			return pkid;
		}

		public void setPkid(String pkid) {
			this.pkid = pkid;
		}

		public int getBillid() {
			return billid;
		}

		public void setBillid(int billid) {
			this.billid = billid;
		}
	}
}
