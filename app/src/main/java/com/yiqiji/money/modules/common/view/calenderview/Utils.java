package com.yiqiji.money.modules.common.view.calenderview;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ${huangweishui} on 2017/4/20.
 * address huang.weishui@71dai.com
 */
public class Utils {
    /**
     * 根据手机的分辨率从dp转换成px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 获取手机屏幕分辨率
     *
     * @param activity
     * @return
     */
    public static DisplayMetrics getPhoneScreen(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();

        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }
    /**
     * @return eg:09-29
     */
    public static String formatTheDateToMM_dd(Date date, int type) {
        String str = "";
        switch (type) {
            case 0:
                str = new SimpleDateFormat("MM-dd", Locale.CHINESE).format(date);
                break;
            case 1:
                str = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE).format(date);
                break;
            case 2:
                str = new SimpleDateFormat("dd", Locale.CHINESE).format(date);
                break;
            case 3:
                str = new SimpleDateFormat("MM", Locale.CHINESE).format(date);
                break;
            case 4:
                str = new SimpleDateFormat("EEEE", Locale.CHINESE).format(date);
                break;
            case 5:
                str = new SimpleDateFormat("yyyy", Locale.CHINESE).format(date);
                break;
            case 6:
                str = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINESE).format(date);
                break;

        }
        return str;
    }
}
