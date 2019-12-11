package com.yiqiji.money.modules.homeModule.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.yiqiji.money.modules.common.utils.XzbUtils;

/**
 * Created by ${huangweishui} on 2017/5/15.
 * address huang.weishui@71dai.com
 */
public class MemberAdapter {
    private Context mContext;
    private View view_list;
    private View alignment_view_list;
    private int view_item_id;
    private int screeWith = 0;

    public MemberAdapter(Context context, View view_list, int view_item_id, View alignment_view_list) {
        this.mContext = context;
        this.view_list = view_list;
        this.view_item_id = view_item_id;
        this.alignment_view_list = alignment_view_list;
        screeWith = XzbUtils.getPhoneScreenintWith((Activity) mContext);
    }


}
