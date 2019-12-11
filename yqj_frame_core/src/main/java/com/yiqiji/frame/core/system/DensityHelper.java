package com.yiqiji.frame.core.system;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by xiaolong on 2017/4/27.
 */

public class DensityHelper {


    public static int getWidthOfTheScreen(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    public static int getHeightOfTheScreen(Context context){
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

}
