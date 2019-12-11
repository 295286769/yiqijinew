package com.yiqiji.money.modules.community.travel.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.utils.LoadingDialog;
import com.yiqiji.money.modules.community.discover.adapter.DiscoverOtherListAdapter;
import com.yiqiji.money.modules.community.travel.manager.TravelDataManager;
import com.yiqiji.money.modules.community.travel.manager.TravelListDataAssembler;
import com.yiqiji.money.modules.community.travel.model.TravelMainListModel;
import com.yiqiji.money.modules.community.travel.travelinterface.HotPlaceInteface;
import com.yiqiji.money.modules.community.travel.view.TravelMainListHeadView;
import com.yiqiji.money.modules.community.utils.StartActivityUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by leichi on 2017/8/2.
 */

public class TravelMainActivity extends BaseActivity {

    @BindView(R.id.iv_details_return)
    ImageView ivDetailsReturn;
    @BindView(R.id.ryv_travel_main)
    RecyclerView ryvDiscoverOther;
    TravelMainListHeadView travelMainListHeadView;
    DiscoverOtherListAdapter adapter;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_main_layout);
        ButterKnife.bind(this);
        initView();
        initEvent();
        loadData();
    }

    private void initView() {
        loadingDialog = new LoadingDialog(this);
        travelMainListHeadView = new TravelMainListHeadView(this);
        initRecycleView();
    }

    private void initEvent() {
        ivDetailsReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        travelMainListHeadView.setHotPalcePress(new HotPlaceInteface<TravelMainListModel.PlaceModel>() {
            @Override
            public void getInfo(TravelMainListModel.PlaceModel placeModel) {
                super.getInfo(placeModel);
                StartActivityUtil.startHotDestinationsActivity(TravelMainActivity.this, placeModel.title);
            }
        });
    }

    private void initRecycleView() {
        adapter = new DiscoverOtherListAdapter(this);
        adapter.addHeaderView(travelMainListHeadView);
        ryvDiscoverOther.setLayoutManager(new LinearLayoutManager(this));
        ryvDiscoverOther.setAdapter(adapter);
    }

    private void loadData() {
        loadingDialog.show();
        TravelDataManager.getTravleMainListData(new ViewCallBack<TravelMainListModel>() {
            @Override
            public void onSuccess(TravelMainListModel travelMainListModel) throws Exception {
                super.onSuccess(travelMainListModel);
                adapter.setDataList(TravelListDataAssembler.getAssemblerMultipItem(travelMainListModel.hotbook));
                travelMainListHeadView.setViewData(travelMainListModel);
                loadingDialog.dismiss();
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                loadingDialog.dismiss();
            }
        });

    }
}
