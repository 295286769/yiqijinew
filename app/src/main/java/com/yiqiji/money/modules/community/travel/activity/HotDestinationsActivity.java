package com.yiqiji.money.modules.community.travel.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.widget.BaseRecylerview;
import com.yiqiji.money.modules.community.travel.adapter.HotDestinationsAdapter;
import com.yiqiji.money.modules.community.travel.manager.DestinationGuideDataModleManager;
import com.yiqiji.money.modules.community.travel.manager.TravelDataManager;
import com.yiqiji.money.modules.community.travel.model.HotDestinationsInfo;
import com.yiqiji.money.modules.community.travel.model.HotPlace;
import com.yiqiji.money.modules.community.travel.view.HotDestinationsHeadView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ${huangweishui} on 2017/8/2.
 * address huang.weishui@71dai.com
 */
public class HotDestinationsActivity extends BaseActivity {
    @BindView(R.id.list_hot)
    BaseRecylerview listHot;//
    private Unbinder unbinder;
    private HotDestinationsAdapter hotDestinationsAdapter;
    private HotDestinationsHeadView hotDestinationsHeadView;
    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_destinations_layout);
        unbinder = ButterKnife.bind(this);
        getIntentInfo();
        initAdapter();
        initDtata();
    }

    private void getIntentInfo() {
        city = getIntent().getStringExtra("city");
    }

    private void initAdapter() {
        hotDestinationsAdapter = new HotDestinationsAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listHot.setLayoutManager(linearLayoutManager);
        hotDestinationsHeadView = new HotDestinationsHeadView(this);
        hotDestinationsAdapter.addHeaderView(hotDestinationsHeadView);
        listHot.setAdapter(hotDestinationsAdapter);
    }

    private void initDtata() {
        TravelDataManager.getDestinationData(city, new ViewCallBack<HotDestinationsInfo.DataBean>() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(HotDestinationsInfo.DataBean dataBean) throws Exception {
                super.onSuccess(dataBean);
                HotPlace hotPlace = dataBean.getPlace();
                hotDestinationsHeadView.setHotDestinationsHeadImageView(hotPlace.getImg());
                hotDestinationsHeadView.setCity(hotPlace.getTitle());
                hotDestinationsHeadView.setDestinationGuideView(dataBean.getColumn(), hotPlace.getTitle());
                hotDestinationsAdapter.setDataList(DestinationGuideDataModleManager.getHotDestinationsMultiitem(dataBean.getHotbook()));
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                showToast(simleMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissDialog();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
