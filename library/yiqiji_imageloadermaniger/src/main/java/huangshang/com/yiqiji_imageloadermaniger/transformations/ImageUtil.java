package huangshang.com.yiqiji_imageloadermaniger.transformations;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by ${huangweishui} on 2017/7/14.
 * address huang.weishui@71dai.com
 */
public class ImageUtil {
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
}
