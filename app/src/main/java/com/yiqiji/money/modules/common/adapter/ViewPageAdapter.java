package com.yiqiji.money.modules.common.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

/**
 * Created by dansakai on 2017/3/27.
 */

public class ViewPageAdapter extends PagerAdapter {
    // 界面列表
    private List<View> views = null;

    public ViewPageAdapter(List<View> views) {
        this.views = views;
    }

    // 获得当前界面总数
    @Override
    public int getCount() {
        if (views != null) {
            return views.size();
        }
        return 0;
    }

    // 初始化position位置的界面
    @Override
    public Object instantiateItem(View arg0, int arg1) {
        ((ViewPager) arg0).addView(views.get(arg1), 0);
        return views.get(arg1);
    }

    // 判断是否由对象生成界面
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    // 销毁position位置的界面
    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView(views.get(arg1));
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
    }
}
