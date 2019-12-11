package com.yiqiji.money.modules.myModule.my.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.utils.XzbUtils;

import java.util.ArrayList;
import java.util.List;

public class AboutXzbActivity extends BaseActivity {
    private ViewPager about;
    private Button welcome_button;
    private List<View> viewList;
    private List<ImageView> ivList = new ArrayList<ImageView>();
    private TextView version_number;
    private int[] icon = {R.drawable.about_1, R.drawable.about_2, R.drawable.about_3, R.drawable.about_4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutxzb);
        init();
        initData();
    }

    private void initData() {
        version_number.setText("一起记 " + XzbUtils.getVersion(this));
    }

    private void init() {
        initTitle("关于");
        version_number = (TextView) findViewById(R.id.version_number);
        about = (ViewPager) findViewById(R.id.about);
        setViewData();
    }

    /**
     * 设置数据
     */
    private void setViewData() {
        viewList = new ArrayList<View>();
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        // 初始化引导图片列表
        for (int i = 0; i < icon.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            // 防止图片不能填满屏幕
            iv.setScaleType(ScaleType.FIT_XY);
            // 加载图片资源
            XzbUtils.displayImage(iv, "drawable://" + icon[i], 0);
//            iv.setImageResource(icon[i]);
            viewList.add(iv);
            ivList.add(iv);
        }

        about.setAdapter(new mPageAdapter(viewList));

    }

    class mPageAdapter extends PagerAdapter {
        private List<View> mListViews;

        public mPageAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(mListViews.get(position));
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mListViews.get(position);
            ((ViewPager) container).addView(view, 0);
            return mListViews.get(position);
        }
    }

}
