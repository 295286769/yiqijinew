package com.yiqiji.money.modules.homeModule.home.modelperecenter;

import android.net.Uri;
import android.text.TextUtils;

import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.db.DailycostEntity;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.money.modules.common.utils.FileUtil;
import com.yiqiji.frame.core.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ${huangweishui} on 2017/5/25.
 * address huang.weishui@71dai.com
 */
public class ModelPerecenter {
    /**
     * 添加日志
     */
    public static List<DailycostEntity> addDailycostInfo(String textName, String accountbookid, String location, String billcateid, String accountbooktype, Uri uri, Date date_time) {
        List<DailycostEntity> dailycostEntityList = new ArrayList<>();
        String pkid = StringUtils.getUUID();

        final DailycostEntity dailycostEntity = new DailycostEntity();


        if (TextUtils.isEmpty(textName)) {
            dailycostEntity.setBillmark("");
        } else {
            dailycostEntity.setBillmark(textName);
        }
        // 消费类型
        dailycostEntity.setBilltype("5");
        dailycostEntity.setIssynchronization("false");// 是否已同步
        dailycostEntity.setAccountbookid(accountbookid);
        dailycostEntity.setUsername(LoginConfig.getInstance().getUserName());
        // 资产账户ID,默认为0
        dailycostEntity.setAccountnumber("0");

        long mTradetime = getLongData(date_time);
        dailycostEntity.setYear(Integer.parseInt(DateUtil.transferLongToDate(2, mTradetime)));
        dailycostEntity.setMoth(Integer.parseInt(DateUtil.transferLongToDate(3, mTradetime)));
        dailycostEntity.setDay(Integer.parseInt(DateUtil.transferLongToDate(4, mTradetime)));
//        dailycostEntity.setBillamount(money);
        dailycostEntity.setAccountbooktype(accountbooktype);// 是否多人账本
        dailycostEntity.setPkid(pkid);// 账单主键唯一ID,精确到秒
        dailycostEntity.setBillid("");
        dailycostEntity.setBillclear(0 + "");
        dailycostEntity.setIsclear(0 + "");// 是否需要结算
        dailycostEntity.setYear(Integer.parseInt(DateUtil.transferLongToDate(2, mTradetime)));
        dailycostEntity.setMoth(Integer.parseInt(DateUtil.transferLongToDate(3, mTradetime)));
        dailycostEntity.setDay(Integer.parseInt(DateUtil.transferLongToDate(4, mTradetime)));
        dailycostEntity.setTradetime(mTradetime + "");// 创建时间(时间轴选则的时间)

        long curTimestamp = System.currentTimeMillis() / 1000;
        dailycostEntity.setBillctime(curTimestamp + "");// 记录所在时间的那一天 毫秒
        dailycostEntity.setUpdatetime(curTimestamp);// 更新时间

        dailycostEntity.setBillcateid(billcateid);// 账单一级分类id
        if (FileUtil.getfileLenth(uri)) {
            String urlPath = uri.toString();
            if (!urlPath.contains("file://")) {
                urlPath = "file://" + urlPath;
            }
            dailycostEntity.setBillimg(urlPath);

        } else {
            dailycostEntity.setBillimg("");
        }

        if (TextUtils.isEmpty(location) || location.equals("定位位置")) {
            location = "";
        }
        dailycostEntity.setAddress(location);
        dailycostEntityList.add(dailycostEntity);
        return dailycostEntityList;
    }

    /**
     * 添加日志
     */
    public static List<DailycostEntity> addDailycostInfo(String textName, String accountbookid,
                                                         String location, String billcateid, String accountbooktype,
                                                         Uri uri, Date date_time, int cateId) {
        List<DailycostEntity> dailycostEntityList = new ArrayList<>();
        String pkid = StringUtils.getUUID();

        final DailycostEntity dailycostEntity = new DailycostEntity();
        dailycostEntity.setBillcateid(String.valueOf(cateId));

        if (TextUtils.isEmpty(textName)) {
            dailycostEntity.setBillmark("");
        } else {
            dailycostEntity.setBillmark(textName);
        }
        // 消费类型
        dailycostEntity.setBilltype("5");
        dailycostEntity.setIssynchronization("false");// 是否已同步
        dailycostEntity.setAccountbookid(accountbookid);
        dailycostEntity.setUsername(LoginConfig.getInstance().getUserName());
        // 资产账户ID,默认为0
        dailycostEntity.setAccountnumber("0");

        long mTradetime = getLongData(date_time);
        dailycostEntity.setYear(Integer.parseInt(DateUtil.transferLongToDate(2, mTradetime)));
        dailycostEntity.setMoth(Integer.parseInt(DateUtil.transferLongToDate(3, mTradetime)));
        dailycostEntity.setDay(Integer.parseInt(DateUtil.transferLongToDate(4, mTradetime)));
//        dailycostEntity.setBillamount(money);
        dailycostEntity.setAccountbooktype(accountbooktype);// 是否多人账本
        dailycostEntity.setPkid(pkid);// 账单主键唯一ID,精确到秒
        dailycostEntity.setBillid("");
        dailycostEntity.setBillclear(0 + "");
        dailycostEntity.setIsclear(0 + "");// 是否需要结算
        dailycostEntity.setYear(Integer.parseInt(DateUtil.transferLongToDate(2, mTradetime)));
        dailycostEntity.setMoth(Integer.parseInt(DateUtil.transferLongToDate(3, mTradetime)));
        dailycostEntity.setDay(Integer.parseInt(DateUtil.transferLongToDate(4, mTradetime)));
        dailycostEntity.setTradetime(mTradetime + "");// 创建时间(时间轴选则的时间)

        long curTimestamp = System.currentTimeMillis() / 1000;
        dailycostEntity.setBillctime(curTimestamp + "");// 记录所在时间的那一天 毫秒
        dailycostEntity.setUpdatetime(curTimestamp);// 更新时间

//        dailycostEntity.setBillcateid(billcateid);// 账单一级分类id
        if (FileUtil.getfileLenth(uri)) {
            String urlPath = uri.toString();
            if (!urlPath.contains("file://")) {
                urlPath = "file://" + urlPath;
            }
            dailycostEntity.setBillimg(urlPath);

        } else {
            dailycostEntity.setBillimg("");
        }

        if (TextUtils.isEmpty(location) || location.equals("定位位置")) {
            location = "";
        }
        dailycostEntity.setAddress(location);
        dailycostEntityList.add(dailycostEntity);
        return dailycostEntityList;
    }

    private static long getLongData(Date date_time) {
        long mTradetime = DateUtil.toTimeStamp(date_time) / 1000;
        mTradetime = mTradetime - DateUtil.longTime(date_time) + DateUtil.longTime(new Date());
        return mTradetime;
    }
}
