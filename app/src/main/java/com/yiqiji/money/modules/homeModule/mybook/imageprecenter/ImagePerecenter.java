package com.yiqiji.money.modules.homeModule.mybook.imageprecenter;

import android.content.Context;
import android.widget.ImageView;

import com.yiqiji.money.R;

import huangshang.com.yiqiji_imageloadermaniger.ImageLoaderManager;

/**
 * Created by ${huangweishui} on 2017/6/14.
 * address huang.weishui@71dai.com
 */
public class ImagePerecenter {
    public static void setImage(Context context, String url, ImageView imageView) {
        ImageLoaderManager.loadImage(context, url, R.drawable.sing_icon, imageView);
    }

    public static void setImage(Context context, int drawbleid, ImageView imageView) {
        ImageLoaderManager.loadImage(context, drawbleid, R.drawable.sing_icon, imageView);
    }

    public static void setImage(Context context, byte[] bytes, ImageView imageView) {
        ImageLoaderManager.loadImage(context, bytes, R.drawable.sing_icon, imageView);
    }
}
