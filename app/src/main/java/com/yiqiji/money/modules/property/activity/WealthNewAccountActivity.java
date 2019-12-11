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
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.property.entity.AddPropertyItemEntity;

import java.util.HashMap;

/**
 * 新建(编辑)账单--现金钱包、支付宝、微信钱包、公交卡、饭卡、其他账户--银行借记卡
 */
public class WealthNewAccountActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title;

    private ImageView iv_icon;
    private TextView tv_name;
    private TextView tv_borrowCard;
    private TextView tvMark;//备注信息
    private EditText et_remarks;
    private EditText et_balance;
    private Button btn_save;//保存
    private LinearLayout ll_edit;//编辑
    private Button btn_delet, btn_etSave;

    private AddPropertyItemEntity entity = null;

    private int itemType;//类型

    private boolean isEdite = false;//是否编辑


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wealth_new_account);
        initView();
        changeState();
    }

    private void changeState() {
        isEdite = getIntent().getBooleanExtra("isEdite", false);
        entity = (AddPropertyItemEntity) getIntent().getSerializableExtra("addproEntity");
        if (entity != null) {
            XzbUtils.displayImage(iv_icon, entity.getCategoryicon(), 0);
            tv_name.setText(entity.getCategoryname());
            itemType = Integer.parseInt(entity.getItemtypeId());
            if (isEdite) {
                et_remarks.setText(entity.getMarktext());
                if (!StringUtils.isEmpty(entity.getMarktext())) {
                    et_remarks.setSelection(entity.getMarktext().length());
                }
                if (!"0.00".equals(entity.getAssetamount())) {
                    et_balance.setText(entity.getAssetamount());
                }
                btn_save.setVisibility(View.GONE);
                ll_edit.setVisibility(View.VISIBLE);
                tv_title.setText("编辑账户");
            } else {
                btn_save.setVisibility(View.VISIBLE);
                ll_edit.setVisibility(View.GONE);
                tv_title.setText("添加账户");
            }
        }
        if (itemType == 9) {
            tv_borrowCard.setVisibility(View.VISIBLE);
        } else {
            tv_borrowCard.setVisibility(View.GONE);
        }

        if (itemType == 7 || itemType == 8) {//支付宝、微信
            tvMark.setText("用户名");
            et_remarks.setHint("请输入用户名");
        } else {
            tvMark.setText("备注信息");
            et_remarks.setHint("请输入备注名");
        }
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);

        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_borrowCard = (TextView) findViewById(R.id.tv_borrowCard);
        tvMark = (TextView) findViewById(R.id.tvMark);
        et_remarks = (EditText) findViewById(R.id.et_remarks);
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                UIHelper.showSoftInputMethod(et_remarks);
            }
        }, 100);

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
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        ll_edit = (LinearLayout) findViewById(R.id.ll_edit);
        btn_delet = (Button) findViewById(R.id.btn_delet);
        btn_delet.setOnClickListener(this);
        btn_etSave = (Button) findViewById(R.id.btn_etSave);
        btn_etSave.setOnClickListener(this);

    }

    /**
     * 更新资产
     */
    private void upData() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("assetid", entity.getAssetid());
        hashMap.put("marktext", et_remarks.getText().toString());
        String balanceMoney;
        if (!StringUtils.isEmpty(et_balance.getText().toString())) {
            balanceMoney = et_balance.getText().toString();
        } else {
            balanceMoney = "0.00";
        }
        hashMap.put("balance", balanceMoney);
        CommonFacade.getInstance().exec(Constants.ADD_RENEW_ASSERT, hashMap, new ViewCallBack() {
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
     * 删除账户
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
     * 新建账户
     */
    private void submit() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("assettype", entity.getCategoryid());
        hashMap.put("itemtype", entity.getItemtypeId());
        hashMap.put("itemname", entity.getCategoryname());
        hashMap.put("marktext", et_remarks.getText().toString());
        String balanceMoney;
        if (!StringUtils.isEmpty(et_balance.getText().toString())) {
            balanceMoney = et_balance.getText().toString();
        } else {
            balanceMoney = "0.00";
        }
        hashMap.put("balance", balanceMoney);

        if (itemType == 9) {//银行
            hashMap.put("itemid", entity.getBankid());
        }

        CommonFacade.getInstance().exec(Constants.ADD_ASSERT, hashMap, new ViewCallBack() {
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
            case R.id.btn_etSave://更新
                upData();
                break;
            case R.id.btn_save://保存
                submit();
                break;
            default:
                break;
        }
    }
}
