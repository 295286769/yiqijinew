package com.yiqiji.money.modules.common;

import android.content.Context;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by leichi on 2017/6/8.
 */

public class CustomCachingGlideModule implements GlideModule {

    String downloadDirectoryPath = Environment.getExternalStorageDirectory().getPath()+"/glide_image";

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        int cacheSize100MegaBytes = 104857600;
        builder.setDiskCache(
                new DiskLruCacheFactory(downloadDirectoryPath, cacheSize100MegaBytes));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
