package com.yiqiji.money.modules.common.request;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.yiqiji.money.modules.common.callback.BaseCallBack;

import java.util.HashMap;

/**
 * Created by ${huangweishui} on 2017/3/11.
 * address huang.weishui@71dai.com
 */
public class RequstHelpr {
    private static String urlString = "";

    public static void RequstBotton(ApiService apiService, String url, HashMap<String, String> stringStringHashMap, BaseCallBack baseCallBack) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (urlString.equals(url)) {
            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    String urlString = "";
                    return;
                }
            };
            handler.sendEmptyMessageDelayed(0, 2000);
        } else {
            urlString = url;
//            apiService.addBook(stringStringHashMap).enqueue(baseCallBack);
        }

    }
}
