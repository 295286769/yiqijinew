package com.yiqiji.money.modules.community.discover.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.Found.activity.BannerDetailsActivity;
import com.yiqiji.money.modules.Found.entity.BannerAdEntity;
import com.yiqiji.money.modules.Found.entity.TabListModel;
import com.yiqiji.money.modules.Found.view.BaseWebView;
import com.yiqiji.money.modules.common.activity.WebActivity;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.community.decoration.ActivityUtil;
import com.yiqiji.money.modules.community.discover.model.DiscoverListModel;
import com.yiqiji.money.modules.community.utils.StartActivityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by leichi on 2017/7/31.
 */

public class HeadView extends LinearLayout {

    Context context;
    @BindView(R.id.banner)
    BGABanner banner;
    @BindView(R.id.linear_decoration)
    LinearLayout linearDecoration;
    @BindView(R.id.linear_travel)
    LinearLayout linearTravel;
    @BindView(R.id.linear_other)
    LinearLayout linearOther;

    @BindView(R.id.image1)
    ImageView image1;
    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.image2)
    ImageView image2;
    @BindView(R.id.text2)
    TextView text2;
    @BindView(R.id.image3)
    ImageView image3;
    @BindView(R.id.text3)
    TextView text3;

    private List<BannerAdEntity> listAds = new ArrayList<>();   //头部广告
    private DiscoverListModel mDiscoverListModel;
    public HeadView(Context context) {
        this(context, null);
    }

    public HeadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater.from(context).inflate(R.layout.discover_head_view_layout, this);
        ButterKnife.bind(this);
        initBannerView();
        initEvent();
    }

    private void initBannerView() {
        int width = UIHelper.getDisplayWidth((Activity) context);
        banner.getLayoutParams().height = (int) (width / 5 * 2);
        banner.setAdapter(new BGABanner.Adapter() {

            @Override
            public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
                // 加载图片
                ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER_CROP);
                XzbUtils.displayImage(((ImageView) view), ((BannerAdEntity) model).getImg(), 0);
            }
        });
        banner.setOnItemClickListener(new BGABanner.OnItemClickListener() {

            @Override
            public void onBannerItemClick(BGABanner banner, View view, Object model, int position) {
                BannerAdEntity entity = (BannerAdEntity) model;
                if (BaseWebView.overrideUrlLoading(context, entity.getUrl())) {
                    return;
                } else {
                    Intent intent = new Intent(context, BannerDetailsActivity.class);
                    intent.putExtra("AdEntity", entity);
                    ((Activity) context).startActivity(intent);
                }
            }
        });
    }

    private void initEvent() {
        linearDecoration.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.startDecorateHome(context);
            }
        });
        linearTravel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                StartActivityUtil.startTravelMain(context);
            }
        });

        linearOther.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TabListModel.TabModel model=mDiscoverListModel.nav.get(2);
                if(model.url.contains("http")){
                    Intent in = new Intent(context, WebActivity.class);
                    in.putExtra("url", model.url);
                    in.putExtra("title", model.title);
                    context.startActivity(in);
                }else {
                    StartActivityUtil.startDiscoverOther(context);
                }
            }
        });
    }

    public void setViewData(DiscoverListModel discoverListModel) {
        mDiscoverListModel=discoverListModel;
        banner.setData(discoverListModel.ads, null);
        List<TabListModel.TabModel> nav=discoverListModel.nav;
        text1.setText(nav.get(0).text);
        text2.setText(nav.get(1).text);
        text3.setText(nav.get(2).text);

        XzbUtils.displayImage(image1, nav.get(0).icon, 0);
        XzbUtils.displayImage(image2, nav.get(1).icon, 0);
        XzbUtils.displayImage(image3, nav.get(2).icon, 0);


    }
}
