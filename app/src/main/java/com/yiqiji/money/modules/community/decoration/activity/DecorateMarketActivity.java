package com.yiqiji.money.modules.community.decoration.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dansakai on 2017/8/3.
 * 装修行情统计
 */

public class DecorateMarketActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_statics);
        ButterKnife.bind(this);
        tvTitle.setText("统计");
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
