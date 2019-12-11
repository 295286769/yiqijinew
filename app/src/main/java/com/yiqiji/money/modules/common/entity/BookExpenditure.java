package com.yiqiji.money.modules.common.entity;

import java.util.List;

/**
 * 
 * @author Administrator
 * 
 */

public class BookExpenditure {
	private String categorydesc;
	private String categoryicon;
	private String categoryid;
	private String categorytitle;
	private String status;
	private String parentid;
	private String isclear;
	private String categorytype;
	private String billtype;// 0:收入1支出
	private List<ChildBean> child;

	public String getBilltype() {
		return billtype;
	}

	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}

	public String getCategorydesc() {
		return categorydesc;
	}

	public void setCategorydesc(String categorydesc) {
		this.categorydesc = categorydesc;
	}

	public String getCategoryicon() {
		return categoryicon;
	}

	public void setCategoryicon(String categoryicon) {
		this.categoryicon = categoryicon;
	}

	public String getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(String categoryid) {
		this.categoryid = categoryid;
	}

	public String getCategorytitle() {
		return categorytitle;
	}

	public void setCategorytitle(String categorytitle) {
		this.categorytitle = categorytitle;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getIsclear() {
		return isclear;
	}

	public void setIsclear(String isclear) {
		this.isclear = isclear;
	}

	public String getCategorytype() {
		return categorytype;
	}

	public void setCategorytype(String categorytype) {
		this.categorytype = categorytype;
	}

	public List<ChildBean> getChild() {
		return child;
	}

	public void setChild(List<ChildBean> child) {
		this.child = child;
	}

	public static class ChildBean {
		private String categorydesc;
		private String categoryicon;
		private String categoryid;
		private String categorytitle;
		private String status;
		private String parentid;
		private String isclear;
		private String categorytype;
		private List<ChildBean> child;

		public List<ChildBean> getChild() {
			return child;
		}

		public void setChild(List<ChildBean> child) {
			this.child = child;
		}

		public String getCategorydesc() {
			return categorydesc;
		}

		public void setCategorydesc(String categorydesc) {
			this.categorydesc = categorydesc;
		}

		public String getCategoryicon() {
			return categoryicon;
		}

		public void setCategoryicon(String categoryicon) {
			this.categoryicon = categoryicon;
		}

		public String getCategoryid() {
			return categoryid;
		}

		public void setCategoryid(String categoryid) {
			this.categoryid = categoryid;
		}

		public String getCategorytitle() {
			return categorytitle;
		}

		public void setCategorytitle(String categorytitle) {
			this.categorytitle = categorytitle;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getParentid() {
			return parentid;
		}

		public void setParentid(String parentid) {
			this.parentid = parentid;
		}

		public String getIsclear() {
			return isclear;
		}

		public void setIsclear(String isclear) {
			this.isclear = isclear;
		}

		public String getCategorytype() {
			return categorytype;
		}

		public void setCategorytype(String categorytype) {
			this.categorytype = categorytype;
		}
	}

}
