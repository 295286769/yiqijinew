package com.yiqiji.money.modules.homeModule.home.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

public class TabAdapter implements View.OnClickListener {
    // tab fragment的集合
    private List<Fragment> fragments;
    // Activity中需要被Fragment替换区域的id
    private int fragment_id;
    private FragmentActivity fragmentActivity;
    // 当前fragment的下标
    private int currentTab;
    private RadioGroup tabGroup;
    // 回调的接口
    public OnTabChangeListener onTachangeListener;

    public void setTachangeListener(OnTabChangeListener onTabChangeListener) {
        this.onTachangeListener = onTabChangeListener;
    }

    public TabAdapter(FragmentActivity fragmentActivity, RadioGroup tabGroup,
                      int fragment_id, List<Fragment> fragments, int currentTab) {
        this.fragments = fragments;
        this.fragmentActivity = fragmentActivity;
        this.tabGroup = tabGroup;
        this.currentTab = currentTab;
        this.fragment_id = fragment_id;
        ((RadioButton) tabGroup.getChildAt(0)).setChecked(true);
        for (int i = 0; i < tabGroup.getChildCount(); i++) {
            tabGroup.getChildAt(i).setOnClickListener(this);
        }

        // 显示当前的fragment
        FragmentTransaction transaction = getFragmentTransaction();
        transaction.add(fragment_id, fragments.get(currentTab));
        transaction.commitAllowingStateLoss();
    }

    /**
     * 获取FragmentTransaction
     *
     * @return
     */
    public FragmentTransaction getFragmentTransaction() {
        // 默认显示第一页
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager()
                .beginTransaction();
        return ft;

    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < tabGroup.getChildCount(); i++) {
            if (tabGroup.getChildAt(i).getId() == v.getId()) {
                currentTab = i;
                ((RadioButton) tabGroup.getChildAt(i)).setChecked(true);
                Fragment fragment = fragments.get(i);
                FragmentTransaction fragmentTransaction = getFragmentTransaction();
                getCurrentFragment().onStop();
                // 如果当前需要显示的fragment已被添加进事务则直接调用onResume()，否则将需要添加的fragment添加进事务
                if (fragment != null && fragment.isAdded()) {
                    if (onTachangeListener != null) {
                        onTachangeListener.onTabChange(tabGroup, v.getId(),
                                fragment, i);
                    }
                    fragment.onStart();
                } else {
                    // fragment没有被添加进事物则需要重新添加
                    fragmentTransaction.add(fragment_id, fragments.get(i));
                    if (onTachangeListener != null) {
                        onTachangeListener.onTabChange(tabGroup, v.getId(),
                                fragment, i);
                    }

                }
                // 显示要显示的tab
                show(i);
                // 提交事务
                fragmentTransaction.commitAllowingStateLoss();
            } else {
                ((RadioButton) tabGroup.getChildAt(i)).setChecked(false);
            }
        }


    }

    public interface OnTabChangeListener {
        void onTabChange(RadioGroup group, int checkedId, Fragment fragment,
                         int index);
    }

//    @Override
//    public void onCheckedChanged(RadioGroup group, int checkedId) {
//        for (int i = 0; i < tabGroup.getChildCount(); i++) {
//            if (tabGroup.getChildAt(i).getId() == checkedId) {
//                Fragment fragment = fragments.get(i);
//                FragmentTransaction fragmentTransaction = getFragmentTransaction();
//                getCurrentFragment().onStop();
//                // 如果当前需要显示的fragment已被添加进事务则直接调用onResume()，否则将需要添加的fragment添加进事务
//                if (fragment != null && fragment.isAdded()) {
//                    if (onTachangeListener != null) {
//                        onTachangeListener.onTabChange(group, checkedId,
//                                fragment, i);
//                    }
//                    fragment.onStart();
//                } else {
//                    // fragment没有被添加进事物则需要重新添加
//                    fragmentTransaction.add(fragment_id, fragments.get(i));
//                    if (onTachangeListener != null) {
//                        onTachangeListener.onTabChange(group, checkedId,
//                                fragment, i);
//                    }
//
//                }
//                // 显示要显示的tab
//                show(i);
//                // 提交事务
//                fragmentTransaction.commitAllowingStateLoss();
//            }
//        }
//
//    }

    /**
     * 显示要显示的tab
     *
     * @param i
     */
        private void show(int i) {
        for (int j = 0; j < fragments.size(); j++) {
            FragmentTransaction fragmentTransaction = getFragmentTransaction();
            Fragment fragment = fragments.get(j);
            if (i == j) {
                fragmentTransaction.show(fragment);
            } else {
                fragmentTransaction.hide(fragment);
            }
            fragmentTransaction.commitAllowingStateLoss();
        }

    }

    /**
     * 获取当前的ragment
     *
     * @param
     * @return
     */
    public Fragment getCurrentFragment() {
        return fragments.get(currentTab);

    }

    /**
     * 获取当前的ragment下标index
     *
     * @param
     * @return
     */
    public int getCurrentFragmentIndex() {
        return currentTab;


    }

}
