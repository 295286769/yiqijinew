package com.yiqiji.money.modules.homeModule.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.money.modules.common.utils.DownUrlUtil;
import com.yiqiji.money.modules.common.widget.myviewpager.NoScrollViewPager;
import com.yiqiji.money.modules.common.widget.myviewpager.TabPageIndicator;
import com.yiqiji.money.modules.common.widget.myviewpager.TabPageIndicator.OnTabReselectedListener;
import com.yiqiji.money.modules.homeModule.home.adapter.MyFragmentPagerAdapter;
import com.yiqiji.money.modules.homeModule.home.fragment.BillIFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 账单详情页 传mType 0表示收入，1表示支出 private int mType;
 *
 * @author Administrator
 */
public class BillActivity extends BaseActivity implements OnTabReselectedListener {
    private LinearLayout layout_root;// 页面加载完成显示布局
    private TabPageIndicator tabPageIndicator;
    private RadioGroup radio_group_details;
    private Calendar calendar = Calendar.getInstance();
    private List<Fragment> fragments;
    private List<String> names;
    private int moths = 120;
    private Date date_time;
    private FragmentStatePagerAdapter pagerAdapter;
    private NoScrollViewPager noScrollViewPager;
    private int current_moths = 70;
    private int mType;
    private String bookid = "";
    private String bookName = "";
    private String sorttype = "";
    private String mAccountbookcatename = "";
    private String accountbooktype = "";
    private String accountbookcount = "";

    public static void startActivity(Context mContext, int mType, String mAccountbooktitle, String mAccountbookid, String mSorttype, String mAccountbookcatename, String mAccountbooktype, String mAccountbookcount) {
        Intent intent = new Intent(mContext, BillActivity.class);
        intent.putExtra("Type", mType);
        intent.putExtra("bookName", mAccountbooktitle);
        intent.putExtra("bookid", mAccountbookid);
        intent.putExtra("sorttype", mSorttype);
        intent.putExtra("mAccountbookcatename", mAccountbookcatename);
        intent.putExtra("accountbooktype", mAccountbooktype);
        intent.putExtra("accountbookcount", mAccountbookcount);
        mContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        mType = getIntent().getIntExtra("Type", 1);
        bookid = getIntent().getStringExtra("bookid");
        bookName = getIntent().getStringExtra("bookName");
        sorttype = getIntent().getStringExtra("sorttype");
        mAccountbookcatename = getIntent().getStringExtra("mAccountbookcatename");
        accountbooktype = getIntent().getStringExtra("accountbooktype");
        accountbookcount = getIntent().getStringExtra("accountbookcount");
        String titleNmae = "";

        if (mType == 1) {
            if (mAccountbookcatename.equals("结婚账本")) {
                titleNmae = "结婚支出";
                initTitle("结婚支出");
            } else {
                initTitle(bookName + "-支出账单");
                titleNmae = bookName + "-支出账单";
            }

        } else {

            if (mAccountbookcatename.equals("结婚账本")) {
                initTitle(bookName + "-结婚收入");
                titleNmae = bookName + "-结婚收入";
            } else {
                initTitle(bookName + "-收入账单");
                titleNmae = bookName + "-收入账单";
            }
        }
        unrestrictedTitleLenth(titleNmae);
        initView();
        DownUrlUtil.searchTime(bookid, handler, RequsterTag.SEARCHTIME);
        initData();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            Message message = Message.obtain();
            message.what = 0;
            handler.sendMessage(message);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            switch (msg.what) {
                case RequsterTag.SEARCHTIME:
                    initTabFragment(msg);
                    initPager();
                    break;
                case 0:
                    layout_root.setVisibility(View.VISIBLE);
                    break;

                default:
                    break;
            }
        }
    };

    private void initView() {
        date_time = new Date();
        calendar.setTime(date_time);
        noScrollViewPager = (NoScrollViewPager) findViewById(R.id.pager);
        tabPageIndicator = (TabPageIndicator) findViewById(R.id.tabPageIndicator);
        radio_group_details = (RadioGroup) findViewById(R.id.radio_group_details);
        layout_root = (LinearLayout) findViewById(R.id.layout_root);
        radio_group_details.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                switch (checkedId) {
                    case R.id.rb_details_expenditure:// 支出
                        ((BillIFragment) fragments.get(current_moths)).setType(1);
                        break;
                    case R.id.rb_details_income:// 收入
                        ((BillIFragment) fragments.get(current_moths)).setType(0);
                        break;
                    default:
                        break;

                }
                ft.commit();
            }
        });

    }

    private void initData() {
        fragments = new ArrayList<Fragment>();
        names = new ArrayList<String>();
        calendar.setTime(date_time);

        // tabPageIndicator.notifyDataSetChanged();

    }

    private void initPager() {
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), names, fragments);
        noScrollViewPager.setAdapter(pagerAdapter);
        noScrollViewPager.setNoScroll(false);
        tabPageIndicator.setViewPager(noScrollViewPager);
        tabPageIndicator.setOnTabReselectedListener(this);
        tabPageIndicator.setCurrentItem(current_moths);
        if (sorttype.equals("0")) {
            tabPageIndicator.setVisibility(View.GONE);
            noScrollViewPager.setNoScroll(true);
        } else {
            tabPageIndicator.setVisibility(View.VISIBLE);
        }
    }

    private void initTabFragment(Message msg) {
        long cTime = (long) msg.obj;
        calendar.setTime(date_time);
        int moth = calendar.get(Calendar.MONTH) + 1;//
        if (names != null && names.size() > 0) {
            names.clear();
        }
        if (fragments != null && fragments.size() > 0) {
            fragments.clear();
        }
        for (int i = 0; i < moths; i++) {

            long calendar_time = DateUtil.stringToTime(calendar.getTimeInMillis());// 开始时间
            names.add(calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月");
            BillIFragment fragment = new BillIFragment(this, date_time, mType, bookid, sorttype, accountbooktype, accountbookcount);
            fragments.add(fragment);

            if (calendar_time < cTime) {// 显示记账的前一个月
                calendar.add(Calendar.MONTH, -1);

                names.add(calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月");
                BillIFragment fragment2 = new BillIFragment(this, date_time, mType, bookid, sorttype, accountbooktype, accountbookcount);
                fragments.add(fragment2);
                break;
            }
            calendar.add(Calendar.MONTH, -1);
        }
        Collections.reverse(names);
        Collections.reverse(fragments);
        calendar.setTime(date_time);

        names.add("预设的");
        current_moths = names.size() - 2;
        BillIFragment fragment = new BillIFragment(this, date_time, mType, bookid, sorttype, accountbooktype, accountbookcount);
        fragments.add(fragment);
    }

    @Override
    public void onTabReselected(int position) {
        // TODO Auto-generated method stub
        calendar.add(Calendar.MONTH, position - current_moths);
        date_time = calendar.getTime();
        current_moths = position;
        ((BillIFragment) fragments.get(current_moths)).initData(date_time, sorttype);

    }

}
