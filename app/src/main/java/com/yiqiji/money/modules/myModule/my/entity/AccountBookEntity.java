package com.yiqiji.money.modules.myModule.my.entity;


import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dansakai on 2017/5/19.
 */

public class AccountBookEntity implements Parcelable {

    private boolean isEdit = false;
    private String subscribeid;//关注的id(编辑和删除的时候用)
    private String accountbookid;//账本id
    private String accountbooktitle;//账本名称
    private String accountbookicon;//账本icon
    private String userid;//用户id
    private String username;//账本创建者
    private String usericon;//账本创建者头像
    private String deviceid;//设备id
    private String accountbookcate;//账本分类id
    private String accountbooktype;//是否是多人账本
    private String accountbookbudget;//账本预算费用
    private String accountbookstatus;//账本状态
    private String accountbookcount;//账本人数
    private String isclear;//是否需要清算
    private String sorttype;//排序类型：1.按月，0.按天
    private String accountbookctime;//
    private String accountbookutime;//
    private String accountbookltime;//
    private String firsttime;//


    public AccountBookEntity() {
    }

    public String getFirsttime() {
        return firsttime;
    }

    public void setFirsttime(String firsttime) {
        this.firsttime = firsttime;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public String getSubscribeid() {
        return subscribeid;
    }

    public void setSubscribeid(String subscribeid) {
        this.subscribeid = subscribeid;
    }

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

    public String getAccountbookicon() {
        return accountbookicon;
    }

    public void setAccountbookicon(String accountbookicon) {
        this.accountbookicon = accountbookicon;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsericon() {
        return usericon;
    }

    public void setUsericon(String usericon) {
        this.usericon = usericon;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
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

    public String getSorttype() {
        return sorttype;
    }

    public void setSorttype(String sorttype) {
        this.sorttype = sorttype;
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

    public String getAccountbookltime() {
        return accountbookltime;
    }

    public void setAccountbookltime(String accountbookltime) {
        this.accountbookltime = accountbookltime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isEdit ? (byte) 1 : (byte) 0);
        dest.writeString(this.subscribeid);
        dest.writeString(this.accountbookid);
        dest.writeString(this.accountbooktitle);
        dest.writeString(this.accountbookicon);
        dest.writeString(this.userid);
        dest.writeString(this.username);
        dest.writeString(this.usericon);
        dest.writeString(this.deviceid);
        dest.writeString(this.accountbookcate);
        dest.writeString(this.accountbooktype);
        dest.writeString(this.accountbookbudget);
        dest.writeString(this.accountbookstatus);
        dest.writeString(this.accountbookcount);
        dest.writeString(this.isclear);
        dest.writeString(this.sorttype);
        dest.writeString(this.accountbookctime);
        dest.writeString(this.accountbookutime);
        dest.writeString(this.accountbookltime);
    }

    protected AccountBookEntity(Parcel in) {
        this.isEdit = in.readByte() != 0;
        this.subscribeid = in.readString();
        this.accountbookid = in.readString();
        this.accountbooktitle = in.readString();
        this.accountbookicon = in.readString();
        this.userid = in.readString();
        this.username = in.readString();
        this.usericon = in.readString();
        this.deviceid = in.readString();
        this.accountbookcate = in.readString();
        this.accountbooktype = in.readString();
        this.accountbookbudget = in.readString();
        this.accountbookstatus = in.readString();
        this.accountbookcount = in.readString();
        this.isclear = in.readString();
        this.sorttype = in.readString();
        this.accountbookctime = in.readString();
        this.accountbookutime = in.readString();
        this.accountbookltime = in.readString();
    }

    public static final Creator<AccountBookEntity> CREATOR = new Creator<AccountBookEntity>() {
        @Override
        public AccountBookEntity createFromParcel(Parcel source) {
            return new AccountBookEntity(source);
        }

        @Override
        public AccountBookEntity[] newArray(int size) {
            return new AccountBookEntity[size];
        }
    };


    public static List<AccountBookEntity> parceLis(String str) throws JSONException{
        List<AccountBookEntity> bookEntities = new ArrayList<>();
        JSONArray array = new JSONArray(str);
        JSONObject obj = null;
        AccountBookEntity entity = null;
        if (array != null && array.length() > 0) {
            for (int i = 0; i < array.length(); i++) {
                obj = array.optJSONObject(i);
                entity = new AccountBookEntity();
                entity.setEdit(false);
                entity.setSubscribeid(obj.optString("subscribeid"));
                entity.setAccountbookid(obj.optString("accountbookid"));
                entity.setAccountbooktitle(obj.optString("accountbooktitle"));
                entity.setAccountbookicon(obj.optString("accountbookicon"));
                entity.setUserid(obj.optString("userid"));
                entity.setUsericon(obj.optString("usericon"));
                entity.setUsername(obj.optString("username"));
                entity.setDeviceid(obj.optString("deviceid"));
                entity.setAccountbookcate(obj.optString("accountbookcate"));
                entity.setAccountbooktype(obj.optString("accountbooktype"));
                entity.setAccountbookbudget(obj.optString("accountbookbudget"));
                entity.setAccountbookstatus(obj.optString("accountbookstatus"));
                entity.setAccountbookcount(obj.optString("accountbookcount"));
                entity.setIsclear(obj.optString("isclear"));
                entity.setSorttype(obj.optString("sorttype"));
                entity.setAccountbookctime(obj.optString("accountbookctime"));
                entity.setAccountbookutime(obj.optString("accountbookutime"));
                entity.setAccountbookltime(obj.optString("accountbookltime"));
                bookEntities.add(entity);
            }
        }
        return bookEntities;
    }
}
