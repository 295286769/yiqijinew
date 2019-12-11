package com.yiqiji.money.modules.property.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.control.CustomDatePickerDialog;
import com.yiqiji.money.modules.common.control.LinearLayoutForDatePicker;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.property.entity.AddPropertyItemEntity;

import java.util.HashMap;

/**
 * Created by dansakai on 2017/3/9.
 * 新建 借入借出自定义
 */

public class PropertyNewGetpayActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_back;
    private TextView tv_title;
    private RadioGroup radio_group_wealth;

    private TextView tv_getContent;
    private EditText et_getContent;

    private TextView tv_getMoney;
    private EditText et_getMoney;

    private TextView tv_data;
    private TextView tv_dataDetail;

    private EditText et_marks;

    private Button btn_save;

    private AddPropertyItemEntity entity = null;
    private int itemType;

    private int type = 0;//:1.借入;2.借出

    private CustomDatePickerDialog mDatePicker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_getandpay);
        initView();
        changeData();
    }

    private void changeData() {
        if (itemType == 19) {//借出
            radio_group_wealth.setVisibility(View.GONE);
            tv_title.setText("新建借出");
            et_getContent.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
            et_getMoney.setInputType(EditorInfo.TYPE_CLASS_TEXT);
            tv_getContent.setText("借出金额");
            et_getContent.setHint("0.00(可选)");
            tv_getMoney.setText("借钱给谁");
            et_getMoney.setHint("请输入借款人名称(必填)");
            tv_data.setText("借出日期");
            et_getContent.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().contains(".")) {
                        if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                            s = s.toString().subSequence(0,
                                    s.toString().indexOf(".") + 3);
                            et_getContent.setText(s);
                            et_getContent.setSelection(s.length());
                        }
                    }
                    if (s.toString().trim().substring(0).equals(".")) {
                        s = "0" + s;
                        et_getContent.setText(s);
                        et_getContent.setSelection(2);
                    }
                    if (s.toString().startsWith("0")
                            && s.toString().trim().length() > 1) {
                        if (!s.toString().substring(1, 2).equals(".")) {
                            et_getContent.setText(s.subSequence(0, 1));
                            et_getContent.setSelection(1);
                            return;
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } else if (itemType == 20) {//借入
            radio_group_wealth.setVisibility(View.GONE);
            tv_title.setText("新建借入");
            et_getContent.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
            et_getMoney.setInputType(EditorInfo.TYPE_CLASS_TEXT);
            tv_getContent.setText("借入金额");
            et_getContent.setHint("0.00(可选)");
            tv_getMoney.setText("借入内容");
            et_getMoney.setHint("请输入借款人名称(必填)");
            tv_data.setText("借出日期");

            et_getContent.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().contains(".")) {
                        if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                            s = s.toString().subSequence(0,
                                    s.toString().indexOf(".") + 3);
                            et_getContent.setText(s);
                            et_getContent.setSelection(s.length());
                        }
                    }
                    if (s.toString().trim().substring(0).equals(".")) {
                        s = "0" + s;
                        et_getContent.setText(s);
                        et_getContent.setSelection(2);
                    }
                    if (s.toString().startsWith("0")
                            && s.toString().trim().length() > 1) {
                        if (!s.toString().substring(1, 2).equals(".")) {
                            et_getContent.setText(s.subSequence(0, 1));
                            et_getContent.setSelection(1);
                            return;
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        } else if (itemType == 21) {//自定义
            et_getContent.setInputType(EditorInfo.TYPE_CLASS_TEXT);
            et_getMoney.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
            radio_group_wealth.setVisibility(View.VISIBLE);
            tv_title.setText("新建自定义");
            tv_getContent.setText("应收内容");
            et_getContent.setHint("请输入应收内容(必填)");
            tv_getMoney.setText("应收金额");
            et_getMoney.setHint("0.00(可选)");
            tv_data.setText("借出日期");
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
        }
    }

    private void initView() {
        entity = (AddPropertyItemEntity) getIntent().getSerializableExtra("addproEntity");
        if (entity != null) {
            itemType = Integer.parseInt(entity.getItemtypeId());
        }

        radio_group_wealth = (RadioGroup) findViewById(R.id.radio_group_wealth);
        ((RadioButton) radio_group_wealth.getChildAt(0)).setChecked(true);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);

        tv_getContent = (TextView) findViewById(R.id.tv_getContent);
        et_getContent = (EditText) findViewById(R.id.et_getContent);
        UIHelper.showSoftInputMethod(et_getContent);
        tv_getMoney = (TextView) findViewById(R.id.tv_getMoney);
        et_getMoney = (EditText) findViewById(R.id.et_getMoney);
        tv_data = (TextView) findViewById(R.id.tv_data);
        tv_dataDetail = (TextView) findViewById(R.id.tv_dataDetail);
        tv_dataDetail.setOnClickListener(this);
        tv_dataDetail.setText(DateUtil.getTodayDate());
        et_marks = (EditText) findViewById(R.id.et_marks);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);

        radio_group_wealth.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_get://新建应收
                        tv_getContent.setText("应收内容");
                        tv_getMoney.setText("应收金额");
                        type = 0;//借出
                        break;
                    case R.id.rb_pay://新建应付
                        tv_getContent.setText("应付内容");
                        tv_getMoney.setText("应付金额");
                        type = 1;//借入
                        break;
                }
            }
        });

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                UIHelper.showSoftInputMethod(et_getContent);

            }
        }, 100);
    }

    /**
     * 提交新建
     */
    private void submit() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("assettype", entity.getCategoryid());
        hashMap.put("itemtype", entity.getItemtypeId());
        hashMap.put("itemname", entity.getCategoryname());
        hashMap.put("loandate", DateUtil.getTime(tv_dataDetail.getText().toString()));//时间戳
        hashMap.put("remark", et_marks.getText().toString());
        String assetAmount;
        if (itemType == 19 || itemType == 20) {

            if (!StringUtils.isEmpty(et_getContent.getText().toString())) {
                assetAmount = et_getContent.getText().toString();
            } else {
                assetAmount = "0";
            }
            hashMap.put("assetamount", assetAmount);//金额
            hashMap.put("marktext", et_getMoney.getText().toString());//借款名称
        } else if (itemType == 21) {
            if (!StringUtils.isEmpty(et_getMoney.getText().toString())) {
                assetAmount = et_getMoney.getText().toString();
            } else {
                assetAmount = "0";
            }
            hashMap.put("assetamount", assetAmount);//金额
            hashMap.put("marktext", et_getContent.getText().toString());//借款名称
            hashMap.put("loantype", String.valueOf(type));
        }

        CommonFacade.getInstance().exec(Constants.ADD_LOAN_ASSERT, hashMap, new ViewCallBack() {
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
                UIHelper.showShortToast(mContext,simleMsg.getErrMsg(),500);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_dataDetail://弹出日期选择
                // 创建并且显示对话框
                if (mDatePicker == null) {
                    String[] datas = DateUtil.getTodayDate().split("-");

                    mDatePicker = new CustomDatePickerDialog(mContext,
                            new LinearLayoutForDatePicker.DatePickListener() {

                                @Override
                                public void DatePick(int leftValue, int middleValue, int rightValue,String string) {
                                    tv_dataDetail.setText(leftValue + "-" + middleValue + "-" + rightValue);
                                    mDatePicker.dismiss();
                                }
                            });
                    mDatePicker
                            .setTitle("请选择起息时间")
                            .setYearMonthDayRange(1990, 2020, 1, 12, 1, 31);
                    if (datas != null && datas.length == 3) {
                        mDatePicker.setCurrentSelectTime(Integer.parseInt(datas[0]), Integer.parseInt(datas[1]), Integer.parseInt(datas[2]));
                    }
                }

                mDatePicker.showDialog();
                break;
            case R.id.btn_save://保存
                if (itemType == 19) {
                    if (StringUtils.isEmpty(et_getMoney.getText().toString())) {
                        UIHelper.showShortToast(this, "请输入借款人名称", 500);
                        UIHelper.showSoftInputMethod(et_getMoney);
                        return;
                    } else {
                        submit();
                    }
                } else if (itemType == 20) {
                    if (StringUtils.isEmpty(et_getMoney.getText().toString())) {
                        UIHelper.showShortToast(this, "请输入借款人名称", 500);
                        UIHelper.showSoftInputMethod(et_getMoney);
                        return;
                    } else {
                        submit();
                    }
                } else if (itemType == 21) {
                    if (StringUtils.isEmpty(et_getContent.getText().toString())) {
                        if (type == 1) {
                            UIHelper.showShortToast(this, "请输入应收内容", 500);
                        } else {
                            UIHelper.showShortToast(this, "请输入应付内容", 500);
                        }
                        UIHelper.showSoftInputMethod(et_getContent);
                        return;
                    } else {
                        submit();
                    }
                }

                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDatePicker != null && mDatePicker.isShowing()) {
            mDatePicker.dismiss();
            mDatePicker = null;
        }
    }
}
