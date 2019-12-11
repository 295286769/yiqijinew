package com.yiqiji.money.modules.homeModule.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.db.DailycostEntity;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.money.modules.common.utils.DownUrlUtil;
import com.yiqiji.money.modules.common.utils.GsonUtil;
import com.yiqiji.money.modules.common.widget.PinnedHeaderListView;
import com.yiqiji.money.modules.homeModule.home.adapter.SettledDetailAdapter;
import com.yiqiji.money.modules.homeModule.home.entity.BaserClassMode;
import com.yiqiji.money.modules.homeModule.home.entity.HasSettementBillInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SettledDetailActivity extends BaseActivity implements OnClickListener, OnLongClickListener {
    private PinnedHeaderListView listview;
    private View not_data;
    private SettledDetailAdapter adapter;
    private List<DailycostEntity> dailycostEntities;
    private String titleName = "";
    private String id;
    private Date date;
    private HasSettementBillInfo billListInfo;
    public static void openActivity(Context context,String id,String titleName,String isClear){
       Intent intent = new Intent(context, SettledDetailActivity.class);
        intent.putExtra("titleName", titleName);
        intent.putExtra("id", id);
        intent.putExtra("isClear", isClear);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settled_detail);
        titleName = getIntent().getStringExtra("titleName");
        id = getIntent().getStringExtra("id");
        date = new Date();
        initView();
        initTitle();
        initAdapter();
        // initData();
        if ("已删除明细".equals(titleName)) {
            initDeletInterData();
        } else {
            initSettInterData();
        }

    }

    private void initSettInterData() {
        HashMap<String, String> hashMap = initparamasSettement();
        CommonFacade.getInstance().exec(Constants.CLEARED_BILL, hashMap, new ViewCallBack<HasSettementBillInfo>() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog(true);
            }

            @Override
            public void onSuccess(HasSettementBillInfo o) throws Exception {
                super.onSuccess(o);
                HasSettementBillInfo billListInfo = o;
                List<DailycostEntity> billInfo = billListInfo.getData();
                if (dailycostEntities != null && dailycostEntities.size() > 0) {
                    dailycostEntities.clear();
                }

                for (int i = 0; i < billInfo.size(); i++) {

                    dailycostEntities.add(billInfo.get(i));
                }
                setVisibleData();
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }

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

    private void setVisibleData() {
        if (dailycostEntities.size() > 0) {
            not_data.setVisibility(View.GONE);
        } else {
            not_data.setVisibility(View.VISIBLE);

        }
    }

    /**
     *
     */
    private void initDeletInterData() {
        HashMap<String, String> hashMap = initparamas();
        CommonFacade.getInstance().exec(Constants.BILL_IS_DELET, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog(true);
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                HasSettementBillInfo billListInfo = GsonUtil.GsonToBean(o.toString(), HasSettementBillInfo.class);
                List<DailycostEntity> billInfo = billListInfo.getData();
                if (billInfo == null) {
                    return;
                }
                if (dailycostEntities != null && dailycostEntities.size() > 0) {
                    dailycostEntities.clear();
                }

                for (int i = 0; i < billInfo.size(); i++) {
                    dailycostEntities.add(billInfo.get(i));
                }
                setVisibleData();
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
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

    private HashMap<String, String> initparamas() {
        long start_time = DateUtil.stringToTime(date.getTime()) / 1000;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);

        long end_time = (DateUtil.stringToTime(calendar.getTimeInMillis()) - 60 * 1000) / 1000;// 结束时间
        // 23:59
        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("id", id);
        hashMap.put("stime", start_time + "");
        hashMap.put("etime", end_time + "");
        hashMap.put("page", "1");
        return hashMap;
    }

    private HashMap<String, String> initparamasSettement() {
        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("id", id);
        hashMap.put("billid", "");

        return hashMap;
    }

    private void initView() {
        listview = (PinnedHeaderListView) findViewById(R.id.listview);
        not_data = (View) findViewById(R.id.not_data);

    }

    private void initTitle() {
        initTitle(titleName);
    }

    private void initAdapter() {
        dailycostEntities = new ArrayList<DailycostEntity>();
        adapter = new SettledDetailAdapter(this, dailycostEntities, "0", this, this);
        listview.setAdapter(adapter);

    }

    private void deleteDailycostInfo(final String billid, List<DailycostEntity> listData, int position) {
        listData.remove(position);
        adapter.notifyDataSetChanged();
        DownUrlUtil.deleteDailycostInfo(billid, handler, 890);
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

        }

    };


    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.relayout_item:
                // final int positio = Integer.parseInt(v.getTag().toString());
                //
                // DialogUtils.showConfirm(this, "提示", "您确定要删除这条账单?", "确定", new
                // OnClickListener() {
                //
                // @Override
                // public void onClick(View v) {
                // if (InternetUtils.checkNet(MyApplicaction.getContext())) {
                // deletBill(positio,
                // toBillListInfoItem(dailycostEntities.get(positio)));
                // } else {
                // deleteDailycostInfo(dailycostEntities.get(positio).getBillid(),
                // dailycostEntities, positio);
                // }
                // DialogUtils.dismissConfirmDialog();
                //
                // }
                // }, "取消", new OnClickListener() {
                //
                // @Override
                // public void onClick(View v) {
                // DialogUtils.dismissConfirmDialog();
                //
                // }
                // });

                break;

            default:
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case R.id.relayout_item:
                final int positio = Integer.parseInt(v.getTag().toString());
                BaserClassMode.billDetail(this, positio, dailycostEntities, id);

                break;

            default:
                break;
        }
    }

}
