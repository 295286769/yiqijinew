package com.yiqiji.money.modules.homeModule.mybook.perecenter;

import android.content.Context;

import com.yiqiji.money.modules.common.utils.IntentUtils;

/**
 * Created by ${huangweishui} on 2017/6/15.
 * address huang.weishui@71dai.com
 */
public class IntentPerecenter {
    public static void intentJrop(Context context, Class aClass, String... para) {

        String[] key = {"id", "bookName", "categoryicon", "isFirstAdd", "isEdit", "accountbookid"};
        StringBuilder a = new StringBuilder();
        if (key.length < para.length) {
            for (int i = 0; i < key.length; i++) {
                if (para.length < 2) {
                    a.append(key[i]).append(",").append(para[i]);
                } else {
                    if (i == para.length - 1) {
                        a.append(key[i]).append(",").append(para[i]);
                    } else {
                        a.append(key[i]).append(",").append(para[i]).append(",");
                    }
                }
            }
        } else {
            for (int i = 0; i < para.length; i++) {
                if (para.length < 2) {
                    a.append(key[i]).append(",").append(para[i]);
                } else {
                    if (i == para.length - 1) {
                        a.append(key[i]).append(",").append(para[i]);
                    } else {
                        a.append(key[i]).append(",").append(para[i]).append(",");
                    }
                }
            }
        }
        String[] paramas = a.toString().split(",");
        IntentUtils.startActivity(context, aClass, paramas);
    }
}
