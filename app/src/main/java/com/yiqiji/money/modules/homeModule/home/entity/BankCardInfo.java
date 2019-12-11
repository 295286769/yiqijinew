package com.yiqiji.money.modules.homeModule.home.entity;

import java.io.Serializable;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.yiqiji.money.modules.common.entity.JsonData;

public class BankCardInfo extends JsonData implements Parcelable, Serializable {
    private static final long serialVersionUID = 1L;

    public String bankid; // 银行卡保存的id
    public String bankno; // 银行卡号
    public String bank; // 银行名称
    public String banktype; // 银行卡类型，1为支付卡，2为提现+支付
    public String banksid; // 银行所属的类型
    public String canquickpay; // 是否支持快捷支付
    public String daypaydesc;
    public String bankmobile; // 银行预留手机号
    public String canquickpayfp; // 是否支持fp支付
    public String banklogourl;
    public List<BankCardInfo> list;

    private BankCardInfo() {
        this.bankid = null;
        this.bankno = null;
        this.bank = null;
        this.banktype = null;
        this.banksid = null;
        this.canquickpay = null;
        this.daypaydesc = null;
        this.bankmobile = null;
        this.canquickpayfp = null;
    }

    public BankCardInfo(Parcel source) {
        this.bankid = source.readString();
        this.bankno = source.readString();
        this.bank = source.readString();
        this.banktype = source.readString();
        this.banksid = source.readString();
        this.canquickpay = source.readString();
        this.daypaydesc = source.readString();
        this.bankmobile = source.readString();
        this.canquickpayfp = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bankid);
        dest.writeString(bankno);
        dest.writeString(bank);
        dest.writeString(banktype);
        dest.writeString(banksid);
        dest.writeString(canquickpay);
        dest.writeString(daypaydesc);
        dest.writeString(bankmobile);
        dest.writeString(canquickpayfp);
    }

    public final static Creator<BankCardInfo> CREATOR = new Creator<BankCardInfo>() {

        @Override
        public BankCardInfo [] newArray(int size) {
            return new BankCardInfo[size];
        }

        @Override
        public BankCardInfo createFromParcel(Parcel source) {
            return new BankCardInfo(source);
        }
    };

    public List<BankCardInfo> getList() {
		return list;
	}

	public void setList(List<BankCardInfo> list) {
		this.list = list;
	}

	@Override
    public String toString() {
    	// TODO Auto-generated method stub
    	return "bandId:"+bankid+"/bankno:"+bankno+"/bank:"+bank+"/banktype:"+banktype;
    }
}
