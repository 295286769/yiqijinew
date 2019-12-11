package com.yiqiji.money.modules.book.detailinfo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiqiji.money.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by leichi on 2017/6/16.
 */

public class FollowView extends LinearLayout {

    Context mContext;

    @BindView(R.id.img_follow)
    TextView imgFollow;
    @BindView(R.id.tv_follow)
    TextView tvFollow;

    public FollowView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public FollowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.view_follow_layout, this);
        ButterKnife.bind(this);
    }

    public void bindData(String id,String state){

    }
}
