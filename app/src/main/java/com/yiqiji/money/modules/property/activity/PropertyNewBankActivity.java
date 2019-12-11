package com.yiqiji.money.modules.property.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.control.DaySeleterDialog;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.property.entity.AddPropertyItemEntity;

import java.util.HashMap;

/**
 * Created by dansakai on 2017/3/9.
 * 新建 (编辑)信用卡
 */
public class PropertyNewBankActivity extends BaseActivity implements  View.OnClickListener{

    private ImageView iv_back;
    private TextView tv_title;

    private ImageView iv_icon;
    private TextView tv_name;

    private EditText et_remarks;
    private EditText et_balance;
    private TextView tv_billData;
    private TextView tv_payData;
    private EditText et_needPay;

    private Button btn_save;
    private LinearLayout ll_edit;
    private Button btn_delet;
    private Button btn_etSave;

    private AddPropertyItemEntity entity = null;

    private boolean isEdite = false;

    private String[] dataList = {"每月1日", "每月2日", "每月3日", "每月4日", "每月5日", "每月6日", "每月7日", "每月8日", "每月9日", "每月10日", "每月11日", "每月12日", "每月13日", "每月14日", "每月15日"
            , "每月16日", "每月17日", "每月18日", "每月19日", "每月20日", "每月21日", "每月22日",
            "每月23日", "每月24日", "每月25日", "每月26日", "每月27日", "每月28日"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promise_car);
        initView();
        changeData();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_name = (TextView) findViewById(R.id.tv_name);
        et_remarks = (EditText) findViewById(R.id.et_remarks);
        et_balance = (EditText) findViewById(R.id.et_balance);
        et_balance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        et_balance.setText(s);
                        et_balance.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    et_balance.setText(s);
                    et_balance.setSelection(2);
                }
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        et_balance.setText(s.subSequence(0, 1));
                        et_balance.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        tv_billData = (TextView) findViewById(R.id.tv_billData);
        tv_billData.setOnClickListener(this);
        tv_payData = (TextView) findViewById(R.id.tv_payData);
        tv_payData.setOnClickListener(this);
        et_needPay = (EditText) findViewById(R.id.et_needPay);
        et_needPay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        et_needPay.setText(s);
                        et_needPay.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    et_needPay.setText(s);
                    et_needPay.setSelection(2);
                }
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        et_needPay.setText(s.subSequence(0, 1));
                        et_needPay.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        ll_edit = (LinearLayout) findViewById(R.id.ll_edit);
        btn_delet = (Button) findViewById(R.id.btn_delet);
        btn_delet.setOnClickListener(this);
        btn_etSave = (Button) findViewById(R.id.btn_etSave);
        btn_etSave.setOnClickListener(this);

    }

    private void changeData() {
        isEdite = getIntent().getBooleanExtra("isEdite", false);

        entity = (AddPropertyItemEntity) getIntent().getSerializableExtra("bankEntity");
        if (entity != null) {
            if (isEdite) {
                et_remarks.setText(entity.getMarktext());
                if (!StringUtils.isEmpty(entity.getMarktext())) {
                    et_remarks.setSelection(entity.getMarktext().length());
                }
                if (!"0.00".equals(entity.getAssetamount())) {
                    et_balance.setText(entity.getCreditlimit());
                }
                et_needPay.setText(entity.getAssetamount());
                ll_edit.setVisibility(View.VISIBLE);
                btn_save.setVisibility(View.GONE);
                XzbUtils.displayImage(iv_icon, entity.getCategoryicon(), 0);
                tv_name.setText(entity.getCategoryname());

                tv_billData.setText("每月" + entity.getBillday() + "日");
                tv_payData.setText("每月" + entity.getRepayday() + "日");
                tv_title.setText("编辑账户");
            } else {
                XzbUtils.displayImage(iv_icon, entity.getBankicon(), 0);
                tv_name.setText(entity.getBankname());
                ll_edit.setVisibility(View.GONE);
                btn_save.setVisibility(View.VISIBLE);
                tv_title.setText("添加账户");
            }

        }

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                UIHelper.showSoftInputMethod(et_remarks);
            }
        }, 100);
    }

    /**
     * 删除
     */
    private void delect() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", entity.getAssetid());

        CommonFacade.getInstance().exec(Constants.ADD_DELE_ASSERT, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                showLoadingDialog();
            }

            @Override
            public void onFinish() {
                dismissDialog();
            }

            @Override
            public void onSuccess(Object o) throws Exception{
                Intent intent = new Intent();
                intent.putExtra("isDel", true);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                UIHelper.showShortToast(mContext, simleMsg.getErrMsg(), 500);
            }
        });
    }

    /**
     * 更新
     */
    private void upData() {
        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("assetid", entity.getAssetid());
        hashMap.put("marktext", et_remarks.getText().toString());
        String bill;
        if (!StringUtils.isEmpty(et_needPay.getText().toString())) {
            bill = et_needPay.getText().toString();
        } else {
            bill = "0";
        }
        hashMap.put("creditlimit", StringUtils.isEmpty(et_balance.getText().toString())?"0":et_balance.getText().toString());
        hashMap.put("billamount", bill);
        String strbill = tv_billData.getText().toString();
        String strpay = tv_payData.getText().toString();
        hashMap.put("billday", strbill.substring(2, strbill.length() - 1));
        hashMap.put("repayday", strpay.substring(2, strpay.length() - 1));
        CommonFacade.getInstance().exec(Constants.ADD_RENEW_CREDIT, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                showLoadingDialog();
            }

            @Override
            public void onFinish() {
                dismissDialog();
            }

            @Override
            public void onSuccess(Object o) throws Exception{
                Intent intent = new Intent();
                intent.putExtra("isDel", false);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                UIHelper.showShortToast(mContext, simleMsg.getErrMsg(), 500);
            }
        });
    }

    /**
     * 提交信用卡账户信息
     */
    private void submit() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("assettype", entity.getCategoryid());
        hashMap.put("itemid", entity.getBankid());
        hashMap.put("itemtype", entity.getItemtypeId());
        hashMap.put("itemname", entity.getBankname());
        hashMap.put("marktext", et_remarks.getText().toString());
        hashMap.put("creditlimit", StringUtils.isEmpty(et_balance.getText().toString())?"0":et_balance.getText().toString());
        String bill;
        if (!StringUtils.isEmpty(et_needPay.getText().toString())) {
            bill = et_needPay.getText().toString();
        } else {
            bill = "0";
        }
        hashMap.put("billamount", bill);
        String strbill = tv_billData.getText().toString();
        String strpay = tv_payData.getText().toString();
        hashMap.put("billday", strbill.substring(2, strbill.length() - 1));
        hashMap.put("repayday", strpay.substring(2, strpay.length() - 1));

        CommonFacade.getInstance().exec(Constants.ADD_CREDIT_ASSERT, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                showLoadingDialog();
            }

            @Override
            public void onFinish() {
                dismissDialog();
            }

            @Override
            public void onSuccess(Object o) throws Exception{
                setResult(RESULT_OK);
                showToast("资产添加成功");
                finish();
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                UIHelper.showShortToast(mContext, simleMsg.getErrMsg(), 500);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_save:
//                if (StringUtils.isEmpty(et_balance.getText().toString())) {
//                    UIHelper.showShortToast(mContext,"请填写信用额度",500);
//                    UIHelper.showSoftInputMethod(et_balance);
//                    return;
//                }
                submit();
                break;
            case R.id.btn_etSave://更新
//                if (StringUtils.isEmpty(et_balance.getText().toString())) {
//                    UIHelper.showShortToast(mContext,"请填写信用额度",500);
//                    UIHelper.showSoftInputMethod(et_balance);
//                    return;
//                } else if (tv_billData.getText().toString().equals(tv_payData.getText().toString())) {
//                    UIHelper.showShortToast(mContext, "账单日还款日不能为同一天", 500);
//                    return;
//                }
                if (tv_billData.getText().toString().equals(tv_payData.getText().toString())) {
                    UIHelper.showShortToast(mContext, "账单日还款日不能为同一天", 500);
                    return;
                }

                upData();
                break;
            case R.id.btn_delet://删除
                showSimpleAlertDialog("", "确定删除该资产么？", "确定", "取消", false, true,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismissDialog();
                                delect();
                            }
                        },
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismissDialog();
                            }
                        });
                break;
            case R.id.tv_billData://选择账单日期
                new DaySeleterDialog(mContext, new DaySeleterDialog.DataListener() {
                    @Override
                    public void dataPicker(String day) {
                        tv_billData.setText(day);
                    }
                }, dataList).show();
                break;
            case R.id.tv_payData://请选择还款日期
                new DaySeleterDialog(mContext, new DaySeleterDialog.DataListener() {
                    @Override
                    public void dataPicker(String day) {
                        tv_payData.setText(day);
                    }
                }, dataList).show();
                break;
            default:
                break;
        }
    }
}
