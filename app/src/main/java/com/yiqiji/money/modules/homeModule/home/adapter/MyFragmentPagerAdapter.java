package com.yiqiji.money.modules.homeModule.home.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private List<String> names;
    private List<Fragment> fragments;
    private FragmentManager fragmentManager;

    public MyFragmentPagerAdapter(FragmentManager fm, List<String> names,
                                  List<Fragment> fragments) {
        super(fm);
        fragmentManager = fm;
        this.names = names;
        this.fragments = fragments;
    }


    @Override
    public Fragment getItem(int arg0) {


        return fragments.get(arg0);
    }

    @Override
    public int getCount() {
        if (names == null) {
            return 0;
        }
        return names.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // TODO Auto-generated method stub
        return names.get(position);
    }


//    private int mChildCount = 0;
//
//    @Override
//    public void notifyDataSetChanged() {
//        mChildCount = getCount();
//        super.notifyDataSetChanged();
//    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
