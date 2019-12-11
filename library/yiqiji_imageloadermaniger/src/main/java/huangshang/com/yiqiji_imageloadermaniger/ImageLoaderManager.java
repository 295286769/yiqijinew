package huangshang.com.yiqiji_imageloadermaniger;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

import huangshang.com.yiqiji_imageloadermaniger.transformations.CropCircleTransformation;
import huangshang.com.yiqiji_imageloadermaniger.transformations.RoundedCornersTransformation;
import huangshang.com.yiqiji_imageloadermaniger.transformations.internal.IntegerVersionSignature;

/**
 * Created by ${huangweishui} on 2017/5/19.
 * address huang.weishui@71dai.com
 */
public class ImageLoaderManager {

    public static void loadImage(Context context, String url, int erroImg, int emptyImg, ImageView iv) {
        //原生 API
        Glide.with(context).load(url).signature(new IntegerVersionSignature(R.string.appversion)).placeholder(emptyImg).error(erroImg).into(iv);
    }

    public static void loadImage(Context context, String url, int defultImg, ImageView iv) {
        //原生 API
        Glide.with(context).load(url).signature(new IntegerVersionSignature(R.string.appversion)).dontAnimate().placeholder(defultImg).into(iv);
    }

    public static void loadImage(Context context, String url, int defultImg, ImageView iv, int appversion) {
        //原生 API
        Glide.with(context).load(url).signature(new IntegerVersionSignature(appversion)).signature(new IntegerVersionSignature(appversion)).dontAnimate().placeholder(defultImg).into(iv);
    }

    public static void loadImage(Context context, int drawbleid, int defultImg, ImageView iv) {
        //原生 API
        Glide.with(context).load(drawbleid).signature(new IntegerVersionSignature(R.string.appversion)).placeholder(defultImg).error(defultImg).dontAnimate().into(iv);
    }

    public static void loadImage(Context context, byte[] bytes, int defultImg, ImageView iv) {
        //原生 API
        Glide.with(context).load(bytes).signature(new IntegerVersionSignature(R.string.appversion)).placeholder(defultImg).error(defultImg).dontAnimate().into(iv);
    }

    public static void loadImage(Context context, String url, ImageView iv) {

        //原生 API
        Glide.with(context).load(url).signature(new IntegerVersionSignature(R.string.appversion)).crossFade().placeholder(0).error(0).into(iv);
    }


    public static void loadImageDiskCacheStrategy(Context context, String url, ImageView iv) {//是否跳过缓存
        //原生 API
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.NONE).crossFade().placeholder(0).error(0).into(iv);
    }

    public static void loadImage(String url, ImageView iv) {
        //原生 API
        Glide.with(iv.getContext()).load(url).signature(new IntegerVersionSignature(R.string.appversion)).crossFade().placeholder(0).error(0).into(iv);
    }


    public static void loadImage(Context context, final File file, final ImageView imageView) {
        Glide.with(context)
                .load(file)
                .into(imageView);
    }

    public static void loadImage(Context context, final int resourceId, final ImageView imageView) {
        Glide.with(context)
                .load(resourceId)
                .into(imageView);
    }

    //加载指定大小
    public static void loadImageViewSize(Context mContext, String path, int width, int height, ImageView mImageView) {
        Glide.with(mContext).load(path).override(width, height).into(mImageView);
    }

    //填充
    public static void loadImageCrop(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(mImageView);
    }

    //设置跳过内存缓存
    public static void loadImageViewCache(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).skipMemoryCache(true).into(mImageView);
    }

    //设置下载优先级
    public static void loadImageViewPriority(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).priority(Priority.NORMAL).into(mImageView);
    }

    /**
     * 策略解说：
     * <p/>
     * all:缓存源资源和转换后的资源
     * <p/>
     * none:不作任何磁盘缓存
     * <p/>
     * source:缓存源资源
     * <p/>
     * result：缓存转换后的资源
     */
    //设置缓存策略
    public static void loadImageViewDiskCache(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).into(mImageView);
    }

    //设置加载动画
    public static void loadImageViewAnim(Context mContext, String path, int anim, ImageView mImageView) {
        Glide.with(mContext).load(path).animate(anim).into(mImageView);
    }

    /**
     * 会先加载缩略图
     */

    //设置缩略图支持
    public static void loadImageViewThumbnail(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).thumbnail(0.1f).into(mImageView);
    }

    public static void loadGifImage(Context context, String url, ImageView iv) {
        Glide.with(context).load(url).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(0).error(0).into(iv);
    }

    public static void loadCircleImage(Context context, String url, ImageView iv) {
        Glide.with(context).load(url).placeholder(0).error(0).bitmapTransform(new CropCircleTransformation(context)).into(iv);
    }

    public static void loadCircleImage(Context context, int resourceId, ImageView iv) {
        Glide.with(context).load(resourceId).placeholder(0).error(0).bitmapTransform(new CropCircleTransformation(context)).into(iv);
    }

    public static void loadRoundCornerImage(Context context, String url, ImageView iv, int radius) {
        Glide.with(context).load(url).placeholder(0).error(0).bitmapTransform(new RoundedCornersTransformation(context, radius,
                RoundedCornersTransformation.CornerType.ALL)).into(iv);
    }

    public static void loadRoundCornerImage(Context context, int resourceId, ImageView iv, int radius) {
        Glide.with(context).load(resourceId).placeholder(0).error(0).bitmapTransform(new RoundedCornersTransformation(context, radius,
                RoundedCornersTransformation.CornerType.ALL)).into(iv);
    }

}
