package com.yiqiji.money.modules.property.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.control.RelativeLayoutWithCorner;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.property.entity.NetPropertEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by dansakai on 2017/3/22.
 * 网络理财（银行理财）详细页
 */

public class PropertyNetDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_back;//
    private TextView tv_title;

    private ImageView iv_complet;//完结图标
    private RelativeLayoutWithCorner rl_card;
    private TextView tv_name;
    private TextView tv_mark;
    private TextView tv_edit;
    private TextView tv_desc;
    private TextView tv_money;

    private TextView tv_head;
    private LinearLayout ll_pdData;
    private TextView tv_dayBenif;//日收益
    private TextView tv_totalBenif;//日收益
    private LinearLayout ll_yearBenif;
    private TextView tv_yearBenif;

    private TextView tv_way;
    private TextView tv_decWay;
    private TextView tv_endData;
    private TextView tv_startData;

    private View v_divider;

    //    private ImageView iv_noData;//无还款计划展示
//    private LinearLayout ll_noData;
    private ImageView iv_noBillData;

    private RelativeLayout rl_banerHead;//
    private LinearLayout ll_items;

    private Button btn_complet;

    private int itemtype;//类型id
    private String assetid;//id
    private boolean isComple = false;//是否完结

    private NetPropertEntity entity = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_propert_detail);
        initView();
        loadData();
    }

    /**
     * 完结资产
     */
    private void complete() {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", assetid);
        CommonFacade.getInstance().exec(Constants.ADD_FINISH_ASSERT, map, new ViewCallBack() {
            @Override
            public void onStart() {
                showLoadingDialog();
            }

            @Override
            public void onFinish() {
                dismissDialog();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                tv_edit.setVisibility(View.GONE);
                iv_complet.setVisibility(View.VISIBLE);
                btn_complet.setVisibility(View.INVISIBLE);
                rl_card.setBackgroundDrawable(Color.parseColor("#999999"));
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                UIHelper.showShortToast(mContext, simleMsg.getErrMsg(), 500);
            }
        });
    }

    /**
     * 获取资产详细页数据
     */
    private void loadData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("assetid", assetid);
        CommonFacade.getInstance().exec(Constants.ASSERT_DETAIL, map, new ViewCallBack() {
            @Override
            public void onStart() {
                showLoadingDialog(true);
            }

            @Override
            public void onFinish() {
                dismissDialog();
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                UIHelper.showShortToast(mContext, simleMsg.getErrMsg(), 500);
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                JSONObject jo_main = (JSONObject) o;
                if (!StringUtils.isEmpty(jo_main.optString("data"))) {
                    entity = NetPropertEntity.parceEntity(jo_main.optString("data"));
                    if (entity != null) {
                        //获取格式化对象
                        NumberFormat nt = NumberFormat.getPercentInstance();
                        //设置百分数精确度2即保留两位小数
                        nt.setMinimumFractionDigits(2);

                        tv_name.setText(entity.getItemname());
                        tv_mark.setText(entity.getMarktext());
                        tv_money.setText(StringUtils.moneySplitComma(entity.getAssetamount()));
                        tv_dayBenif.setText(StringUtils.moneySplitComma(entity.getTodayprofit()));
                        tv_totalBenif.setText(StringUtils.moneySplitComma(entity.getTotalprofit()));
                        if (itemtype == 16) {//银行
                            ll_yearBenif.setVisibility(View.GONE);
                            v_divider.setVisibility(View.GONE);
                            tv_way.setText("预计年化收益率");
                            tv_decWay.setText(nt.format(Double.parseDouble(entity.getYieldrate())));
                        } else {
                            v_divider.setVisibility(View.VISIBLE);
                            ll_yearBenif.setVisibility(View.VISIBLE);
                            tv_yearBenif.setText(nt.format(Double.parseDouble(entity.getYieldrate())));
                            tv_way.setText("汇款方式");
                            tv_decWay.setText(parceString(Integer.parseInt(entity.getDividendmethod())));
                        }

                        tv_startData.setText(DateUtil.formatDate(Long.parseLong(entity.getInterestdate())));

                        String[] interestdate = DateUtil.formatDate(Long.parseLong(entity.getInterestdate())).split("-");
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Integer.parseInt(interestdate[0]), Integer.parseInt(interestdate[1])-1, Integer.parseInt(interestdate[2]));
                        calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(entity.getDeadline()));
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        tv_endData.setText(sdf.format(calendar.getTime()));


                        if (isComple) {//已完结
                            if (!StringUtils.isEmpty(entity.getAsssetlog())) {
                                JSONArray arr_log = new JSONArray(entity.getAsssetlog());
                                if (arr_log != null && arr_log.length() > 0) {
                                    iv_noBillData.setVisibility(View.GONE);
                                    rl_banerHead.setVisibility(View.VISIBLE);
                                    ll_items.setVisibility(View.VISIBLE);

                                    ll_items.removeAllViews();
                                    JSONObject obj = null;
                                    for (int i = 0; i < arr_log.length(); i++) {
                                        obj = arr_log.optJSONObject(i);
                                        View item = LayoutInflater.from(PropertyNetDetailActivity.this).inflate(R.layout.include_finacil_complet_item, null);
                                        ((TextView) item.findViewById(R.id.tv_time)).setText(DateUtil.formatDate(Long.parseLong(obj.optString("logdate"))));
                                        ((TextView) item.findViewById(R.id.tv_text)).setText(obj.optString("logtext"));
                                        ll_items.addView(item);
                                    }
                                } else {
                                    rl_banerHead.setVisibility(View.GONE);
                                    ll_items.setVisibility(View.GONE);
                                    iv_noBillData.setVisibility(View.VISIBLE);
                                }
                            } else {
                                rl_banerHead.setVisibility(View.GONE);
                                ll_items.setVisibility(View.GONE);
                                iv_noBillData.setVisibility(View.VISIBLE);
                            }
                            tv_edit.setVisibility(View.GONE);
                            iv_complet.setVisibility(View.VISIBLE);
                            btn_complet.setVisibility(View.INVISIBLE);
                            rl_card.setBackgroundDrawable(Color.parseColor("#999999"));
                        } else {
                            tv_edit.setVisibility(View.VISIBLE);
                            iv_complet.setVisibility(View.GONE);
                            btn_complet.setVisibility(View.VISIBLE);
                            rl_card.setBackgroundDrawable(Color.parseColor("#498be7"));

                            rl_banerHead.setVisibility(View.GONE);
                            ll_items.setVisibility(View.GONE);
                            iv_noBillData.setVisibility(View.VISIBLE);
                        }
                    }
                }
                rl_card.setVisibility(View.VISIBLE);
                tv_head.setVisibility(View.VISIBLE);
                ll_pdData.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initView() {
        itemtype = Integer.parseInt(getIntent().getStringExtra("itemtype"));
        assetid = getIntent().getStringExtra("assetid");
        isComple = getIntent().getBooleanExtra("isComple", false);

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("资产详情");

        iv_complet = (ImageView) findViewById(R.id.iv_complet);
        rl_card = (RelativeLayoutWithCorner) findViewById(R.id.rl_card);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_mark = (TextView) findViewById(R.id.tv_mark);
        tv_edit = (TextView) findViewById(R.id.tv_edit);
        tv_edit.setOnClickListener(this);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        tv_money = (TextView) findViewById(R.id.tv_money);

        tv_head = (TextView) findViewById(R.id.tv_head);
        ll_pdData = (LinearLayout) findViewById(R.id.ll_pdData);
        tv_dayBenif = (TextView) findViewById(R.id.tv_dayBenif);
        tv_totalBenif = (TextView) findViewById(R.id.tv_totalBenif);
        ll_yearBenif = (LinearLayout) findViewById(R.id.ll_yearBenif);
        tv_yearBenif = (TextView) findViewById(R.id.tv_yearBenif);

        tv_way = (TextView) findViewById(R.id.tv_way);
        tv_decWay = (TextView) findViewById(R.id.tv_decWay);
        tv_endData = (TextView) findViewById(R.id.tv_endData);
        tv_startData = (TextView) findViewById(R.id.tv_startData);

        v_divider = findViewById(R.id.v_divider);

        iv_noBillData = (ImageView) findViewById(R.id.iv_noBillData);

        rl_banerHead = (RelativeLayout) findViewById(R.id.rl_banerHead);
        ll_items = (LinearLayout) findViewById(R.id.ll_items);

        btn_complet = (Button) findViewById(R.id.btn_complet);
        btn_complet.setOnClickListener(this);

        rl_card.setVisibility(View.GONE);
        tv_head.setVisibility(View.GONE);
        ll_pdData.setVisibility(View.GONE);
        btn_complet.setVisibility(View.INVISIBLE);
        iv_noBillData.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_complet:
                complete();
                break;
            case R.id.tv_edit://跳转到编辑页
                Intent intent = new Intent(PropertyNetDetailActivity.this, PropertyNewFinancialActivity.class);
                intent.putExtra("netEntity", entity);
                intent.putExtra("isEdit", true);
                startActivityForResult(intent, 1002);
                break;
            default:
        }
    }

    private String parceString(int i) {
        String backString = "";
        switch (i) {
            case 1:
                backString = "到期还本付息";
                break;
            case 2:
                backString = "月还等额本金";
                break;
            case 3:
                backString = "月还等额本息";
                break;
            case 4:
                backString = "每月还息，到期还本息";
                break;
        }
        return backString;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        boolean isDel = data.getBooleanExtra("isDel", false);
        if (isDel) {//删除返回
            setResult(RESULT_OK);
            finish();
        } else {//更新返回
            loadData();
        }

    }
}
