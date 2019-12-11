package com.yiqiji.money.modules.common.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/3/8.
 */
public class UserInfo implements Parcelable {


    private int issetnick;

    public int getIssetnick() {
        return issetnick;
    }

    public void setIssetnick(int issetnick) {
        this.issetnick = issetnick;
    }

    public int getIssetavatar() {
        return issetavatar;
    }

    public void setIssetavatar(int issetavatar) {
        this.issetavatar = issetavatar;
    }

    private int issetavatar;
    private String uid;
    private String mobile;
    private String username;
    private String usericon;
    private String wxid;
    private String wxname;
    private String wbid;
    private String wbname;
    private String qqid;
    private String qqname;
    private int code;

    private String msg;

    protected UserInfo(Parcel in) {
        uid = in.readString();
        mobile = in.readString();
        username = in.readString();
        usericon = in.readString();
        wxid = in.readString();
        wxname = in.readString();
        wbid = in.readString();
        wbname = in.readString();
        qqid = in.readString();
        qqname = in.readString();
        code = in.readInt();
        msg = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getWxid() {
        return wxid;
    }

    public void setWxid(String wxid) {
        this.wxid = wxid;
    }

    public String getWxname() {
        return wxname;
    }

    public void setWxname(String wxname) {
        this.wxname = wxname;
    }

    public String getWbid() {
        return wbid;
    }

    public void setWbid(String wbid) {
        this.wbid = wbid;
    }

    public String getWbname() {
        return wbname;
    }

    public void setWbname(String wbname) {
        this.wbname = wbname;
    }

    public String getQqid() {
        return qqid;
    }

    public void setQqid(String qqid) {
        this.qqid = qqid;
    }

    public String getQqname() {
        return qqname;
    }

    public void setQqname(String qqname) {
        this.qqname = qqname;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(mobile);
        dest.writeString(username);
        dest.writeString(usericon);
        dest.writeString(wxid);
        dest.writeString(wxname);
        dest.writeString(wbid);
        dest.writeString(wbname);
        dest.writeString(qqid);
        dest.writeString(qqname);
        dest.writeInt(code);
        dest.writeString(msg);
    }
}
