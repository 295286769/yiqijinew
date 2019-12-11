package com.yiqiji.money.modules.community.travel.view;

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
import com.yiqiji.money.modules.Found.adapter.TabHScorllerAdapter;
import com.yiqiji.money.modules.Found.entity.TabListModel;
import com.yiqiji.money.modules.Found.view.BaseWebView;
import com.yiqiji.money.modules.Found.view.TabHSView;
import com.yiqiji.money.modules.common.activity.WebActivity;
import com.yiqiji.money.modules.community.travel.model.TravelMainListModel;
import com.yiqiji.money.modules.community.travel.travelinterface.BaseOnItemClickListener;
import com.yiqiji.money.modules.community.travel.travelinterface.BaseScorllerAdapter;
import com.yiqiji.money.modules.community.travel.travelinterface.HotPlaceInteface;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import huangshang.com.yiqiji_imageloadermaniger.ImageLoaderManager;

/**
 * Created by leichi on 2017/8/2.
 */

public class TravelMainListHeadView extends LinearLayout {

    Context context;
    @BindView(R.id.tv_book_num)
    TextView tvBookNum;

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

    @BindView(R.id.h_scroll_view)
    TabHSView hScrollView;
    private TravelMainListModel.PlaceListModel placeListModel;


    public TravelMainListHeadView(Context context) {
        this(context, null);
    }

    public TravelMainListHeadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_travel_main_head_layout, this);
        ButterKnife.bind(this);
    }

    public void setViewData(TravelMainListModel travelMainListModel) {
        DecimalFormat df = new DecimalFormat("00000000");//这样为保持2位
        tvBookNum.setText(df.format(Double.parseDouble(travelMainListModel.booktotal)));
        initPlaceListModel(travelMainListModel.place);
        initTabList(travelMainListModel.tab);
    }

    private void initPlaceListModel(TravelMainListModel.PlaceListModel placeListModel) {
        this.placeListModel = placeListModel;
        TravelMainListModel.PlaceModel placeModel1 = placeListModel.list.get(0);
        ImageLoaderManager.loadImage(placeModel1.img, image1);
        text1.setText(placeModel1.title);

        TravelMainListModel.PlaceModel placeModel2 = placeListModel.list.get(1);
        ImageLoaderManager.loadImage(placeModel2.img, image2);
        text2.setText(placeModel2.title);

        TravelMainListModel.PlaceModel placeModel3 = placeListModel.list.get(2);
        ImageLoaderManager.loadImage(placeModel3.img, image3);
        text3.setText(placeModel3.title);
    }

    public void setHotPalcePress(final HotPlaceInteface palcePress) {

        image1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TravelMainListModel.PlaceModel placeModel1 = placeListModel.list.get(0);
                palcePress.getInfo(placeModel1);
            }
        });
        image2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TravelMainListModel.PlaceModel placeModel1 = placeListModel.list.get(1);
                palcePress.getInfo(placeModel1);
            }
        });
        image3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TravelMainListModel.PlaceModel placeModel1 = placeListModel.list.get(2);
                palcePress.getInfo(placeModel1);
            }
        });
    }

    private void initTabList(TabListModel tab) {
        BaseScorllerAdapter adapter = new TabHScorllerAdapter(context, tab);
        hScrollView.setAdapter(adapter);
        hScrollView.setOnItemClick(new BaseOnItemClickListener<TabListModel.TabModel>() {
            @Override
            public void onItemClick(TabListModel.TabModel tabModel) {
                if (BaseWebView.overrideUrlLoading(context, tabModel.url)) {
                    return;
                }
                Intent in = new Intent(context, WebActivity.class);
                in.putExtra("url", tabModel.url);
                in.putExtra("title", tabModel.title);
                context.startActivity(in);
            }
        });
    }
}
