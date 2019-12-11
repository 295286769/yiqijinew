package com.yiqiji.money.modules.community.decoration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.yiqiji.money.modules.community.decoration.activity.ChangeHouseInfoActivity;
import com.yiqiji.money.modules.community.decoration.activity.DecoratCommunityActivity;
import com.yiqiji.money.modules.community.decoration.activity.DecorateCommpanyDetailActivity;
import com.yiqiji.money.modules.community.decoration.activity.DecorateCommpanyListActivity;
import com.yiqiji.money.modules.community.decoration.activity.DecorateMarketActivity;
import com.yiqiji.money.modules.community.decoration.activity.DecorateNearBookActivity;

/**
 * Created by dansakai on 2017/8/2.
 * 装修社区 activity启动工具类
 */

public class ActivityUtil {
    /**
     * 启动装修首页
     */
    public static void startDecorateHome(Context mContext) {
        mContext.startActivity(new Intent(mContext, DecoratCommunityActivity.class));
    }

    /**
     * 启动装修公司列表页
     */
    public static void startCommpanyList(Context mContext) {
        mContext.startActivity(new Intent(mContext, DecorateCommpanyListActivity.class));
    }

    /**
     * 启动装修公司详情页
     */
    public static void startCommpantDetail(Context mContext,String commpanyId) {
        Intent intent = new Intent(mContext, DecorateCommpanyDetailActivity.class);
        intent.putExtra("commpanyId", commpanyId);
        mContext.startActivity(intent);
    }
    /**
     * 启动装修行情统计页
     */
    public static void startDecorateMarket(Context mContext) {
        mContext.startActivity(new Intent(mContext, DecorateMarketActivity.class));
    }

    /**
     * 启动查看附近装修共享账本
     */
    public static void startDecorateNearBook(Context mContext) {
        mContext.startActivity(new Intent(mContext, DecorateNearBookActivity.class));
    }

    /**
     * 启动修改小区信息
     */
    public static void startChangeHouseInfoActivity(Context mContext,int request_code) {
        ((Activity)mContext).startActivityForResult(new Intent(mContext, ChangeHouseInfoActivity.class),request_code);
    }
}
