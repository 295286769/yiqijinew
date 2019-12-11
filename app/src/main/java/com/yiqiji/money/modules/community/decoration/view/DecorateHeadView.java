package com.yiqiji.money.modules.community.decoration.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.decoration.ActivityUtil;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dansakai on 2017/8/2.
 * 装修社区首页头部
 */

public class DecorateHeadView extends LinearLayout {
    @BindView(R.id.tv_bookNm)
    TextView tvBookNm;
    @BindView(R.id.tv_curCity)
    TextView tvCurCity;
    @BindView(R.id.tv_commpanyNm)
    TextView tvCommpanyNm;
    private Context mContext;

    public DecorateHeadView(Context context) {
        this(context, null);
    }

    public DecorateHeadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.include_decooration_head, this);
        ButterKnife.bind(this);
    }

    public void setData(String bookNm, String curCity, String commpanyNm) {
        DecimalFormat df = new DecimalFormat("00000000");//这样为保持2位
        tvBookNm.setText(df.format(Double.parseDouble(bookNm)));
        tvCurCity.setText(curCity);
        tvCommpanyNm.setText(commpanyNm);
    }

    @OnClick({R.id.rl_changeCity, R.id.tv_statist, R.id.rl_decorateCommpany})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_changeCity:
                ActivityUtil.startDecorateNearBook(mContext);
                break;
            case R.id.tv_statist:
                ActivityUtil.startDecorateMarket(mContext);
                break;
            case R.id.rl_decorateCommpany:
                ActivityUtil.startCommpanyList(mContext);
                break;
        }
    }
}
