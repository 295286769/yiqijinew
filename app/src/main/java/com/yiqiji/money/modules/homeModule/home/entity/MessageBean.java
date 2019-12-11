package com.yiqiji.money.modules.homeModule.home.entity;

import java.util.List;

public class MessageBean {

	private List<DataBean> data;

	public List<DataBean> getData() {
		return data;
	}

	public void setData(List<DataBean> data) {
		this.data = data;
	}

	public static class DataBean {
		private String content;
		private String fromuserid;
		private String icon;
		private String isread;
		private String touserid;
		private String messageid;
		private String type;
		private String ctime;
		private String url;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getFromuserid() {
			return fromuserid;
		}

		public void setFromuserid(String fromuserid) {
			this.fromuserid = fromuserid;
		}

		public String getIcon() {
			return icon;
		}

		public void setIcon(String icon) {
			this.icon = icon;
		}

		public String getIsread() {
			return isread;
		}

		public void setIsread(String isread) {
			this.isread = isread;
		}

		public String getTouserid() {
			return touserid;
		}

		public void setTouserid(String touserid) {
			this.touserid = touserid;
		}

		public String getMessageid() {
			return messageid;
		}

		public void setMessageid(String messageid) {
			this.messageid = messageid;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getCtime() {
			return ctime;
		}

		public void setCtime(String ctime) {
			this.ctime = ctime;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}

}
