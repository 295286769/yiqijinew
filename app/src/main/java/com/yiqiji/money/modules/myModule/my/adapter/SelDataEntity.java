package com.yiqiji.money.modules.myModule.my.adapter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dansakai on 2017/7/5.
 * 选择时间实体---导出账本
 */

public class SelDataEntity implements Parcelable {
    private String title;//标题
    private String limit;//期限类型
    private int isCheck = 0;//是否选中 1选中 0未选中

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.limit);
        dest.writeInt(this.isCheck);
    }

    public SelDataEntity(String title,String limit,int isCheck) {
        this.title = title;
        this.isCheck = isCheck;
        this.limit = limit;
    }

    protected SelDataEntity(Parcel in) {
        this.title = in.readString();
        this.limit = in.readString();
        this.isCheck = in.readInt();
    }

    public static final Parcelable.Creator<SelDataEntity> CREATOR = new Parcelable.Creator<SelDataEntity>() {
        @Override
        public SelDataEntity createFromParcel(Parcel source) {
            return new SelDataEntity(source);
        }

        @Override
        public SelDataEntity[] newArray(int size) {
            return new SelDataEntity[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public int getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }
}
