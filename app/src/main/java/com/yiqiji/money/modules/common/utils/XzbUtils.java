package com.yiqiji.money.modules.common.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.BuildConfig;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.plication.MyApplicaction;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class XzbUtils {
    private static DisplayMetrics dm;
    public static Uri uri;
    public static ImageLoader imageLoader = ImageLoader.getInstance();
    public static boolean isForn = true;// 应用是否在前台

    public static void onViewAlphaOne(TextView textView, ImageView imageView, int a, int b) {
        ObjectAnimator textAnim = ObjectAnimator.ofFloat(textView, "alpha", a, b);
        ObjectAnimator imageAnim = ObjectAnimator.ofFloat(imageView, "alpha", a, b);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(textAnim, imageAnim);// 一起执行
        animatorSet.setDuration(500);
        animatorSet.start();
    }

    public static void onViewAlphaOne(TextView textView, int a, int b) {
        ObjectAnimator textAnim = ObjectAnimator.ofFloat(textView, "alpha", a, b);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(textAnim);// 一起执行
        animatorSet.setDuration(500);
        animatorSet.start();
    }

    public static void shakeTextView(TextView textview) {
        ObjectAnimator textAnim = ObjectAnimator.ofFloat(textview, "translationX", 0, 30, 30, 0);
        // 设置插值器
        // textAnim.setInterpolator(new AccelerateInterpolator());
        textAnim.setRepeatCount(1);
        textAnim.setDuration(200);
        textAnim.start();
    }

    // 判断输入的值有没有超过百万
    public static boolean isOverOneMillion(String cleanData) {
        // 首先判断有没有加号,有则相加判断是否大于9999999
        if (cleanData.contains("+")) {
            ArrayList result = CountMoney.getStringList(cleanData); // String转换为List
            result = CountMoney.getPostOrder(result); // 中缀变后缀
            Double i = CountMoney.calculate(result);
            if (i > 1000000) {
                return true;
            }
        } else {// 如果没有
            Double j = Double.valueOf(cleanData);
            if (j > 1000000) {
                return true;
            }
        }
        return false;
    }

    public static void hideView(View view) {
        int visibility = view.getVisibility();
        if (visibility == View.VISIBLE) {
            view.setVisibility(View.GONE);
        }
    }

    public static void showView(View view) {
        int visibility = view.getVisibility();
        if (visibility == View.GONE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    // 必定返回两位小数
    public static String inNumberFormat(double money) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(money);

    }

    // 截取小数点后第三位
    public static String getPointAfterThree(String text) {
        String jieguo = text.substring(text.indexOf(".") + 3, text.indexOf(".") + 4);
        return jieguo;
    }

    // 获取小数点后有多少位数
    public static int getPoibtAfterCount(String text) {
        int position = text.length() - text.indexOf(".") - 1;
        return position;
    }

    /**
     * 如果最后是“.” 则删除
     *
     * @param textView
     * @return
     */
    public static String deleteSpot(TextView textView, String keySpot) {
        String textData = textView.getText().toString();
        String cutMoney = textData.substring(textData.length() - 1);
        if (cutMoney.equals(keySpot)) {
            return textData.substring(0, textData.length() - 1);
        }
        return textData;
    }

    /**
     * 下拉添加一条数据的时候，清除前面的默认值
     */
    public static String cleanTextView(TextView textView) {
        String textData = textView.getText().toString();
        if (textData.equals("0.00") || textData.equals("0")) {
            textView.setText("");
            return textView.getText().toString();
        }
        return textData;
    }

    public static String cleanTextToZero(TextView textView) {
        String textData = textView.getText().toString();
        if (textData.equals("0.00")) {
            textView.setText("0");
            return textView.getText().toString();
        }
        return textData;
    }

    public static String cleanText(TextView textView) {

        textView.setText("");
        return textView.getText().toString();

    }

    public static String addTextToZero(TextView textView) {
        String textData = textView.getText().toString();
        textView.setText(textData + "0");
        return textView.getText().toString();
    }

    public static boolean equalsIsZero(TextView textView) {
        String textData = textView.getText().toString();
        if (textData.equals("0.00")) {
            return true;
        }
        return false;
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeightPx(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            sbar = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(sbar);
        } catch (Exception e1) {
            Log.e("getStatusBarHeightPx", "get status bar height fail");
            e1.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取手机屏幕分辨率
     *
     * @param activity
     * @return
     */
    public static DisplayMetrics getPhoneScreen(Activity activity) {
        dm = new DisplayMetrics();

        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 获取手机屏幕宽度
     *
     * @param activity
     * @return
     */
    public static int getPhoneScreenintWith(Activity activity) {
        int with = 0;
        dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        with = dm.widthPixels;
        return with;
    }

    /**
     * 获取手机屏幕宽度
     *
     * @param activity
     * @return
     */
    public static int getPhoneScreenintHeight(Activity activity) {
        int height = 0;
        dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        height = dm.heightPixels;
        return height;
    }

    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getAppMetaData(String key) {
        Context ctx = MyApplicaction.getContext();
        if (ctx == null || key == null) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key) + "";
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }

    /**
     * 获取友盟渠道名
     * <p>
     * 实际上context就可以
     *
     * @return 如果没有获取成功，那么返回值为空
     */
    public static String getChannelName() {
        Context ctx = MyApplicaction.getContext();
        if (ctx == null) {
            return null;
        }
        String channelName = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        //此处这样写的目的是为了在debug模式下也能获取到渠道号，如果用getString的话只能在Release下获取到。
                        channelName = applicationInfo.metaData.getString("UMENG_CHANNEL");
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelName;
    }


    public static int[] returnInt(String[] array) {
        int[] height = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            height[i] = (int) Double.parseDouble(array[i]);
        }
        return height;

    }


    /**
     * 取出数组中的最大值
     *
     * @param arr
     * @return
     */
    public static int getMax(int[] arr) {
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

    /**
     * 取出数组中的最大值的下标
     *
     * @param arr
     * @return
     */
    public static int getMaxIndex(float[] arr) {
        float max = arr[0];
        int idex = 0;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
                idex = i;
            }
        }
        return idex;
    }

    /**
     * 取出数组中的最小值
     *
     * @param arr
     * @return
     */
    public static int getMin(int[] arr) {
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < max) {
                max = arr[i];
            }
        }
        return max;
    }

    public static String numberChange(Float number) {
        String stringNumber = null;
        if (number >= 10000 && number < 10000000) {
            stringNumber = String.format("%.2f", Double.parseDouble((number / 10000) + "")) + "万";
        } else if (number >= 10000000) {

            stringNumber = String.format("%.2f", Double.parseDouble((number / 10000000) + "")) + "亿";
        } else if (number < 10000) {
            stringNumber = String.format("%.2f", Double.parseDouble(number + ""));
        }

        return stringNumber;
    }

    /**
     * 获取App版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion(Context mthis) {
        try {
            PackageManager manager = mthis.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mthis.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "0.0.0";
        }
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getAppPackageInfo(context).versionCode;
    }

    public static PackageInfo getAppPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    /**
     * 获取系统版本4.0.4
     *
     * @return
     */
    public static String getHandSetInfo() {
        String handSetInfo = android.os.Build.VERSION.RELEASE;
        return handSetInfo;
    }

    /**
     * 手机类型 小米。。。
     *
     * @return
     */
    public static String getMobleType() {
        String handSetInfo = android.os.Build.MODEL;
        return handSetInfo;
    }

    @SuppressWarnings("unused")
    public static void downFile(final Context context, final String url, final ProgressDialog m_progressDlg) {
        m_mainHandler = new Handler();
        m_progressDlg.show();
        new Thread() {
            public void run() {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse response;
                try {
                    response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    long length = entity.getContentLength();
                    m_progressDlg.setMax((int) length / 10000);// 设置进度条的最大值
                    InputStream is = entity.getContent();
                    FileOutputStream fileOutputStream = null;
                    if (is != null) {
                        File file = new File(Environment.getExternalStorageDirectory(), "name");
                        fileOutputStream = new FileOutputStream(file);
                        byte[] buf = new byte[1024];
                        int ch = -1;
                        int count = 0;
                        while ((ch = is.read(buf)) != -1) {
                            fileOutputStream.write(buf, 0, ch);
                            count += ch;
                            if (length > 0) {
                                m_progressDlg.setProgress(count / 10000);
                            }
                        }
                    }
                    fileOutputStream.flush();
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    down(context, m_progressDlg);
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private static Handler m_mainHandler;

    private static void down(final Context context, final ProgressDialog m_progressDlg) {
        m_mainHandler.post(new Runnable() {
            public void run() {

                m_progressDlg.cancel();
                update(context);
            }
        });
    }

    private static void update(Context context) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", new File(Environment.getExternalStorageDirectory(), "name"));
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "name")),
                    "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    /**
     * 外部sdcard创建文件
     */
    public static Uri getPath() {
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                // path=Environment.getExternalStorageDirectory();
                File file = Environment.getExternalStorageDirectory();
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                String time = dateFormat.format(new Date());
                String path = file.getAbsolutePath() + "/" + time + ".png";
                File fileImage = new File(path);
                if (fileImage.exists()) {
                    fileImage.delete();
                }
                fileImage.createNewFile();
                uri = Uri.fromFile(fileImage);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }

    /**
     * 外部sdcard创建文件(for take photo)
     */
    public static Uri getTpPath() {
        Uri tpUri = null;
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                // path=Environment.getExternalStorageDirectory();
                File file = Environment.getExternalStorageDirectory();
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                String time = dateFormat.format(new Date());
                String path = file.getAbsolutePath() + "/" + time + ".png";
                final File fileImage = new File(path);

                tpUri = Uri.fromFile(fileImage);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tpUri;
    }

    /**
     * 外部sdcard创建文件
     */
    public static String getPathString() {
        String path = "";
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                // path=Environment.getExternalStorageDirectory();
                File file = Environment.getExternalStorageDirectory();
                // DateFormat dateFormat = new
                // SimpleDateFormat("yyyyMMddHHmmss");
                // String time = dateFormat.format(new Date());
                path = file.getAbsolutePath() + "/" + "image";
                File file2 = null;
                try {
                    file2 = new File(path);
                    if (!file2.exists()) {
                        file2.mkdir();
                    }
                    path = file2.getAbsolutePath();
                } catch (Exception e) {

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }


    //调用系统裁剪
    public static void doCropPhoto(Context act, Uri uri, Uri path, int requestCode) {
        try {
            final Intent intent = getCropImageIntent(act, uri, path);
            ((Activity) act).startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Intent getCropImageIntent(Context context, Uri photoUri, Uri path) {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (currentapiVersion < 24) {
            intent.setDataAndType(photoUri, "image/*");
        } else {
            intent.setDataAndType(StringUtils.getImageContentUri(context, new File(StringUtils.getRealFilePath(context, photoUri))), "image/*");
//            if (photoUri.toString().contains("file")) {
//                intent.setDataAndType(StringUtils.getImageContentUri(context, new File(StringUtils.getRealFilePath(context, photoUri))), "image/*");
//            } else {
//                intent.setDataAndType(photoUri, "image/*");
//            }
        }

        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("return-data", false);
        // intent.putExtra("return-data", false);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        //为拍摄的图片指定一个存储的路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, path);

        intent.putExtra("outputFormat", CompressFormat.JPEG.toString());

        return intent;
    }

    public static Bitmap getBitMBitmap(String urlpath) {
        return imageLoader.loadImageSync(urlpath);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 计算缩放比例
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(String resId, int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(resId, options);

        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(resId, options);
    }

    public static Bitmap decodeUriAsBitmap(Context context, Uri uri, boolean isCompress) {
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            if (isCompress) {
                o2.inSampleSize = 2;
            } else {
                o2.inSampleSize = 1;
            }

            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, o2);
            bitmap = adjuseBmp(bitmap, uri.getPath());
            // if(isNeedCut){
            // bitmap = scaleBitmap(bitmap,DdUtil.cutSize,DdUtil.cutSize);
            // }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    public static Bitmap decodeBitmap(Context context, Bitmap bitmapd, String path, boolean isCompress) {
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            if (isCompress) {
                o2.inSampleSize = 2;
            } else {
                o2.inSampleSize = 1;
            }

            bitmap = adjuseBmp(bitmapd, path);
            // if(isNeedCut){
            // bitmap = scaleBitmap(bitmap,DdUtil.cutSize,DdUtil.cutSize);
            // }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    public static File saveImage(Bitmap bmp) {
        if (bmp == null) {
            return null;
        }
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static Bitmap adjuseBmp(Bitmap bmp, String path) {
        return adjuseBmp(bmp, path, null);
    }

    public static Bitmap adjuseBmp(Bitmap bmp, String path, ArrayList<String> xys) {
        // 从指定路径下读取图片，并获取其EXIF信息
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (xys != null) {
            String x = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            String y = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            xys.add("|" + calcxy(x) + "|" + calcxy(y));
        }
        // 获取图片的旋转信息
        int orientation = exifInterface
                .getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int degree = 0;
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degree = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degree = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degree = 270;
                break;
        }
        if (degree != 0) {
            return rotateBitmapByDegree(bmp, degree);
        }

        return bmp;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * 计算坐标
     *
     * @param xory
     * @return
     * @author huangcheng
     */
    public static String calcxy(String xory) {
        if (xory != null) {
            try {
                // 121/1,24/1,461206/10000 31/1,17/1,306706/10000
                String[] strs = xory.split(",");
                if (strs.length >= 3) {
                    String[] n1s = strs[0].split("/");
                    String[] n2s = strs[1].split("/");
                    String[] n3s = strs[2].split("/");
                    if (n1s.length >= 2 && (n2s.length >= 2 && n3s.length >= 2)) {
                        double n1 = Double.parseDouble(n1s[0]) / Double.parseDouble(n1s[1]);
                        double n2 = Double.parseDouble(n2s[0]) / (Double.parseDouble(n2s[1]) * 60);
                        double n3 = Double.parseDouble(n3s[0]) / (Double.parseDouble(n3s[1]) * 3600);
                        String rs = String.valueOf(n1 + n2 + n3);
                        if ("NaN".equals(rs)) {
                            return "_";
                        }
                        return rs;
                    }
                }
            } catch (Exception e) {
                return "_";
            }
        }
        return "_";
    }

    public static void displayImageHead(ImageView imageView, String imageUrl, int drawable) {

        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(drawable)
                .showImageForEmptyUri(drawable).showImageOnFail(drawable).cacheInMemory(true).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        imageLoader.displayImage(imageUrl, imageView, options);
    }

    public static void displayRoundImage(ImageView imageView, String imageUrl, int drawable, int cornerRadius) {

        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(drawable)
                .showImageForEmptyUri(drawable).showImageOnFail(drawable).cacheInMemory(true).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(cornerRadius))
                .build();
        imageLoader.displayImage(imageUrl, imageView, options);
    }

    public static void displayImage(ImageView imageView, String imageUrl, int drawable) {
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(drawable)
                .showImageForEmptyUri(drawable).showImageOnFail(drawable).cacheInMemory(false).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        imageLoader.displayImage(imageUrl, imageView, options);

    }

    public static void displayImageListener(final ImageView imageView, String imageUrl, final float with, int drawable) {

        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(drawable)
                .showImageForEmptyUri(drawable).showImageOnFail(drawable).cacheInMemory(false).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageSize imageSize = new ImageSize((int) with, (int) ((int) with * (10.8f / 7.1f)));
//        imageLoader.displayImage(imageUrl, imageView, options, new SimpleImageLoadingListener() {
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                super.onLoadingComplete(imageUri, view, loadedImage);
//                loadedImage = Bitmap.createBitmap(loadedImage, 0, 0, (int) with, (int) ((int) with * (10.8f / 7.1f)));
//                imageView.setImageBitmap(loadedImage);
//            }
//        });
        imageLoader.displayImage(imageUrl, imageView, imageSize);

    }

    /**
     * 保留小数位
     *
     * @param decimal
     * @param f
     * @return
     */
    public static float setTwoDecimalFormat(String decimal, float f) {

        DecimalFormat df = new DecimalFormat(decimal);
        return Float.parseFloat(df.format(f));
    }

    /**
     * 保留小数位
     *
     * @param decimal
     * @param d
     * @return 此方法有问题
     */
    public static double setTwoDecimalFormat(String decimal, double d) {

        DecimalFormat df = new DecimalFormat(decimal);
        return Double.parseDouble(df.format(d));
    }

    /**
     * 使用String.format来实现。
     */
    public static String formatDouble(String decimal, double value) {

        // return String.format("%.2f", value).toString();
        return String.format(decimal, value).toString();
    }

    public static HashMap<String, String> mapParmas() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("tokenid", LoginConfig.getInstance().getTokenId());
        hashMap.put("deviceid", LoginConfig.getInstance().getDeviceid());
        hashMap.put("plat", "android");
        hashMap.put("appver", LoginConfig.getInstance().getAppver());
        hashMap.put("machine", LoginConfig.getInstance().getMachine());
        hashMap.put("osver", LoginConfig.getInstance().getOsver());
        hashMap.put("channel", XzbUtils.getChannelName());

        return hashMap;

    }

    /**
     * 拼接不需要file://头部
     *
     * @param iamge_url
     * @param cateid
     * @return
     */
    public static String initImageUrl(String iamge_url, String cateid) {
        if (TextUtils.isEmpty(iamge_url)) {
            iamge_url = XzbUtils.getPathString() + "/" + cateid + "_s";
        } else {

            if (iamge_url.contains("@") && InternetUtils.checkNet(MyApplicaction.getContext())) {
                iamge_url = iamge_url.replace("2x", "3x");
                iamge_url = iamge_url.substring(0, iamge_url.indexOf("@")) + "_s"
                        + iamge_url.substring(iamge_url.indexOf("@"), iamge_url.length());
            } else {
                iamge_url = XzbUtils.getPathString() + "/" + cateid + "_s";
            }

        }
        return iamge_url;
    }

    /**
     * 拼接需要file://头部
     *
     * @param iamge_url
     * @param cateid
     * @return
     */
    public static String initImageUrlNeedFile(String iamge_url, String cateid) {
        if (!TextUtils.isEmpty(iamge_url) && iamge_url.contains("@") && InternetUtils.checkNet(MyApplicaction.getContext())) {
            iamge_url = iamge_url.replace("2x", "3x");
            iamge_url = iamge_url.substring(0, iamge_url.indexOf("@")) + "_s"
                    + iamge_url.substring(iamge_url.indexOf("@"), iamge_url.length());
        } else {
            iamge_url = "file://" + XzbUtils.getPathString() + "/" + cateid + "_s";

        }
        return iamge_url;
    }


    public static String getBalance(double balan) {
        String balance = "0.00";
        if (balan > 10000) {

            balance = formatDouble("%.2f", balan / 10000);
            balance = balance + "万";

        } else if (balan > 100000000) {
            balance = formatDouble("%.2f", balan / 100000000);
            balance = balance + "亿";
        } else {
            balance = formatDouble("%.2f", balan);
            balance = balance + "";
        }
        return balance;
    }

    public static String getBalanceOther(double balan) {
        String balance = "0.00";
        if (balan >= 100000) {

            balance = formatDouble("%.2f", balan / 10000);
            balance = balance + "万";

        } else if (balan >= 100000000) {
            balance = formatDouble("%.2f", balan / 100000000);
            balance = balance + "亿";
        } else {
            balance = formatDouble("%.2f", balan);
            balance = balance + "";
        }
        return balance;
    }

    public static String getPackageInfo(Context context) {

        return getAppPackageInfo(context).packageName;
    }

    /**
     * 弹出软键盘
     *
     * @param context
     */
    public static void show(View View, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(View, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static void hidekeyboard(View View, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(View.getWindowToken(), 0); // 强制隐藏键盘
    }


    private static long lastClickTime = 0;//上次点击的时间

    private static int spaceTime = 1000;//时间间隔

    /**
     * 防止多次点击
     */
    public static boolean isFastClick() {

        long currentTime = System.currentTimeMillis();//当前系统时间

        boolean isAllowClick;//是否允许点击

        if (currentTime - lastClickTime > spaceTime) {

            isAllowClick = true;

        } else {

            isAllowClick = false;

        }

        lastClickTime = currentTime;

        return isAllowClick;

    }

    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }

        return false;
    }


    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public static boolean isAppOnForeground() {

        ActivityManager activityManager = (ActivityManager) MyApplicaction.getContext().getSystemService(
                Context.ACTIVITY_SERVICE);
        String packageName = MyApplicaction.getContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    /**
     * 返回当前类名，方法名，行数
     *
     * @return
     */
    public static String getTraceInfo() {
        StringBuffer sb = new StringBuffer();

        StackTraceElement[] stacks = new Throwable().getStackTrace();
        int stacksLen = stacks.length;
        sb.append("class: ").append(stacks[1].getClassName()).append("; method: ").append(stacks[1].getMethodName())
                .append("; number: ").append(stacks[1].getLineNumber());
        return sb.toString();
    }

    /**
     * 判断某一个类是否存在任务栈里面
     *
     * @return
     */
    public static boolean isExsitActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        ComponentName cmpName = intent.resolveActivity(context.getPackageManager());
        boolean flag = false;
        if (cmpName != null) { // 说明系统中存在这个activity
            ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
                    flag = true;
                    break;  //跳出循环，优化效率
                }
            }
        }
        return flag;
    }


    /**
     * 判断是否系统关闭了消息通知
     * true 可用 false 不可用
     */
    public static boolean areNotificationsEnabled(Context mContext) {
        return NotificationManagerCompat.from(mContext).areNotificationsEnabled();
    }

    /**
     * 显示和隐藏软键盘
     *
     * @param context
     * @param view    isShow 隐藏false 显示true
     */
    public static void hideOrShowSoft(Context context, EditText view, boolean isShow) {
        if (context != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (isShow) {
                if (!imm.isActive()) {
                    imm.showSoftInput(view, InputMethodManager.SHOW_FORCED); //强制显示键盘
                }
            } else {
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
                }
            }

        }

    }

    /**
     * Umeng 数据埋点
     *
     * @param context//环境
     * @param id          //事件id
     */
    public static void hidePointInUmg(Context context, String id) {
        MobclickAgent.onEvent(context, id);
    }

    //判断某一个类是否存在任务栈里面
    private boolean isExistMainActivity(Context context, Class<?> activity) {
        Intent intent = new Intent(context, activity);
        ComponentName cmpName = intent.resolveActivity(context.getPackageManager());
        boolean flag = false;
        if (cmpName != null) { // 说明系统中存在这个activity
            ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);  //获取从栈顶开始往下查找的10个activity
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
                    flag = true;
                    break;  //跳出循环，优化效率
                }
            }
        }
        return flag;
    }
}
