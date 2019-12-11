package com.yiqiji.frame.core.system;

import android.app.Application;
import android.content.Context;

/**
 * Created by leichi on 2017/7/27.
 */

public class BaseApplication extends Application {

    public static Context mContext;

    private static BaseApplication app;

    public static BaseApplication getInstence() {
        if (app == null) {
            app = new BaseApplication();
        }
        return app;
    }

    public static Context getContext() {

        return mContext;

    }
}
