package com.yiqiji.money.modules.community.decoration.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.decoration.model.DecorationcompanyBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dansakai on 2017/8/2.
 * 装修公司详情head
 */

public class CommpanyHeadView extends LinearLayout {

    @BindView(R.id.commpany_view)
    DecorateCommpanyView commpanyView;
    @BindView(R.id.tv_shortDescrib)
    TextView tvShortDescrib;
    @BindView(R.id.tv_bookTitle)
    TextView tvBookTitle;

    public CommpanyHeadView(Context context) {
        this(context, null);
    }

    public CommpanyHeadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.commpany_detail_head, this);
        ButterKnife.bind(this);
    }

    public void setData(DecorationcompanyBean companyBean) {
        commpanyView.setData(companyBean);
        tvShortDescrib.setText(companyBean.getCompanyprofile());
    }

    public void setBookTitle(String title) {
        tvBookTitle.setText(title);
    }
}
