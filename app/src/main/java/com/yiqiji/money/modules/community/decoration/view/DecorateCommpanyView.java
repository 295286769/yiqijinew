package com.yiqiji.money.modules.community.decoration.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.decoration.ActivityUtil;
import com.yiqiji.money.modules.community.decoration.model.DecorationcompanyBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import huangshang.com.yiqiji_imageloadermaniger.ImageLoaderManager;

/**
 * Created by dansakai on 2017/8/1.
 */

public class DecorateCommpanyView extends LinearLayout {
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.tv_cyName)
    TextView tvCyName;
    @BindView(R.id.tv_sorce)
    TextView tvSorce;
    @BindView(R.id.tv_value)
    TextView tvValue;
    @BindView(R.id.tv_addr)
    TextView tvAddr;
    private Context mContext;

    private DecorationcompanyBean commpanyBean;

    public DecorateCommpanyView(Context context) {
        this(context, null);
    }

    public DecorateCommpanyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.view_decorat_commpany, this);
        ButterKnife.bind(this);
        initEvent();
    }

    public void setData(DecorationcompanyBean bean) {
        this.commpanyBean = bean;
        ImageLoaderManager.loadImage(bean.getCompanylogo(), ivPhoto);
        tvCyName.setText(bean.getCompanyname());
        tvSorce.setText("总评分： "+bean.getServicescore());
        tvValue.setText("口碑值： "+bean.getPublicpraise());
        tvAddr.setText("地址： "+bean.getCompanyaddress());
    }

    private void initEvent() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到装修公司详情页
                ActivityUtil.startCommpantDetail(mContext,commpanyBean.getCompanyid());
            }
        });
    }
}
