package huangshang.com.yiqiji_imageloadermaniger.transformations.myappaplacation;

import android.app.Application;
import android.content.Context;

/**
 * Created by ${huangweishui} on 2017/7/14.
 * address huang.weishui@71dai.com
 */
public class MyAppAplacation extends Application {
    private static Context context;

    public static MyAppAplacation gtIntence() {
        return new MyAppAplacation();
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
