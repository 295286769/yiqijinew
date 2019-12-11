package com.yiqiji.money.modules.property.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.plication.MyApplicaction;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.property.adapter.AddPropertyAdapter;
import com.yiqiji.money.modules.property.entity.AddPropertyEntity;
import com.yiqiji.money.modules.property.entity.AddPropertyItemEntity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加资产
 */
public class WealthAddActivity extends BaseActivity implements OnClickListener {

    private ImageView iv_back;
    private ImageView iv_share;
    private TextView tv_title;
    private RadioGroup radio_group_wealth;

    private AddPropertyEntity entity;
    private List<AddPropertyItemEntity> moneyItemEntities = new ArrayList<>();//资金账户
    private List<AddPropertyItemEntity> invesrItemEntities = new ArrayList<>();//投资理财
    private List<AddPropertyItemEntity> borrowItemEntities = new ArrayList<>();//应收应付

    private List<AddPropertyItemEntity> propertyEntities = new ArrayList<>();//数据源
    private ListView listView;
    private AddPropertyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplicaction.getInstence().addActivity(this);
        setContentView(R.layout.activity_wealth_add);
        initView();
        loadData();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.GONE);
        iv_share = (ImageView) findViewById(R.id.iv_share);
        iv_share.setImageResource(R.drawable.cancle_icon);
        iv_share.setVisibility(View.VISIBLE);
        iv_share.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("添加资产");
        radio_group_wealth = (RadioGroup) findViewById(R.id.radio_group_wealth);
        ((RadioButton) radio_group_wealth.getChildAt(0)).setChecked(true);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new AddPropertyAdapter(this, propertyEntities, R.layout.activity_addproperty_item);
        listView.setAdapter(adapter);

        radio_group_wealth.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_wealth_add_capital://资金账户
                        propertyEntities.clear();
                        propertyEntities.addAll(moneyItemEntities);
                        adapter.notifyDataSetChanged();
                        break;
                    case R.id.rb_wealth_add_investment://投资理财
                        propertyEntities.clear();
                        propertyEntities.addAll(invesrItemEntities);
                        adapter.notifyDataSetChanged();
                        break;
                    case R.id.rb_wealth_add_receivables://应收\付款
                        propertyEntities.clear();
                        propertyEntities.addAll(borrowItemEntities);
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        });

    }

    /**
     * 请求数据
     */
    private void loadData() {

        CommonFacade.getInstance().exec(Constants.ADD_CATE_ASSERT, new ViewCallBack() {
            @Override
            public void onStart() {
                showLoadingDialog(true);
            }

            @Override
            public void onFinish() {
                dismissDialog();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                JSONObject jo_main = (JSONObject) o;
                List<AddPropertyItemEntity> moneyItems = null;//资金账户
                List<AddPropertyItemEntity> invesrItems = null;//投资理财
                List<AddPropertyItemEntity> borrowItems = null;//应收应付

                entity = AddPropertyEntity.parceList(jo_main.optString("data"));
                if (entity != null) {
                    moneyItems = entity.getMoneyItemEntities();
                    invesrItems = entity.getInvesrItemEntities();
                    borrowItems = entity.getBorrowItemEntities();
                }
                moneyItemEntities.clear();
                invesrItemEntities.clear();
                borrowItemEntities.clear();
                propertyEntities.clear();

                moneyItemEntities.addAll(moneyItems);
                invesrItemEntities.addAll(invesrItems);
                borrowItemEntities.addAll(borrowItems);
                propertyEntities.addAll(moneyItemEntities);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                UIHelper.showShortToast(mContext, simleMsg.getErrMsg(), 500);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1002) {//新建账户成功返回
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_share://返回
                finish();
                overridePendingTransition(0,R.anim.activity_out_anim);
                break;
            default:
                break;
        }
    }

}
