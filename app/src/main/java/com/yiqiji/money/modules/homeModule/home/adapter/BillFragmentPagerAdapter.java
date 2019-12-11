package com.yiqiji.money.modules.homeModule.home.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by ${huangweishui} on 2017/3/29.
 * address huang.weishui@71dai.com
 */
public class BillFragmentPagerAdapter extends MyFragmentPagerAdapter {
    private List<String> names;
    private List<Fragment> fragments;
    private FragmentManager fragmentManager;

    public BillFragmentPagerAdapter(FragmentManager fm, List<String> names, List<Fragment> fragments) {
        super(fm, names, fragments);
        fragmentManager = fm;
        this.names = names;
        this.fragments = fragments;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(fragments.get(position % fragments.size()).getView()); // 移出viewpager两边之外的page布局
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = fragments.get(position % fragments.size());
        if (!fragment.isAdded()) { // 如果fragment还没有added
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(fragment, fragment.getClass().getSimpleName());
            ft.commit();
            /**
             * 在用FragmentTransaction.commit()方法提交FragmentTransaction对象后
             * 会在进程的主线程中，用异步的方式来执行。
             * 如果想要立即执行这个等待中的操作，就要调用这个方法（只能在主线程中调用）。
             * 要注意的是，所有的回调和相关的行为都会在这个调用中被执行完成，因此要仔细确认这个方法的调用位置。
             */
            fragmentManager.executePendingTransactions();
        }

        if (fragment.getView().getParent() != null) {
            container.removeView(fragment.getView());

        }
        container.addView(fragment.getView()); // 为viewpager增加布局
        return fragment;
    }

    @Override
    public Fragment getItem(int arg0) {
        return super.getItem(arg0);
    }
}
