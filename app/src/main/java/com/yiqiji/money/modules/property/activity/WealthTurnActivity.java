package com.yiqiji.money.modules.property.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.control.CounterSelecterDialog;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.property.entity.PropertyTransEntity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 资产转账页面
 */
public class WealthTurnActivity extends BaseActivity implements OnClickListener {

    private ImageView iv_back;
    //    private TextView tv_attention;
    private Button btn_save;

    private RelativeLayout rl_input;
    private EditText et_Money;
    private LinearLayout ll_transFrom;
    private TextView tv_transFrom;
    private ImageView iv_fromIcon;
    private LinearLayout ll_transTo;
    private TextView tv_transTo;
    private ImageView iv_toIcon;

    private List<PropertyTransEntity> cunterList = new ArrayList<>();//账户列表

    private CounterSelecterDialog selecterDialog = null;

    private boolean isFrom = true;//转出账户对话框
    private String fromassetid = "";//转出账户id
    private String toassetid = "";//转入账户id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_property_transfer);
        initView();
        loadData();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        rl_input = (RelativeLayout) findViewById(R.id.rl_input);
        rl_input.setOnClickListener(this);
        et_Money = (EditText) findViewById(R.id.et_Money);
        ll_transFrom = (LinearLayout) findViewById(R.id.ll_transFrom);
        tv_transFrom = (TextView) findViewById(R.id.tv_transFrom);
        iv_fromIcon = (ImageView) findViewById(R.id.iv_fromIcon);
        ll_transFrom.setOnClickListener(this);
        fromassetid = getIntent().getStringExtra("assertId");
        tv_transFrom.setText(getIntent().getStringExtra("itemName"));
        XzbUtils.displayImage(iv_fromIcon, getIntent().getStringExtra("itemCateIcon"), R.drawable.write_select_account);

        ll_transTo = (LinearLayout) findViewById(R.id.ll_transTo);
        tv_transTo = (TextView) findViewById(R.id.tv_transTo);
        iv_toIcon = (ImageView) findViewById(R.id.iv_toIcon);
        ll_transTo.setOnClickListener(this);

        et_Money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        et_Money.setText(s);
                        et_Money.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    et_Money.setText(s);
                    et_Money.setSelection(2);
                }
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        et_Money.setText(s.subSequence(0, 1));
                        et_Money.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    /**
     * 获取转账资金账户接口
     */
    private void loadData() {

        CommonFacade.getInstance().exec(Constants.TRANSLATE_ASSERT_ACOUNT, new ViewCallBack() {
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
                List<PropertyTransEntity> tempLis = null;
                tempLis = PropertyTransEntity.parceList(jo_main.optString("data"));
                if (!StringUtils.isEmptyList(tempLis)) {
                    if (!StringUtils.isEmptyList(cunterList)) {
                        List<String> temp = new ArrayList<String>();//请求下来的
                        List<String> exit = new ArrayList<String>();//已存在

                        for (PropertyTransEntity entity : tempLis) {
                            temp.add(entity.getAssetid());
                        }
                        for (PropertyTransEntity entity : cunterList) {
                            exit.add(entity.getAssetid());
                        }

                        for (String assertId : temp) {
                            if (!exit.contains(assertId)) {
                                PropertyTransEntity transEntity = null;
                                for (PropertyTransEntity entity : tempLis) {
                                    if (entity.getAssetid().equals(assertId)) {
                                        transEntity = entity;
                                    }
                                }
                                if (isFrom) {
                                    fromassetid = transEntity.getAssetid();
                                    tv_transFrom.setText(transEntity.getItemname());
                                    XzbUtils.displayImage(iv_fromIcon, transEntity.getItemicon(), R.drawable.write_select_account);
                                } else {
                                    toassetid = transEntity.getAssetid();
                                    tv_transTo.setText(transEntity.getItemname());
                                    XzbUtils.displayImage(iv_toIcon, transEntity.getItemicon(), R.drawable.write_select_account);
                                }
                            }
                        }
                    }
                    cunterList.clear();
                    cunterList.addAll(tempLis);
                    selecterDialog = new CounterSelecterDialog(WealthTurnActivity.this, "请选择账户", cunterList);
                    selecterDialog.setDissmissCallBack(new CounterSelecterDialog.DismissListener() {
                        @Override
                        public void DismissDialog(PropertyTransEntity entity) {
                            if (entity != null) {
                                if (isFrom) {
                                    fromassetid = entity.getAssetid();
                                    tv_transFrom.setText(entity.getItemname());
                                    XzbUtils.displayImage(iv_fromIcon, entity.getItemicon(), R.drawable.write_select_account);
                                } else {
                                    toassetid = entity.getAssetid();
                                    tv_transTo.setText(entity.getItemname());
                                    XzbUtils.displayImage(iv_toIcon, entity.getItemicon(), R.drawable.write_select_account);
                                }
                            }
                        }
                    });

                    getWindow().getDecorView().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            UIHelper.showSoftInputMethod(et_Money);
                        }
                    }, 100);
                } else {
                    showSimpleAlertDialog("", "您还未添加资产，请先添加资产再进行转账", "确定", "取消", false, true,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dismissDialog();
                                    Intent intent = new Intent(mContext, WealthAddActivity.class);
                                    ((Activity) mContext).startActivityForResult(intent, 1004);
                                }
                            },
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dismissDialog();
                                }
                            });
                }
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                UIHelper.showShortToast(mContext, simleMsg.getErrMsg(), 500);
            }
        });
    }

    /**
     * 转账
     */
    private void submit() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("fromassetid", fromassetid);
        hashMap.put("toassetid", toassetid);
        hashMap.put("amount", et_Money.getText().toString());
        CommonFacade.getInstance().exec(Constants.TRANSLATE_MONEY, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                showLoadingDialog();
            }

            @Override
            public void onFinish() {
                dismissDialog();
            }

            @Override
            public void onSuccess(Object o) {
                UIHelper.showShortToast(mContext, "成功", 500);
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
            case R.id.btn_save://保存
                if (StringUtils.isEmpty(et_Money.getText().toString())) {
                    UIHelper.showShortToast(this, "请输入转账金额", 500);
                    return;
                } else if (StringUtils.isEmpty(fromassetid)) {
                    UIHelper.showShortToast(this, "请选择转出账户", 500);
                    return;
                } else if (StringUtils.isEmpty(toassetid)) {
                    UIHelper.showShortToast(this, "请选择转入账户", 500);
                    return;
                } else if (fromassetid.equals(toassetid)) {
                    UIHelper.showShortToast(this, "同一账户不能实现转入转出哦", 500);
                } else {
                    submit();
                }
                break;
            case R.id.ll_transFrom:
                isFrom = true;
                if (selecterDialog != null) {
                    selecterDialog.show();
                }
                break;
            case R.id.ll_transTo:
                isFrom = false;
                if (selecterDialog != null) {
                    selecterDialog.show();
                }
                break;
            case R.id.rl_input:
                UIHelper.showSoftInputMethod(et_Money);
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1004) {
            loadData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (selecterDialog != null && selecterDialog.isShowing()) {
            selecterDialog.dismiss();
        }
        selecterDialog = null;
    }
}
