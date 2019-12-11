package com.yiqiji.money.modules.common.entity;

import java.util.List;

/**
 * 用于结婚账本，账本中有子目录
 * 
 * @author Administrator
 * 
 */

public class BookList {

	private String categoryid;
	private String categorytitle;
	private String categorydesc;
	private String categorytype;
	private String categoryicon;
	private String parentid;
	private String status;
	private String isclear;

	private List<ChildBean1> child;

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

	public String getCategorydesc() {
		return categorydesc;
	}

	public void setCategorydesc(String categorydesc) {
		this.categorydesc = categorydesc;
	}

	public String getCategorytype() {
		return categorytype;
	}

	public void setCategorytype(String categorytype) {
		this.categorytype = categorytype;
	}

	public String getCategoryicon() {
		return categoryicon;
	}

	public void setCategoryicon(String categoryicon) {
		this.categoryicon = categoryicon;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsclear() {
		return isclear;
	}

	public void setIsclear(String isclear) {
		this.isclear = isclear;
	}

	public List<ChildBean1> getChild() {
		return child;
	}

	public void setChild(List<ChildBean1> child) {
		this.child = child;
	}

	public static class ChildBean1 {
		private String categoryid;
		private String categorytitle;
		private String categorydesc;
		private String categorytype;
		private String categoryicon;
		private String parentid;
		private String status;
		private String isclear;
		/**
		 * categoryid : 103 categorytitle : 结婚家具 categorydesc : categorytype : 0
		 * categoryicon : parentid : 313 status : 1 isclear : 0
		 */

		private List<ChildBean2> child;

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

		public String getCategorydesc() {
			return categorydesc;
		}

		public void setCategorydesc(String categorydesc) {
			this.categorydesc = categorydesc;
		}

		public String getCategorytype() {
			return categorytype;
		}

		public void setCategorytype(String categorytype) {
			this.categorytype = categorytype;
		}

		public String getCategoryicon() {
			return categoryicon;
		}

		public void setCategoryicon(String categoryicon) {
			this.categoryicon = categoryicon;
		}

		public String getParentid() {
			return parentid;
		}

		public void setParentid(String parentid) {
			this.parentid = parentid;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getIsclear() {
			return isclear;
		}

		public void setIsclear(String isclear) {
			this.isclear = isclear;
		}

		public List<ChildBean2> getChild() {
			return child;
		}

		public void setChild(List<ChildBean2> child) {
			this.child = child;
		}

		public static class ChildBean2 {
			private String categoryid;
			private String categorytitle;
			private String categorydesc;
			private String categorytype;
			private String categoryicon;
			private String parentid;
			private String status;
			private String isclear;

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

			public String getCategorydesc() {
				return categorydesc;
			}

			public void setCategorydesc(String categorydesc) {
				this.categorydesc = categorydesc;
			}

			public String getCategorytype() {
				return categorytype;
			}

			public void setCategorytype(String categorytype) {
				this.categorytype = categorytype;
			}

			public String getCategoryicon() {
				return categoryicon;
			}

			public void setCategoryicon(String categoryicon) {
				this.categoryicon = categoryicon;
			}

			public String getParentid() {
				return parentid;
			}

			public void setParentid(String parentid) {
				this.parentid = parentid;
			}

			public String getStatus() {
				return status;
			}

			public void setStatus(String status) {
				this.status = status;
			}

			public String getIsclear() {
				return isclear;
			}

			public void setIsclear(String isclear) {
				this.isclear = isclear;
			}
		}
	}
}
