package com.yiqiji.money.modules.Found.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.travel.travelinterface.BaseOnItemClickListener;
import com.yiqiji.money.modules.community.travel.travelinterface.BaseScorllerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by leichi on 2017/6/6.
 */

public class TabHSView extends HorizontalScrollView {
    @BindView(R.id.discover_item_book_layout)
    LinearLayout discoverItemBookLayout;
    private Context mContext;
    private BaseScorllerAdapter tabHScorllerAdapter;


    public TabHSView(Context context) {
        this(context, null);
    }

    public TabHSView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabHSView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_discover_found_item_hs_layout, this, true);
        ButterKnife.bind(this, view);
    }

    public void setAdapter(BaseScorllerAdapter adapter) {
        tabHScorllerAdapter = adapter;
        adapter.initView(discoverItemBookLayout);
    }

    public void setOnItemClick(BaseOnItemClickListener onItemClick) {
        tabHScorllerAdapter.OnItemClickListener(onItemClick);
    }

}
