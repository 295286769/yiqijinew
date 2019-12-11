package com.yiqiji.money.modules.homeModule.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.entity.eventbean.IntentMessageBean;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.homeModule.home.entity.BudgetResponse;
import com.yiqiji.money.modules.homeModule.home.wegit.CashierInputFilter;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

public class SetBudgetActivity extends BaseActivity implements OnClickListener {
    private EditText edit_budget;
    private TextView add;
    private String budget;
    private IntentMessageBean intentMessageBean = null;
    private String bookId;
    private String month;
    private String sorttype;

    public static void openActivity(Context context, String bookId, String month, String sorttype) {
        XzbUtils.hidePointInUmg(context, Constants.HIDE_SET_BUDGET);
        Intent intent = new Intent(context, SetBudgetActivity.class);
        intent.putExtra("bookId", bookId);
        intent.putExtra("month", month);
        intent.putExtra("sorttype", sorttype);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_budget);
        bookId = getIntent().getStringExtra("bookId");
        month = getIntent().getStringExtra("month");
        sorttype = getIntent().getStringExtra("sorttype");
        String budgetkey = DateUtil.getBudgetkey(bookId, month, sorttype);
        budget = LoginConfig.getInstance().getBudget(budgetkey);
        initView();
    }

    private void initView() {
        add = (TextView) findViewById(R.id.add);
        initTitle("设置预算", this);
        add.setOnClickListener(this);
        edit_budget = (EditText) findViewById(R.id.edit_budget);

        if (budget.equals("0") || budget.equals("0.00")) {
            edit_budget.setHint("0.00");
        } else {
            edit_budget.setText(StringUtils.moneyAntiformatComma(budget));
            edit_budget.setSelection(edit_budget.getText().length());
        }
        InputFilter[] inputFilters = {new CashierInputFilter()};
        edit_budget.setFilters(inputFilters);
//        edit_budget.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (!s.toString().contains(".") && s.toString().length() > 7) {
//                    s = s.toString().subSequence(0, 7);
//                    edit_budget.setText(s);
//                    edit_budget.setSelection(s.length());
//                    return;
//                }
//
//                if (s.toString().contains(".")) {
//                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
//                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
//
//                        edit_budget.setText(s);
//                        edit_budget.setSelection(s.length());
//                    }
//
//                }
//                if (s.toString().trim().substring(0).equals(".")) {
//                    s = "0" + s;
//                    edit_budget.setText(s);
//                    edit_budget.setSelection(2);
//                }
//
//                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
//                    if (!s.toString().substring(1, 2).equals(".")) {
//                        edit_budget.setText(s.subSequence(0, 1));
//                        edit_budget.setSelection(1);
//                        return;
//                    }
//                }
//
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.add:// 确定
                budget = edit_budget.getText().toString();
//                if (TextUtils.isEmpty(budget)) {
//                    ToastUtils.DiyToast(this, "请输入预算金额");
//                    return;
//                }·
//                if (Float.parseFloat(budget) <= 0) {
//                    ToastUtils.DiyToast(this, "请输入正确金额");
//                    return;
//                }

                if (TextUtils.isEmpty(budget)) {
                    budget = "0";
                }

                budget = XzbUtils.formatDouble("%.2f", Double.valueOf(budget));

//                if (!InternetUtils.checkNet(MyApplicaction.getContext())) {// 未联网状态
//                    LoginConfig.getInstance().setBudget(bookId, budget);
//                    intentMessageBean = new IntentMessageBean();
//                    intentMessageBean.setBudget(budget);
//                    EventBus.getDefault().post(intentMessageBean);
//
//                    finish();
//                    return;
//                }

                HashMap<String, String> hashMap = DateUtil.getmapParama("id", bookId, "amount", budget + "", "month", month);
                hashMap.put("tokenid", LoginConfig.getInstance().getTokenId());
                hashMap.put("deviceid", LoginConfig.getInstance().getDeviceid());
                hashMap.put("id", bookId);
                hashMap.put("amount", budget + "");
                if (sorttype.equals("0")) {
                    hashMap.put("month", "");
                } else {
                    hashMap.put("month", month);
                }
                CommonFacade.getInstance().exec(Constants.BUDGET, hashMap, new ViewCallBack<BudgetResponse>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showLoadingDialog();
                    }

                    @Override
                    public void onSuccess(BudgetResponse o) throws Exception {
                        super.onSuccess(o);
                        BudgetResponse budgetResponse = o;
                        String budgetkey = DateUtil.getBudgetkey(bookId, month, sorttype);
                        LoginConfig.getInstance().setBudget(budgetkey, budget);
                        if (Double.parseDouble(budget) == 0) {
                            LoginConfig.getInstance().setBudget(budgetkey, "0");
                        }

                        intentMessageBean = new IntentMessageBean();
                        intentMessageBean.setBudget(budget);
                        EventBus.getDefault().post(intentMessageBean);

                        finish();
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

                break;
            case R.id.layout_title_view_return:// 返回
                finish();
                break;

            default:
                break;
        }

    }

}
