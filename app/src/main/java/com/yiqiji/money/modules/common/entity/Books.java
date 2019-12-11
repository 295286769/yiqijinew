package com.yiqiji.money.modules.common.entity;

import java.util.List;

public class Books {

	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	private List<AccountbookBean> accountbook;

	public List<AccountbookBean> getAccountbook() {
		return accountbook;
	}

	public void setAccountbook(List<AccountbookBean> accountbook) {
		this.accountbook = accountbook;
	}

	public static class AccountbookBean {
		private String accountbookid;
		private String accountbooktitle;
		private String userid;
		private String accountbookcate;
		private String accountbooktype;
		private String accountbookbudget;
		private String accountbookstatus;
		private String accountbookcount;
		private String isclear;
		private String accountbookctime;
		private String accountbookutime;
		private String accountbookicon;
		private String isnew;

		public String getAccountbookid() {
			return accountbookid;
		}

		public void setAccountbookid(String accountbookid) {
			this.accountbookid = accountbookid;
		}

		public String getAccountbooktitle() {
			return accountbooktitle;
		}

		public void setAccountbooktitle(String accountbooktitle) {
			this.accountbooktitle = accountbooktitle;
		}

		public String getUserid() {
			return userid;
		}

		public void setUserid(String userid) {
			this.userid = userid;
		}

		public String getAccountbookcate() {
			return accountbookcate;
		}

		public void setAccountbookcate(String accountbookcate) {
			this.accountbookcate = accountbookcate;
		}

		public String getAccountbooktype() {
			return accountbooktype;
		}

		public void setAccountbooktype(String accountbooktype) {
			this.accountbooktype = accountbooktype;
		}

		public String getAccountbookbudget() {
			return accountbookbudget;
		}

		public void setAccountbookbudget(String accountbookbudget) {
			this.accountbookbudget = accountbookbudget;
		}

		public String getAccountbookstatus() {
			return accountbookstatus;
		}

		public void setAccountbookstatus(String accountbookstatus) {
			this.accountbookstatus = accountbookstatus;
		}

		public String getAccountbookcount() {
			return accountbookcount;
		}

		public void setAccountbookcount(String accountbookcount) {
			this.accountbookcount = accountbookcount;
		}

		public String getIsclear() {
			return isclear;
		}

		public void setIsclear(String isclear) {
			this.isclear = isclear;
		}

		public String getAccountbookctime() {
			return accountbookctime;
		}

		public void setAccountbookctime(String accountbookctime) {
			this.accountbookctime = accountbookctime;
		}

		public String getAccountbookutime() {
			return accountbookutime;
		}

		public void setAccountbookutime(String accountbookutime) {
			this.accountbookutime = accountbookutime;
		}

		public String getAccountbookicon() {
			return accountbookicon;
		}

		public void setAccountbookicon(String accountbookicon) {
			this.accountbookicon = accountbookicon;
		}

		public String getIsnew() {
			return isnew;
		}

		public void setIsnew(String isnew) {
			this.isnew = isnew;
		}
	}

	private CatelistBean catelist;

	public CatelistBean getCatelist() {
		return catelist;
	}

	public void setCatelist(CatelistBean catelist) {
		this.catelist = catelist;
	}

	public static class CatelistBean {

		private List<BookListModel> single;

		private List<BookListModel> multiple;

		public List<BookListModel> getSingle() {
			return single;
		}

		public void setSingle(List<BookListModel> single) {
			this.single = single;
		}

		public List<BookListModel> getMultiple() {
			return multiple;
		}

		public void setMultiple(List<BookListModel> multiple) {
			this.multiple = multiple;
		}

		public static class BookListModel {
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
