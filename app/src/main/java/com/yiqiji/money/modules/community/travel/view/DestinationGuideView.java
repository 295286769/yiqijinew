package com.yiqiji.money.modules.community.travel.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.decoration.GridSpacingItemDecoration;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.widget.BaseRecylerview;
import com.yiqiji.money.modules.community.travel.adapter.DestinationGuideAdapter;
import com.yiqiji.money.modules.community.travel.model.HotPlace;
import com.yiqiji.money.modules.community.utils.StartActivityUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${huangweishui} on 2017/8/2.
 * address huang.weishui@71dai.com
 */
public class DestinationGuideView extends LinearLayout {
    @BindView(R.id.guide_view)
    BaseRecylerview guideView;
    private Context mContext;
    private DestinationGuideAdapter destinationGuideAdapter;
    private List<HotPlace> hotPlaces;
    private String city;

    public DestinationGuideView(Context context) {
        this(context, null);
    }

    public DestinationGuideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DestinationGuideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        initGroup();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_hot_destination_guide_layout, this, true);
        ButterKnife.bind(this, view);
    }

    public void setDataInfo(List<HotPlace> hotPlaces, String city) {
        this.hotPlaces = hotPlaces;
        this.city = city;
        destinationGuideAdapter.setDataList(hotPlaces);

    }

    public void initGroup() {
        destinationGuideAdapter = new DestinationGuideAdapter(mContext);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        guideView.setLayoutManager(gridLayoutManager);
        guideView.addItemDecoration(new GridSpacingItemDecoration(3, UIHelper.dip2px(mContext, 15), true));
        guideView.setAdapter(destinationGuideAdapter);
        destinationGuideAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                StartActivityUtil.startHotDestinationsActivity(mContext, city, hotPlaces.get(position).getTitle(), position);
            }
        });
    }
}
