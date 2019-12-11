package com.yiqiji.money.modules.community.discover.activity;

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
import com.yiqiji.money.modules.community.discover.manager.DiscoverListController;
import com.yiqiji.money.modules.community.discover.manager.DiscoverOtherListDataAssembler;
import com.yiqiji.money.modules.community.discover.model.DiscoverOtherListModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by leichi on 2017/7/31.
 */

public class DiscoverOtherActivity extends BaseActivity {


    @BindView(R.id.iv_details_return)
    ImageView ivDetailsReturn;
    @BindView(R.id.ryv_discover_other)
    RecyclerView ryvDiscoverOther;

    DiscoverOtherListAdapter adapter;
    LoadingDialog loadingDialog;
    DiscoverListController discoverListController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_other_layout);
        ButterKnife.bind(this);
        initView();
        initEvent();
        loadData();
    }

    private void initView() {
        loadingDialog = new LoadingDialog(this);
        discoverListController = new DiscoverListController();
        initRecycleView();
    }

    private void initEvent(){
        ivDetailsReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initRecycleView() {
        adapter = new DiscoverOtherListAdapter(this);
        ryvDiscoverOther.setLayoutManager(new LinearLayoutManager(this));
        ryvDiscoverOther.setAdapter(adapter);
    }

    private void loadData() {
        loadingDialog.show();
        discoverListController.getDiscoverOtherListData(new ViewCallBack<DiscoverOtherListModel>() {
            @Override
            public void onSuccess(DiscoverOtherListModel discoverOtherListModel) throws Exception {
                super.onSuccess(discoverOtherListModel);
                adapter.setDataList(DiscoverOtherListDataAssembler.getAssemblerMultipItem(discoverOtherListModel.accountbook.list));
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
