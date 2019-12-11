package com.yiqiji.money.modules.property.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.control.RelativeLayoutWithCorner;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.frame.core.utils.LogUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.property.adapter.BillListAdapter;
import com.yiqiji.money.modules.property.entity.AddPropertyItemEntity;
import com.yiqiji.money.modules.property.entity.BillEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dansakai on 2017/3/9.
 * 资产详情
 */

public class PropertyDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_back;
    private TextView tv_title;
    private TextView tv_attention;

    private RelativeLayoutWithCorner rl_card;
    private TextView tv_name;
    private TextView tv_mark;
    private TextView tv_edit;
    private ImageView iv_complet;
    private TextView tv_desc;
    private TextView tv_money;

    private LinearLayout ll_chekData;
    private TextView tv_chekData;
    private TextView tv_backData;
    private TextView tv_limitMoney;

    private LinearLayout ll_complet;

    //    private LinearLayout ll_noData;
    private ImageView iv_noBillData;
    private Button btn_complet;

    private String assetid = "";
    private int itemtype;
    private boolean isComple;//是否已完结

    private JSONObject jo_body = null;

    private List<JSONObject> compLis = new ArrayList<>();//已完结项目列表

    private ListView listView;
    private List<BillEntity> billists = new ArrayList<>();
    private BillListAdapter billAdapter;
    private List<Integer> sourceId;

    private int assettype;//资产类型

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_detail);
        initView();
        changeState();
        loadData();

    }

    /**
     * 根据不同的资产小类改变布局
     */
    private void changeState() {
        if (itemtype == 6) {//信用卡
            ll_chekData.setVisibility(View.VISIBLE);
            tv_desc.setText("当前欠款(元)");
        } else {
            ll_chekData.setVisibility(View.GONE);
            if (itemtype == 19 || itemtype == 20 || itemtype == 21) {
                if (itemtype == 19) {//借出
                    tv_desc.setText("未收回的本金(元)");
                } else if (itemtype == 20) {//借入
                    tv_desc.setText("未归还的本金(元)");
                } else {//自定义
                    tv_desc.setText("应付金额(元)");
                }
            } else {
                tv_desc.setText("账户余额(元)");
            }
        }
    }

    /**
     * 完结资产
     */
    private void complete() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", assetid);

        CommonFacade.getInstance().exec(Constants.ADD_FINISH_ASSERT, hashMap, new ViewCallBack() {
            @Override
            public void onFailed(SimpleMsg simleMsg) {
                UIHelper.showShortToast(mContext, simleMsg.getErrMsg(), 500);
            }

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
        });
    }

    /**
     * 加载资产详细页数据
     */
    private void loadData() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("assetid", assetid);
        CommonFacade.getInstance().exec(Constants.ASSERT_DETAIL, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                showLoadingDialog(true);
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                JSONObject jo_main = (JSONObject) o;
                if (!StringUtils.isEmpty(jo_main.optString("data"))) {
                    jo_body = new JSONObject(jo_main.optString("data"));
                    assettype = Integer.parseInt(jo_body.optString("assettype"));
                    if (assettype == 1) {
                        tv_attention.setVisibility(View.VISIBLE);
                    } else {
                        tv_attention.setVisibility(View.GONE);
                    }
                    tv_name.setText(jo_body.optString("itemname"));
                    tv_money.setText(StringUtils.moneySplitComma(jo_body.optString("assetamount")));
                    if (itemtype == 9 || itemtype == 6) {//借记卡
                        JSONObject jo_bank = new JSONObject(jo_body.optString("bank"));
                        tv_name.setText(jo_body.optString("itemcatename"));
                        tv_mark.setText(jo_bank.optString("bankname") + " " + jo_body.optString("marktext"));
//                        rl_card.setBackgroundDrawable(Color.parseColor(jo_bank.optString("color")));

                        if (itemtype == 6) {//信用卡
                            JSONObject jo_attach = new JSONObject(jo_body.optString("attach"));
                            tv_chekData.setText("每月" + jo_attach.optString("billday") + "号");
                            tv_backData.setText("每月" + jo_attach.optString("repayday") + "号");
                            tv_limitMoney.setText(StringUtils.moneySplitComma(jo_attach.optString("creditlimit")));
                        }
                    } else if (itemtype == 19 || itemtype == 20 || itemtype == 21) {
                        if (isComple) {
                            JSONArray comArr = new JSONArray(jo_body.optString("asssetlog"));
                            if (comArr != null && comArr.length() > 0) {
                                for (int i = 0; i < comArr.length(); i++) {
                                    compLis.add(comArr.getJSONObject(i));
                                }
                            }
                            btn_complet.setVisibility(View.INVISIBLE);
                        } else {
                            btn_complet.setVisibility(View.VISIBLE);
                        }

                        if (!StringUtils.isEmpty(jo_body.optString("attach"))) {
                            JSONObject jo_attach = new JSONObject(jo_body.optString("attach"));
                            tv_mark.setText(jo_attach.optString("remark") + " " + DateUtil.formatDate(Long.parseLong(jo_attach.optString("loandate"))));
                            if (itemtype == 19) {//借出
                                tv_desc.setText("未收回的本金(元)");
                            } else if (itemtype == 20) {//借入
                                tv_desc.setText("未归还的本金(元)");
                            } else {//自定义
                                int type = Integer.parseInt(jo_attach.optString("loantype"));
                                if (type == 1) {
                                    tv_desc.setText("应付金额(元)");
                                } else if (type == 0) {
                                    tv_desc.setText("应收金额(元)");
                                } else {
                                    tv_desc.setText("应收金额(元)");
                                }
                            }
                        } else {
                            tv_mark.setText("");
                        }

                    } else {
                        tv_mark.setText(jo_body.optString("marktext"));
                    }

                    if (!StringUtils.isEmptyList(compLis)) {
                        ll_complet.setVisibility(View.VISIBLE);
                        iv_noBillData.setVisibility(View.GONE);
                        ll_complet.removeAllViews();
                        for (int i = 0; i < compLis.size(); i++) {
                            JSONObject object = compLis.get(i);
                            View itemView = LayoutInflater.from(PropertyDetailActivity.this).inflate(R.layout.include_property_detail_item, null);
                            XzbUtils.displayImage(((ImageView) itemView.findViewById(R.id.iv_img)), jo_body.optString("itemcateicon"), 0);
                            ((TextView) itemView.findViewById(R.id.tv_title)).setText(object.optString("logtitle"));
                            ((TextView) itemView.findViewById(R.id.tv_content)).setText(object.optString("logtext"));
                            ((TextView) itemView.findViewById(R.id.tv_time)).setText(DateUtil.formatDate(Long.parseLong(object.optString("logdate"))));
                            ll_complet.addView(itemView);
                        }
                    } else {
                        iv_noBillData.setVisibility(View.VISIBLE);
                        ll_complet.setVisibility(View.GONE);
                    }
                    rl_card.setVisibility(View.VISIBLE);
                    List<BillEntity> temBillis = BillEntity.parceLis(jo_body.optString("billlist"));
                    billists.clear();
                    if (!StringUtils.isEmptyList(temBillis)) {
                        listView.setVisibility(View.VISIBLE);
                        billists.addAll(temBillis);
                        billAdapter.notifyDataSetChanged();
                        iv_noBillData.setVisibility(View.GONE);
                    } else {
                        listView.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                UIHelper.showShortToast(mContext, simleMsg.getErrMsg(), 500);
            }

            @Override
            public void onFinish() {
                dismissDialog();
            }
        });

    }

    private void initView() {
        assetid = getIntent().getStringExtra("assetid");
        itemtype = Integer.parseInt(getIntent().getStringExtra("itemtype"));
        isComple = getIntent().getBooleanExtra("isComple", false);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("资产详情");
        tv_attention = (TextView) findViewById(R.id.tv_attention);
        tv_attention.setText("转账");
        tv_attention.setOnClickListener(this);
        rl_card = (RelativeLayoutWithCorner) findViewById(R.id.rl_card);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_mark = (TextView) findViewById(R.id.tv_mark);
        tv_edit = (TextView) findViewById(R.id.tv_edit);
        tv_edit.setOnClickListener(this);
        iv_complet = (ImageView) findViewById(R.id.iv_complet);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        tv_money = (TextView) findViewById(R.id.tv_money);

        ll_chekData = (LinearLayout) findViewById(R.id.ll_chekData);
        tv_chekData = (TextView) findViewById(R.id.tv_chekData);
        tv_backData = (TextView) findViewById(R.id.tv_backData);
        tv_limitMoney = (TextView) findViewById(R.id.tv_limitMoney);

        ll_complet = (LinearLayout) findViewById(R.id.ll_complet);

        iv_noBillData = (ImageView) findViewById(R.id.iv_noBillData);
        btn_complet = (Button) findViewById(R.id.btn_complet);
        btn_complet.setOnClickListener(this);

        if (isComple) {
            tv_edit.setVisibility(View.GONE);
            iv_complet.setVisibility(View.VISIBLE);
            btn_complet.setVisibility(View.INVISIBLE);

            rl_card.setBackgroundDrawable(Color.parseColor("#999999"));
        } else {
            tv_edit.setVisibility(View.VISIBLE);
            iv_complet.setVisibility(View.GONE);
            btn_complet.setVisibility(View.INVISIBLE);
            rl_card.setBackgroundDrawable(Color.parseColor("#498be7"));
        }

        listView = (ListView) findViewById(R.id.listView);
        sourceId = new ArrayList<>();
        sourceId.add(R.layout.activity_item_bill_first);
        sourceId.add(R.layout.activity_item_bill_second);
        billAdapter = new BillListAdapter(this, billists, sourceId);
        listView.setAdapter(billAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_edit://跳转到编辑页
                if (jo_body != null) {
                    if (assettype == 1) {//资金账户
                        if (!"6".equals(jo_body.optString("itemtype"))) {//非信用卡
                            Intent intent = new Intent(PropertyDetailActivity.this, WealthNewAccountActivity.class);
                            AddPropertyItemEntity entity = new AddPropertyItemEntity();
                            entity.setCategoryid(jo_body.optString("assettype"));
                            if ("9".equals(jo_body.optString("itemtype"))) {
                                try {
                                    JSONObject jo_bank = new JSONObject(jo_body.optString("bank"));
                                    entity.setCategoryname(jo_bank.optString("bankname"));
                                    entity.setCategoryicon(jo_bank.optString("bankicon"));
                                } catch (JSONException e) {
                                    LogUtil.log_error(null, e);
                                }
                            } else {
                                entity.setCategoryname(jo_body.optString("itemname"));
                                entity.setCategoryicon(jo_body.optString("itemcateicon"));
                            }

                            entity.setMarktext(jo_body.optString("marktext"));
                            entity.setItemtypeId(jo_body.optString("itemtype"));
                            entity.setAssetamount(jo_body.optString("assetamount"));
                            entity.setAssetid(jo_body.optString("assetid"));
                            intent.putExtra("addproEntity", entity);
                            intent.putExtra("isEdite", true);
                            startActivityForResult(intent, 1002);
                        } else {
                            Intent intent = new Intent(PropertyDetailActivity.this, PropertyNewBankActivity.class);
                            AddPropertyItemEntity entity = new AddPropertyItemEntity();
                            entity.setCategoryid(jo_body.optString("assettype"));
                            try {
                                JSONObject jo_bank = new JSONObject(jo_body.optString("bank"));
                                entity.setCategoryname(jo_bank.optString("bankname"));
                                entity.setCategoryicon(jo_bank.optString("bankicon"));
                            } catch (JSONException e) {
                                LogUtil.log_error(null, e);
                            }
                            entity.setMarktext(jo_body.optString("marktext"));
                            entity.setItemtypeId(jo_body.optString("itemtype"));
                            entity.setAssetamount(jo_body.optString("assetamount"));
                            entity.setAssetid(jo_body.optString("assetid"));
                            try {
                                JSONObject jo_attach = new JSONObject(jo_body.optString("attach"));
                                entity.setCreditlimit(jo_attach.optString("creditlimit"));
                                entity.setBillday(jo_attach.optString("billday"));
                                entity.setRepayday(jo_attach.optString("repayday"));
                            } catch (JSONException e) {
                                LogUtil.log_error(null, e);
                            }
                            intent.putExtra("bankEntity", entity);
                            intent.putExtra("isEdite", true);
                            startActivityForResult(intent, 1002);
                        }
                    } else if (assettype == 3) {//借入、借出
                        Intent intent = new Intent(PropertyDetailActivity.this, EditBorrowActivity.class);
                        intent.putExtra("jo_body", jo_body.toString());
                        startActivityForResult(intent, 1002);
                    }
                }

                break;
            case R.id.btn_complet://完结
                complete();
                break;
            case R.id.tv_attention://转账
                Intent intent = new Intent(mContext, WealthTurnActivity.class);
                intent.putExtra("assertId", jo_body.optString("assetid"));
                intent.putExtra("itemName", jo_body.optString("itemname"));
                intent.putExtra("itemCateIcon", jo_body.optString("itemcateicon"));
                startActivityForResult(intent, 1002);
                break;
            default:

        }
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
