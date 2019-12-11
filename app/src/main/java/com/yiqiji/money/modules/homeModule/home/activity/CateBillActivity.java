package com.yiqiji.money.modules.homeModule.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.db.DailycostEntity;
import com.yiqiji.money.modules.common.widget.BaseRecylerview;
import com.yiqiji.money.modules.homeModule.home.adapter.CateBillAdapter;
import com.yiqiji.money.modules.homeModule.home.perecenter.CateBillPerecenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${huangweishui} on 2017/7/6.
 * address huang.weishui@71dai.com
 */
public class CateBillActivity extends BaseActivity implements CateBillAdapter.OnItemClickListener {
    private BaseRecylerview bill_list;
    private View not_data;
    private String accounbooktid;
    private String cateid;
    private String billcatename;
    private String sortType;
    private long trdeTime;
    private CateBillAdapter cateBillAdapter;
    private List<DailycostEntity> dailycostEntities = new ArrayList<>();
    private boolean isSubscribe;


    public static void openActivity(Context context, String accounbooktid, String
            sortType, String cateid, String billcatename, long trdeTime, boolean isSubscribe) {
        Intent intent = new Intent(context, CateBillActivity.class);
        intent.putExtra("id", accounbooktid);
        intent.putExtra("sortType", sortType);
        intent.putExtra("cid", cateid);
        intent.putExtra("billcatename", billcatename);
        intent.putExtra("trdeTime", trdeTime);
        intent.putExtra("isSubscribe", isSubscribe);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cate_bill);
        getIntentDate();
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void getIntentDate() {
        accounbooktid = getIntent().getStringExtra("id");
        sortType = getIntent().getStringExtra("sortType");
        cateid = getIntent().getStringExtra("cid");
        billcatename = getIntent().getStringExtra("billcatename");
        trdeTime = getIntent().getLongExtra("trdeTime", 0);
        isSubscribe = getIntent().getBooleanExtra("isSubscribe", false);
    }

    private void initView() {
        bill_list = (BaseRecylerview) findViewById(R.id.bill_list);
        not_data = (View) findViewById(R.id.not_data);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        bill_list.setLayoutManager(linearLayoutManager);
        cateBillAdapter = new CateBillAdapter(this);
        bill_list.setAdapter(cateBillAdapter);
        cateBillAdapter.setOnItemClickListener(this);
    }

    private void initData() {
        CateBillPerecenter.getCateBill(this, accounbooktid, sortType, cateid, trdeTime, new ViewCallBack<List<DailycostEntity>>() {
            @Override
            public void onSuccess(List<DailycostEntity> dailycostEntities) throws Exception {
                super.onSuccess(dailycostEntities);
                if (dailycostEntities.size() == 0) {
                    not_data.setVisibility(View.VISIBLE);
                } else {
                    not_data.setVisibility(View.GONE);
                }
                CateBillActivity.this.dailycostEntities.clear();
                for (int i = 0; i < dailycostEntities.size(); i++) {
                    CateBillActivity.this.dailycostEntities.add(dailycostEntities.get(i));
                }
                String cate_name = "类别详情" + "(" + dailycostEntities.size() + "笔" + ")";
                initTitle(cate_name);
                unrestrictedTitleLenth(cate_name);
                cateBillAdapter.setDataList(CateBillActivity.this.dailycostEntities);
                cateBillAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                showToast(simleMsg);
            }

            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissDialog();
            }
        });
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        DailycostEntity dailycostEntity = dailycostEntities.get(position);
        PaymentDetailsActivity.openActivity(this, accounbooktid, dailycostEntity, isSubscribe, true);
    }
}
