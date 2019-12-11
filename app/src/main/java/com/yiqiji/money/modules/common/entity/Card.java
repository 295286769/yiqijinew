package com.yiqiji.money.modules.common.entity;

import java.util.List;

public class Card {

	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	private List<Data> data;

	public static class Data {
		private String letters;
		private String color;
		private String bankicon;
		private String bankname;
		private String bankid;
		private String banktype;
		private String ishot;

		public String getLetters() {
			return letters;
		}

		public void setLetters(String letters) {
			this.letters = letters;
		}

		public String getColor() {
			return color;
		}

		public void setColor(String color) {
			this.color = color;
		}

		public String getBankicon() {
			return bankicon;
		}

		public void setBankicon(String bankicon) {
			this.bankicon = bankicon;
		}

		public String getBankname() {
			return bankname;
		}

		public void setBankname(String bankname) {
			this.bankname = bankname;
		}

		public String getBankid() {
			return bankid;
		}

		public void setBankid(String bankid) {
			this.bankid = bankid;
		}

		public String getBanktype() {
			return banktype;
		}

		public void setBanktype(String banktype) {
			this.banktype = banktype;
		}

		public String getIshot() {
			return ishot;
		}

		public void setIshot(String ishot) {
			this.ishot = ishot;
		}
	}

}
