package com.yiqiji.money.modules.common.adapter.base;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class MyPagerAdapter extends PagerAdapter{
    private List<View> views;
    public MyPagerAdapter(List<View> views){
        this.views = views;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position %= views.size();
        ((ViewPager) container).removeView(views.get(position));
        ((ViewPager) container).addView(views.get(position));
        return views.get(position);
    }
    @Override
    public void destroyItem(ViewGroup container, int position,Object object) {
//        ((ViewPager) container).removeView(views.get(position));
    }
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }
    @Override
    public boolean isViewFromObject(View view,Object object) {
        return view == object;
    }
}
