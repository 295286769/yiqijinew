package com.yiqiji.money.modules.common.entity;

import java.io.Serializable;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 收益明细(账户、余额)
 */
public class MoneyLogInfoEntity extends JsonData implements Parcelable, Serializable {

    private static final long serialVersionUID = 5315249867734817626L;

    public String moneytype; // 资金类型，1为本金，2为收益，3为薪智宝
    public String moneystatus;// 收益状态 1为处理成功，2为待处理，3为已退回
    public String moneyamount;// 金额数量
    public String remark; // 备注
    public String capitalbanlance; // 本金账户余额
    public String interestbalance;// 收益账户余额
    public String xzbbalance; // 薪智宝账户余额，单位元，两位小数点
    public String egoldbalance; // e贝账户余额，单位元，两位小数点
    public String scorebalance; // 积分账户余额，单位元，两位小数点
    public String adddate; // 发生时间
    private List<MoneyLogInfoEntity> list;
    
    private MoneyLogInfoEntity() {
        this.moneytype = null;
        this.moneystatus = null;
        this.moneyamount = null;
        this.remark = null;
        this.capitalbanlance = null;
        this.interestbalance = null;
        this.xzbbalance = null;
        this.egoldbalance = null;
        this.scorebalance = null;
        this.adddate = null;
    }

    public MoneyLogInfoEntity(Parcel source) {
        this.moneytype = source.readString();
        this.moneystatus = source.readString();
        this.moneyamount = source.readString();
        this.remark = source.readString();
        this.capitalbanlance = source.readString();
        this.interestbalance = source.readString();
        this.xzbbalance = source.readString();
        this.egoldbalance = source.readString();
        this.scorebalance = source.readString();
        this.adddate = source.readString();
    }

    public static MoneyLogInfoEntity getInfor(String jsonData) throws JSONException {
        MoneyLogInfoEntity info = new MoneyLogInfoEntity();
        if (jsonData != null) {
            JSONObject jo = new JSONObject(jsonData);
            if (jo.has("moneytype")) {
                info.moneytype = jo.getString("moneytype");
            }
            if (jo.has("moneystatus")) {
                info.moneystatus = jo.getString("moneystatus");
            }
            if (jo.has("moneyamount")) {
                info.moneyamount = jo.getString("moneyamount");
            }
            if (jo.has("remark")) {
                info.remark = jo.getString("remark");
            }
            if (jo.has("capitalbanlance")) {
                info.capitalbanlance = jo.getString("capitalbanlance");
            }
            if (jo.has("interestbalance")) {
                info.interestbalance = jo.getString("interestbalance");
            }
            if (jo.has("xzbbalance")) {
                info.xzbbalance = jo.getString("xzbbalance");
            }
            if (jo.has("egoldbalance")) {
                info.egoldbalance = jo.getString("egoldbalance");
            }
            if (jo.has("scorebalance")) {
                info.scorebalance = jo.getString("scorebalance");
            }
            if (jo.has("adddate")) {
                info.adddate = jo.getString("adddate");
            }
        }
        return info;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(moneytype);
        dest.writeString(moneystatus);
        dest.writeString(moneyamount);
        dest.writeString(remark);
        dest.writeString(capitalbanlance);
        dest.writeString(interestbalance);
        dest.writeString(xzbbalance);
        dest.writeString(egoldbalance);
        dest.writeString(scorebalance);
        dest.writeString(adddate);
    }

    public static final Creator<MoneyLogInfoEntity> CREATOR = new Creator<MoneyLogInfoEntity>() {

        @Override
        public MoneyLogInfoEntity [] newArray(int size) {
            return new MoneyLogInfoEntity[size];
        }

        @Override
        public MoneyLogInfoEntity createFromParcel(Parcel source) {
            return new MoneyLogInfoEntity(source);
        }
    };
    
	public List<MoneyLogInfoEntity> getList() {
		return list;
	}

	public void setList(List<MoneyLogInfoEntity> list) {
		this.list = list;
	}
	
}
