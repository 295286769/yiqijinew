package com.yiqiji.money.modules.myModule.my.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dansakai on 2017/7/4.
 * 选择账本列表
 */

public class BooksEntity implements Parcelable {

    /**
     * accountbook : [{"accountbookid":"42403","accountbooktitle":"你Mook","userid":"0","deviceid":"908b0b9d5bbf463c9bff80158a8a894d","accountbookcate":"2","accountbooktype":"0","accountbookbudget":"0.00","accountbookstatus":"1","accountbookcount":"1","accountbookbgimg":"http://cloud.test.yiqijiba.com/book/25155dda405d4bff9c16475e46fb9df4","isclear":"0","accountbookctime":"1499147029","accountbookutime":"1499156627","accountbookltime":"1499157265","accountbookicon":"http://static.yiqijiba.com/accountbook/category/fm/2@2x.png?20170405","sorttype":1,"isnew":"0"},{"accountbookid":"42414","accountbooktitle":"饭团账本","userid":"0","deviceid":"908b0b9d5bbf463c9bff80158a8a894d","accountbookcate":"7","accountbooktype":"1","accountbookbudget":"0.00","accountbookstatus":"1","accountbookcount":"1","accountbookbgimg":"","isclear":"1","accountbookctime":"1499152491","accountbookutime":"1499152491","accountbookltime":"1499154975","accountbookicon":"http://static.yiqijiba.com/accountbook/category/fm/7@2x.png?20170405","sorttype":1,"isnew":"0"},{"accountbookid":"42402","accountbooktitle":"家庭账本","userid":"0","deviceid":"908b0b9d5bbf463c9bff80158a8a894d","accountbookcate":"11","accountbooktype":"0","accountbookbudget":"0.00","accountbookstatus":"1","accountbookcount":"1","accountbookbgimg":"","isclear":"0","accountbookctime":"1499147005","accountbookutime":"1499147005","accountbookltime":"1499152474","accountbookicon":"http://static.yiqijiba.com/accountbook/category/fm/11@2x.png?20170405","sorttype":1,"isnew":"0"}]
     * code : 0
     */

    private int code;
    private List<AccountbookBean> accountbook;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<AccountbookBean> getAccountbook() {
        return accountbook;
    }

    public void setAccountbook(List<AccountbookBean> accountbook) {
        this.accountbook = accountbook;
    }

    public static class AccountbookBean implements Parcelable {
        /**
         * accountbookid : 42403
         * accountbooktitle : 你Mook
         * userid : 0
         * deviceid : 908b0b9d5bbf463c9bff80158a8a894d
         * accountbookcate : 2
         * accountbooktype : 0
         * accountbookbudget : 0.00
         * accountbookstatus : 1
         * accountbookcount : 1
         * accountbookbgimg : http://cloud.test.yiqijiba.com/book/25155dda405d4bff9c16475e46fb9df4
         * isclear : 0
         * accountbookctime : 1499147029
         * accountbookutime : 1499156627
         * accountbookltime : 1499157265
         * accountbookicon : http://static.yiqijiba.com/accountbook/category/fm/2@2x.png?20170405
         * sorttype : 1
         * isnew : 0
         */

        private String accountbookid;
        private String accountbooktitle;
        private int isCheck = 0;

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

        public int getIsCheck() {
            return isCheck;
        }

        public void setIsCheck(int isCheck) {
            this.isCheck = isCheck;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.accountbookid);
            dest.writeString(this.accountbooktitle);
            dest.writeInt(this.isCheck);
        }

        protected AccountbookBean(Parcel in) {
            this.accountbookid = in.readString();
            this.accountbooktitle = in.readString();
            this.isCheck = in.readInt();
        }

        public static final Creator<AccountbookBean> CREATOR = new Creator<AccountbookBean>() {
            @Override
            public AccountbookBean createFromParcel(Parcel source) {
                return new AccountbookBean(source);
            }

            @Override
            public AccountbookBean[] newArray(int size) {
                return new AccountbookBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeList(this.accountbook);
    }

    protected BooksEntity(Parcel in) {
        this.code = in.readInt();
        this.accountbook = new ArrayList<AccountbookBean>();
        in.readList(this.accountbook, AccountbookBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<BooksEntity> CREATOR = new Parcelable.Creator<BooksEntity>() {
        @Override
        public BooksEntity createFromParcel(Parcel source) {
            return new BooksEntity(source);
        }

        @Override
        public BooksEntity[] newArray(int size) {
            return new BooksEntity[size];
        }
    };
}
