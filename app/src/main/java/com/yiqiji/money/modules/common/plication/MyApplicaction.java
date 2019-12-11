package com.yiqiji.money.modules.common.plication;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.multidex.MultiDex;
import android.view.Display;
import android.view.WindowManager;

import com.iflytek.cloud.SpeechUtility;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.yiqiji.frame.core.system.BaseApplication;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.db.DailycostDatabaseHelper;
import com.yiqiji.money.modules.common.entity.LocalImageHelper;
import com.yiqiji.money.modules.common.request.LocationServer;
import com.yiqiji.money.modules.common.request.QiniuServer;
import com.yiqiji.money.modules.common.utils.DisplayUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jpush.android.api.JPushInterface;


public class MyApplicaction extends BaseApplication {
    public List<Activity> activities = new ArrayList<Activity>();
    public static List<HashMap<String, String>> ids = new ArrayList<HashMap<String, String>>();
    public static List<HashMap<String, String>> urls = new ArrayList<HashMap<String, String>>();
    private static MyApplicaction app = new MyApplicaction();
    private Display display;
    public static boolean isThubm = false;
    public static DailycostDatabaseHelper mOpenHelper;
    private LocationServer locationServer;
    private Vibrator mVibrator;

    public static MyApplicaction getInstence() {
        if (app == null) {
            app = new MyApplicaction();
        }
        return app;
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public LocationServer getLocationServer() {
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        if (locationServer == null) {
            locationServer = new LocationServer(app);
        }

//        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
//        SDKInitializer.initialize(getApplicationContext());
        return locationServer;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        /**
         * 讯飞语音---初始化即创建语音配置对象
         */
        SpeechUtility.createUtility(MyApplicaction.this, "appid=" + getString(R.string.speech_app_id));
        // 以下语句用于设置日志开关（默认开启），设置成false时关闭语音云SDK日志打印
        // Setting.setShowLog(false);
        super.onCreate();
        if (RequsterTag.DEBUG) {// 设置开启日志,发布时请关闭日志
            MobclickAgent.setDebugMode(true);
            JPushInterface.setDebugMode(true);
        } else {
            MobclickAgent.setCatchUncaughtExceptions(true);
        }

        mContext = getApplicationContext();
        DisplayUtil.getInstance();
        mOpenHelper = new DailycostDatabaseHelper(getContext());
        init();
        UMShareAPI.get(this);
        LoginConfig.getInstance().setAppver(getPackageInfo().versionName);
        JPushInterface.init(this);            // 初始化 JPush
        QiniuServer.configQiniu();
        locationServer = new LocationServer(app);

      //设置友盟每次登录都需要回调确认
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(mContext).setShareConfig(config);
    }

    // 各个平台的配置，建议放在全局Application或者程序入口

    {

        PlatformConfig.setWeixin("wxb3f94e5eeb1ec343", "42ce891d81dfce8cbc0077d6516534b9");
        //   6.3版本之后在设置appid的时候一起设置回调地址：
        PlatformConfig.setSinaWeibo("2991138302", "2d94a8cb61968b4de341a95a10ab8aea", "http://sns.whalecloud.com/sina2/callback");
        PlatformConfig.setQQZone("1105887943", "R16wJxyaeKKnrdc9");


    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    // 遍历所有Activity并finish
    public void exit() {
        try {
            for (Activity activity : activities) {
                if (activity != null && activity instanceof Activity) {

                    activity.finish();
                }

            }
            activities.clear();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            // System.exit(0);
        }
    }

    /**
     * 初始化
     */
    private void init() {
        initImageLoader(getApplicationContext());
        // 本地图片辅助类初始化
        LocalImageHelper.init(this);
        if (display == null) {
            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            display = windowManager.getDefaultDisplay();
        }
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY);
        config.denyCacheImageMultipleSizesInMemory();
        config.memoryCacheSize((int) Runtime.getRuntime().maxMemory() / 4);
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(100 * 1024 * 1024); // 100 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        // 修改连接超时时间5秒，下载超时时间5秒
        config.imageDownloader(new BaseImageDownloader(mContext, 5 * 1000, 5 * 1000));
        // config.writeDebugLogs(); // Remove for release app
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public String getCachePath() {
        File cacheDir;
        // if
        // (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
        // cacheDir = getExternalCacheDir();
        // else
        cacheDir = getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();
        return cacheDir.getAbsolutePath();
    }

    /**
     * @return
     * @Description： 获取当前屏幕1/4宽度
     */
    public int getQuarterWidth() {
        return display.getWidth() / 4;
    }

    public float getScreenWith() {
        if (display == null) {
            WindowManager windowManager = (WindowManager) getSystemService(Activity.WINDOW_SERVICE);
            display = windowManager.getDefaultDisplay();
        }
        return display.getWidth();

    }

    private PackageInfo getPackageInfo() {
        PackageInfo pi = null;
        try {
            PackageManager pm = this.getPackageManager();
            pi = pm.getPackageInfo(this.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

}
