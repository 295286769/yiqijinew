package com.yiqiji.money.modules.homeModule.mybook.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.yiqiji.money.modules.common.activity.AddressSelctActivity;
import com.yiqiji.money.modules.homeModule.mybook.activity.HouseStypeActivity;
import com.yiqiji.money.modules.homeModule.mybook.activity.RenovationStypeActivity;
import com.zaaach.citypicker.CityPickerActivity;

/**
 * Created by ${huangweishui} on 2017/7/31.
 * address huang.weishui@71dai.com
 */
public class MyBookActivityUtil {
    public static final int CITYPICKERACTIVITY = 1000;
    public static final int HOUSESTYPEACTIVITY = 1001;
    public static final int HOUSESTYPEACTIVITY_RESULTCODE = 1002;
    public static final int RENOVATIONSTYPEACTIVITY_RESULTCODE = 1003;
    public static final int CITYPICKERACTIVITY_RESULTCODE = 1004;
    public static final int DECORATE_REQUSTTCODE = 1005;
    public static final int DECORATE_RESULTCODE = 1006;

    public static void startAddressActivity(Context context, String accountbookid) {
        Intent intent = new Intent(context, AddressSelctActivity.class);
        intent.putExtra("accountbookid", accountbookid);
        context.startActivity(intent);
    }

    public static void startCityPickerActivity(Context context) {
        Intent intent = new Intent(context, CityPickerActivity.class);
        ((Activity) context).startActivityForResult(intent, CITYPICKERACTIVITY);
    }

    public static void startHouseStypeActivity(Context context, int type, String content) {
        Intent intent = new Intent(context, HouseStypeActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("content", content);
        ((Activity) context).startActivityForResult(intent, HOUSESTYPEACTIVITY);
    }

    public static void startRenovationStypeActivity(Context context, String content) {
        Intent intent = new Intent(context, RenovationStypeActivity.class);
        intent.putExtra("content", content);
        ((Activity) context).startActivityForResult(intent, DECORATE_REQUSTTCODE);
    }
}
