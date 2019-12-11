package com.yiqiji.money.modules.common.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by ${huangweishui} on 2017/3/13.
 * address huang.weishui@71dai.com
 */
public class UnListenerHelper {

    public static UMShareListener getUMShareListener(final Context context) {

        UMShareListener umShareListener = new UMShareListener() {

            @Override
            public void onStart(SHARE_MEDIA share_media) {


            }

            @Override
            public void onResult(SHARE_MEDIA platform) {

                Toast.makeText(context, " 分享成功啦", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                Toast.makeText(context, " 分享失败啦", Toast.LENGTH_SHORT).show();
                if (t != null) {
                    Log.d("throw", "throw:" + t.getMessage());
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                Toast.makeText(context, " 分享取消了", Toast.LENGTH_SHORT).show();
            }

        };
        return umShareListener;
    }


}
