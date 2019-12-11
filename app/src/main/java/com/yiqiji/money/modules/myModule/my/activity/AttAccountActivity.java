package com.yiqiji.money.modules.myModule.my.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.myModule.my.adapter.AccountAdapter;
import com.yiqiji.money.modules.myModule.my.entity.AccountBookEntity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dansakai on 2017/5/19.
 * 我关注的账本
 */

public class AttAccountActivity extends BaseActivity implements View.OnClickListener{
    private ImageView iv_back;
    private TextView tv_title;
    private TextView tv_attention;
    private TextView tv_attNm;

    private LinearLayout ll_noData;//暂无数据

    private GridView mGridView;
    private AccountAdapter adapter;
    private List<AccountBookEntity> mList = new ArrayList<>();

    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_account);
        initView();
        loadData();
    }

    /**
     * 获取我关注的账本
     */
    private void loadData() {
        CommonFacade.getInstance().exec(Constants.ACCOUNTER_SUBSCRIBE, new ViewCallBack() {
            @Override
            public void onStart() {
                showLoadingDialog();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                JSONObject jo_main = (JSONObject) o;
                List<AccountBookEntity> tempList = null;
                if (!StringUtils.isEmpty(jo_main.optString("data"))) {
                    tempList = AccountBookEntity.parceLis(jo_main.optString("data"));
                }
                mList.clear();
                if (!StringUtils.isEmptyList(tempList)) {
                    ll_noData.setVisibility(View.GONE);
                    mList.addAll(tempList);
                    tv_attNm.setText("已订阅" + mList.size() + "个账本");
                } else {
                    tv_attNm.setText("已订阅"+0+"个账本");
                    ll_noData.setVisibility(View.VISIBLE);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                showToast(simleMsg.getErrMsg());
            }

            @Override
            public void onFinish() {
                dismissDialog();
            }
        });
    }

    /**
     * 取消关注账本
     * @param id 账本id
     */
    public void cancleSuscrib(final String id) {
        showSimpleAlertDialog("", "确定取消订阅该账本", "确定", "取消", false, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                HashMap<String, String> map = new HashMap<>();
                map.put("id", id);
                CommonFacade.getInstance().exec(Constants.CANCLE_ACCOUNTER_SUBSCRIBE, map, new ViewCallBack() {
                    @Override
                    public void onStart() {
                        showLoadingDialog();
                    }

                    @Override
                    public void onSuccess(Object o) throws Exception {
                        Iterator<AccountBookEntity> iterator = mList.iterator();
                        while (iterator.hasNext()) {
                            AccountBookEntity entity = iterator.next();
                            if (entity.getSubscribeid().equals(id)) {
                                iterator.remove();
                            }
                        }
                        if (!StringUtils.isEmptyList(mList)) {
                            ll_noData.setVisibility(View.GONE);
                            tv_attNm.setText("已订阅" + mList.size() + "个账本");
                        } else {
                            tv_attNm.setText("已订阅"+0+"个账本");
                            ll_noData.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailed(SimpleMsg simleMsg) {
                        showToast(simleMsg.getErrMsg());
                    }

                    @Override
                    public void onFinish() {
                        dismissDialog();
                    }
                });
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });


    }

    private void initView() {
        mGridView = (GridView) findViewById(R.id.gridView);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("我订阅的账本");
        tv_attention = (TextView) findViewById(R.id.tv_attention);
        tv_attention.setText("编辑");
        tv_attention.setVisibility(View.VISIBLE);
        tv_attNm = (TextView) findViewById(R.id.tv_attNm);

        ll_noData = (LinearLayout) findViewById(R.id.ll_noData);

        iv_back.setOnClickListener(this);
        tv_attention.setOnClickListener(this);

        adapter = new AccountAdapter(mContext, mList);
        mGridView.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_attention://编辑
                if (isEdit) {
                    tv_attention.setText("编辑");
                } else {
                    tv_attention.setText("完成");
                }
                isEdit = !isEdit;
                for (int i = 0;i < mList.size();i++) {
                    mList.get(i).setEdit(isEdit);
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }
}
