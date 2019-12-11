package com.yiqiji.money.modules.property.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.frame.core.utils.LogUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by dansakai on 2017/3/12.
 * 应收付款编辑页
 */

public class EditBorrowActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title;
    private TextView tv_getContent;
    private EditText et_getContent;
    private TextView tv_getMoney;
    private EditText et_getMoney;

    private Button btn_delet;
    private Button btn_etSave;

    private JSONObject jo_body = null;

    //传参
    private int itemType;
    private String assetid = "0";//id
    private String itemname = "";
    private String preinterest = "0";
    private String loantype = "0";
    private String loandate = String.valueOf(System.currentTimeMillis());
    private String remark = "";
    private String isremind = "0";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_borrow);
        initView();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("编辑");
        tv_getContent = (TextView) findViewById(R.id.tv_getContent);
        et_getContent = (EditText) findViewById(R.id.et_getContent);
        tv_getMoney = (TextView) findViewById(R.id.tv_getMoney);
        et_getMoney = (EditText) findViewById(R.id.et_getMoney);
        et_getMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        et_getMoney.setText(s);
                        et_getMoney.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    et_getMoney.setText(s);
                    et_getMoney.setSelection(2);
                }
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        et_getMoney.setText(s.subSequence(0, 1));
                        et_getMoney.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        btn_delet = (Button) findViewById(R.id.btn_delet);
        btn_delet.setOnClickListener(this);
        btn_etSave = (Button) findViewById(R.id.btn_etSave);
        btn_etSave.setOnClickListener(this);

        try {
            changeState();
        } catch (JSONException e) {
            LogUtil.log_error(null,e);
        }

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                UIHelper.showSoftInputMethod(et_getContent);
            }
        }, 100);

    }

    /**
     * @throws JSONException
     */
    private void changeState() throws JSONException{
        if (!StringUtils.isEmpty(getIntent().getStringExtra("jo_body"))) {
            jo_body = new JSONObject(getIntent().getStringExtra("jo_body"));
            et_getContent.setText(jo_body.optString("marktext"));
            if (!StringUtils.isEmpty(jo_body.optString("marktext"))) {
                et_getContent.setSelection(jo_body.optString("marktext").length());
            }

            if (!"0.00".equals(jo_body.optString("assetamount"))) {
                et_getMoney.setText(jo_body.optString("assetamount"));
            }
            itemType = Integer.parseInt(jo_body.optString("itemtype"));
            assetid = jo_body.optString("assetid");
            itemname = jo_body.optString("itemname");
            if (!StringUtils.isEmpty(jo_body.optString("attach"))) {
                JSONObject jo_atta = new JSONObject(jo_body.optString("attach"));
                preinterest = jo_atta.optString("preinterest");
                loantype = jo_atta.optString("loantype");
                loandate = jo_atta.optString("loandate");
                remark = jo_atta.optString("remark");
                isremind = jo_atta.optString("isremind");
            }
        }
        if (itemType == 19) {
            tv_getContent.setText("借钱给谁");
            tv_getMoney.setText("未收回本金");
        } else if (itemType == 20) {
            tv_getContent.setText("向谁借钱");
            tv_getMoney.setText("未归还本金");
        } else if (itemType == 21) {
            if ("0".equals(loantype)) {
                tv_getContent.setText("应收内容");
                tv_getMoney.setText("应收金额");
            } else {
                tv_getContent.setText("应付内容");
                tv_getMoney.setText("应付金额");
            }
        }
    }

    /**
     * 删除账户
     */
    private void delect() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", jo_body.optString("assetid"));

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
     * 保存
     */
    private void save() {
        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("marktext", et_getContent.getText().toString());
        String assetamount;
        if (!StringUtils.isEmpty(et_getMoney.getText().toString())) {
            assetamount = et_getMoney.getText().toString();
        } else {
            assetamount = "0";
        }
        hashMap.put("assetamount", assetamount);
        hashMap.put("assetid", assetid);
        hashMap.put("itemname", itemname);
        hashMap.put("preinterest", preinterest);
        hashMap.put("loantype", loantype);
        hashMap.put("loandate", loandate);
        hashMap.put("remark", remark);
        hashMap.put("isremind", isremind);

        CommonFacade.getInstance().exec(Constants.ADD_RENEW_LOAN, hashMap, new ViewCallBack() {
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
            case R.id.btn_etSave://保存
                if (StringUtils.isEmpty(et_getMoney.getText().toString())) {
                    UIHelper.showShortToast(this, "请输入金额", 500);
                    UIHelper.showSoftInputMethod(et_getMoney);
                    return;
                } else if (StringUtils.isEmpty(et_getContent.getText().toString())) {
                    UIHelper.showShortToast(this, "请输入" + et_getContent.getText().toString(), 500);
                    UIHelper.showSoftInputMethod(et_getContent);
                    return;
                }
                save();
                break;
        }
    }
}
