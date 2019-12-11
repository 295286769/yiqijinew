package com.yiqiji.money.modules.community.travel.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.frame.ui.wigit.BaseTitleLayout;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.widget.BaseRecylerview;
import com.yiqiji.money.modules.community.travel.adapter.RaidersAdapter;
import com.yiqiji.money.modules.community.travel.manager.DestinationGuideDataModleManager;
import com.yiqiji.money.modules.community.travel.manager.TravelDataManager;
import com.yiqiji.money.modules.community.travel.model.RaidersDataItemBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${huangweishui} on 2017/8/3.
 * address huang.weishui@71dai.com
 */
public class RaidersActivity extends BaseActivity {
    @BindView(R.id.title)
    BaseTitleLayout title;
    @BindView(R.id.list_raiders)
    BaseRecylerview listRaiders;
    @BindView(R.id.tv_city)
    TextView tv_city;
    private RaidersAdapter raidersAdapter;
    private String title_name;
    private String city;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raiders_layout);
        ButterKnife.bind(this);
        getIntentInfo();
        initAdapter();
        initData();
    }

    private void getIntentInfo() {
        city = getIntent().getStringExtra("city");
        title_name = getIntent().getStringExtra("title");
        position = getIntent().getIntExtra("position", 0);
    }

    private void initAdapter() {
        raidersAdapter = new RaidersAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listRaiders.setLayoutManager(linearLayoutManager);
        listRaiders.setAdapter(raidersAdapter);

    }

    private void initData() {
        title.setTitle(title_name);
        tv_city.setText(city);
        TravelDataManager.getRaidersInfo(city, position, new ViewCallBack<List<RaidersDataItemBean>>() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                showToast(simleMsg);
            }

            @Override
            public void onSuccess(List<RaidersDataItemBean> o) throws Exception {
                super.onSuccess(o);
                raidersAdapter.setDataList(DestinationGuideDataModleManager.getRaidersMultiItem(o));
                raidersAdapter.expand(0);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissDialog();
            }
        });
    }
}
